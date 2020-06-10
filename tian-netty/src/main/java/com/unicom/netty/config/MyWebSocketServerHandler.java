package com.unicom.netty.config;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;

/** @author by ctf @Classsname MyWebSocketServerHandler @Description TODO @Date 2020/6/2 16:27 */
@Slf4j
public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
  private static final String WEBSOCKET_PATH = "";
  private WebSocketServerHandshaker handshaker;

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
    if (o instanceof FullHttpRequest) {
      // 以http请求接入，但是走websocket
      handleHttpRequest(ctx, (FullHttpRequest) o);
    } else if (o instanceof WebSocketFrame) {
      // 处理websocket客户端的消息
      handleWebSocketFrame(ctx, (WebSocketFrame) o);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
    // 要求upgrade为websocket，过滤掉get、post
    if (!request.decoderResult().isSuccess()
        || !"websocket".equals(request.headers().get("Upgrade"))) {
      // 若不是websocket形式，则创建BAD_REQUEST的req返回给客户端
      sendHttpResponse(
          ctx,
          request,
          new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
      return;
    }
    WebSocketServerHandshakerFactory factory =
        new WebSocketServerHandshakerFactory("ws://localhost:9535/websocket", null, false);
    handshaker = factory.newHandshaker(request);
    if (handshaker == null) {
      WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
    } else {
      handshaker.handshake(ctx.channel(), request);
    }
  }

  private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    // 检测是否关闭会话窗口
    if (frame instanceof CloseWebSocketFrame) {
      handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
      return;
    }
    if (frame instanceof PingWebSocketFrame) {
      ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
      return;
    }
    if (!(frame instanceof TextWebSocketFrame)) {
      log.info("数据格式不支持");
      throw new UnsupportedOperationException(
          String.format("%s frame types not supported", frame.getClass().getName()));
    }
    String request = ((TextWebSocketFrame) frame).text();
    // 这个有问题
    if (request.equals("heart")) {
      ctx.channel().write(new TextWebSocketFrame(request));
      return;
    }
  }

  private static void sendHttpResponse(
      ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
    // 返回应答给客户端
    if (res.status().code() != 200) {
      /*  ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
      res.content().writeBytes(buf);
      buf.release();*/
    }
    ChannelFuture f = ctx.channel().writeAndFlush(res);
    // 如果是非Keep-Alive，关闭连接
    //    if (!isKeepAlive(req) || res.status().code() != 200) {
    //      f.addListener(ChannelFutureListener.CLOSE);
    //    }
  }
}
