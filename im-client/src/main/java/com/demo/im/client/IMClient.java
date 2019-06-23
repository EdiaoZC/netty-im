package com.demo.im.client;

import com.demo.im.biz.util.SessionUtil;
import com.demo.im.client.handler.LoginResponseHandler;
import com.demo.im.client.handler.MessageResponseHandler;
import com.demo.im.codec.PacketCodec;
import com.demo.im.entity.request.LoginRequestPacket;
import com.demo.im.entity.request.MessageRequestPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IMClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup work = new NioEventLoopGroup();

        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        socketChannel.pipeline().addLast(new PacketCodec());
                        socketChannel.pipeline().addLast(new LoginResponseHandler());
                        socketChannel.pipeline().addLast(new MessageResponseHandler());
                    }
                });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.debug("连接成功!");
                        Channel channel = ((ChannelFuture) future).channel();
                        startConsoleThread(channel);
                    } else if (retry == 0) {
                        log.error("重试次数已用完，放弃连接！");
                    } else {
                        // 第几次重连
                        int order = (MAX_RETRY - retry) + 1;
                        // 本次重连的间隔
                        int delay = 1 << order;
                        log.error("连接失败，第{}次重连……", order);
                        bootstrap.config().group()
                                .schedule(() -> connect(bootstrap, host, port, retry - 1)
                                        , delay, TimeUnit.SECONDS);
                    }
                });
    }

    private static void startConsoleThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.print("输入用户名登录: ");
                    String username = sc.nextLine();
                    loginRequestPacket.setUserName(username);

                    // 密码使用默认的
                    loginRequestPacket.setPassword("pwd");

                    // 发送登录数据包
                    channel.writeAndFlush(loginRequestPacket);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                } else {
                    String userId = sc.next();
                    String message = sc.next();
                    channel.writeAndFlush(new MessageRequestPacket(userId, message));
                }
            }
        }).start();
    }
}
