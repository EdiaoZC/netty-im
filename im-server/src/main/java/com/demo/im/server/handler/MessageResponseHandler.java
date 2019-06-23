package com.demo.im.server.handler;

import com.demo.im.entity.request.MessageRequestPacket;
import com.demo.im.entity.response.MessageResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket requestPacket) throws Exception {
        log.debug("收到客户端消息: {}", requestPacket.getMessage());
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMessage("服务端回复【" + requestPacket.getMessage() + "】");
        ctx.channel().writeAndFlush(responsePacket);
    }
}
