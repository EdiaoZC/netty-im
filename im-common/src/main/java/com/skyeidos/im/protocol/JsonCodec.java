package com.skyeidos.im.protocol;

import com.skyeidos.im.command.Command;
import com.skyeidos.im.entity.Packet;
import com.skyeidos.im.entity.request.LoginRequestPacket;
import com.skyeidos.im.entity.request.MessageRequestPacket;
import com.skyeidos.im.entity.response.LoginResponsePacket;
import com.skyeidos.im.entity.response.MessageResponsePacket;
import com.skyeidos.im.serialize.JSONSerializer;
import com.skyeidos.im.serialize.Serializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class JsonCodec {

    public static final JsonCodec INSTANCE = new JsonCodec();

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        serializerMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf buf, Packet packet) {
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(packet.getVersion());
        buf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        buf.writeByte(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(4);
        byteBuf.skipBytes(1);
        byte serializeAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deSerialize(bytes, requestType);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

    public static <T extends Packet> void registerType(byte command, Class<T> clazz) {
        packetTypeMap.put(command, clazz);
    }

    public static void registerSerialize(byte serializeAlgorithm, Serializer serializer) {
        serializerMap.put(serializeAlgorithm, serializer);
    }
}
