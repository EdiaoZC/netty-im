package com.demo.im.codec;

import com.demo.im.entity.Packet;
import com.demo.im.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class PacketCodec {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    protected ByteBuf encode(Packet packet) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(packet.getVersion());
        buf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        buf.writeByte(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        return buf;
    }


    protected Packet decode(ByteBuf byteBuf) throws Exception {
        byteBuf.skipBytes(4);
        byteBuf.skipBytes(1);
        int serializeAlgorithm = byteBuf.readableBytes();
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

    private Serializer getSerializer(int serializeAlogrithm) {
        return null;
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return null;
    }
}
