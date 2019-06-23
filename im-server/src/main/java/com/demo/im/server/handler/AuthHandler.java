package com.demo.im.server.handler;

import com.demo.im.biz.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            super.channelRead(ctx,msg);
            ctx.pipeline().remove(this);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            log.info("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            log.info("无登录验证，强制关闭连接!");
        }
    }
}
