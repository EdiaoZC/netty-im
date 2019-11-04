package com.skyeidos.im.codec;

import com.skyeidos.im.entity.Packet;
import com.skyeidos.im.protocol.JsonCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        JsonCodec.INSTANCE.encode(out, msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(JsonCodec.INSTANCE.decode(in));
    }
}
