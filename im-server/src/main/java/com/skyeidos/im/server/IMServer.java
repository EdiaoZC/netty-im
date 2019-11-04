package com.skyeidos.im.server;

import com.skyeidos.im.codec.PacketCodec;
import com.skyeidos.im.server.handler.AuthHandler;
import com.skyeidos.im.server.handler.LoginRequestHandler;
import com.skyeidos.im.server.handler.MessageResponseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IMServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        socketChannel.pipeline().addLast(new PacketCodec());
                        socketChannel.pipeline().addLast(new LoginRequestHandler());
                        socketChannel.pipeline().addLast(new AuthHandler());
                        socketChannel.pipeline().addLast(new MessageResponseHandler());
                    }
                });
        bind(bootstrap, PORT);
    }

    private static void bind(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        log.debug("端口[{}]绑定成功!", port);
                    } else {
                        log.error("端口[{}]绑定失败!", port);
                    }
                });
    }
}
