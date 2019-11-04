package com.skyeidos.im.server.handler;

import com.skyeidos.im.biz.util.SessionUtil;
import com.skyeidos.im.entity.Session;
import com.skyeidos.im.entity.request.LoginRequestPacket;
import com.skyeidos.im.entity.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket requestPacket) throws Exception {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(requestPacket.getVersion());
        responsePacket.setUserName(requestPacket.getUserName());
        if (valid(requestPacket)) {
            responsePacket.setSuccess(true);
            String userId = randomUserId();
            responsePacket.setUserId(userId);
            log.debug("{} 登陆成功", requestPacket.getUserName());
            SessionUtil.bindSession(new Session(userId, requestPacket.getUserName()), ctx.channel());
        } else {
            responsePacket.setSuccess(false);
            responsePacket.setMsg("用户名或密码错误");
            log.debug("{} 登陆失败", requestPacket.getUserName());
        }
        ctx.channel().writeAndFlush(responsePacket);
    }

    private boolean valid(LoginRequestPacket requestPacket) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }

}
