package com.demo.im.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        log.info("服务端读取数据->{}", buf.toString(CharsetUtil.UTF_8));
        ByteBuf out = getByteBuf(ctx);
        ctx.channel().writeAndFlush(out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {

        ByteBuf result = ctx.alloc().buffer();
        byte[] bytes = "hello too".getBytes(CharsetUtil.UTF_8);
        result.writeBytes(bytes);
        return result;
    }
}
