package com.skyeidos.im.client.handler;

import com.skyeidos.im.biz.util.SessionUtil;
import com.skyeidos.im.entity.Session;
import com.skyeidos.im.entity.request.LoginRequestPacket;
import com.skyeidos.im.entity.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接,并开始登陆");
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUserName("eidos");
        loginRequestPacket.setPassword("As110695");
        ctx.writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket) throws Exception {
        String userId = responsePacket.getUserId();
        String userName = responsePacket.getUserName();
        if (responsePacket.isSuccess()) {
            log.info("[{}]登录成功，userId 为: {}", userName, responsePacket.getUserId());
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            log.error("[{}]登录失败，原因：{}", userName, responsePacket.getMsg());
        }
    }
}

