package com.cp.netty;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.mina.proxy.utils.ByteUtilities;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cp.game.HandlerDispatcher;
import com.cp.game.domain.MessageQueue;
import com.cp.netty.domain.GameRequest;

public class ServerHandler extends SimpleChannelUpstreamHandler {
	public final static Logger log = LoggerFactory.getLogger(ServerHandler.class);
	public static HandlerDispatcher handlerDispatcher;

	public void init() {
		new Thread(handlerDispatcher).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelConnected
	 * (org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent) 建立一个新channel
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.debug("进来一个channel：" + ctx.getChannel().getId());
		MessageQueue messageQueue = new MessageQueue(new ConcurrentLinkedQueue<GameRequest>());
		handlerDispatcher.addMessageQueue(ctx.getChannel().getId(), messageQueue);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#channelDisconnected
	 * (org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ChannelStateEvent) 玩家主动关闭channel
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.error("关掉一个channel：" + ctx.getChannel().getId());
		handlerDispatcher.removeMessageQueue(e.getChannel().getId().toString());
		e.getChannel().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#exceptionCaught(
	 * org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.ExceptionEvent) 玩家被动关闭channel
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error("出异常啦……" + ctx.getChannel().getId());
		e.getCause().printStackTrace();
		handlerDispatcher.removeMessageQueue(e.getChannel().getId().toString());
		e.getChannel().close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.netty.channel.SimpleChannelUpstreamHandler#messageReceived(
	 * org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.MessageEvent) 消息接收处理器
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		ChannelBuffer buffs = (ChannelBuffer) e.getMessage();
		buffs.skipBytes(4);// 越过dataLength的字节
		byte[] decoded = new byte[buffs.readableBytes()];
		buffs.readBytes(decoded);
		log.debug("收到的业务数据:{}", ByteUtilities.asHex(decoded));
		Channel channel = e.getChannel();
		GameRequest gameRequest = new GameRequest(channel, decoded);

		// 通知回调协议
		handlerDispatcher.addMessage(gameRequest);
	}

	public HandlerDispatcher getHandlerDispatcher() {
		return handlerDispatcher;
	}

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		ServerHandler.handlerDispatcher = handlerDispatcher;
	}

}
