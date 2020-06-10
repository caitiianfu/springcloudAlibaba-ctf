package com.unicom.netty.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** @author by ctf @Classsname NettyServer @Description TODO @Date 2020/6/2 15:26 */
@Service
@Slf4j
public class NettyServer {
  public void run(int port) {
    new Thread(() -> {}).start();
  }

  private void runServer(int port) throws InterruptedException {
    log.info("服务器端启动");
    // 主线程
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    // 从线程
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      // netty服务器创建，serverbootstrap是启动类
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup); // 设置主从线程
      b.channel(NioServerSocketChannel.class); // 设置nio双向通道
      b.childHandler(
          new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();
              pipeline.addLast("codec-http", new HttpServerCodec());
              pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
              pipeline.addLast("handler", new MyWebSocketServerHandler());
            }
          });
      Channel ch = b.bind(port).sync().channel();
      log.info("服务器启动成功：  {}", ch.toString());
      ch.closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      log.info("服务已关闭");
    }
  }
}
