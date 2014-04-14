package com.cp.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;
import com.cp.utils.StreamUtils;

public class ClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		Channel c = e.getChannel();
		StreamUtils s = new StreamUtils();
		// 协议号1000
		s.writeInt(1000);
		// 玩家ID 1
		s.writeInt(1);
		// 协议类型
		s.writeInt(1);
		// 协议生成时间
		s.writeLong(System.currentTimeMillis());

		GameRequest gameRequest = new GameRequest(c, s.getBytesM());
		GameResponse g = new GameResponse(gameRequest);
		// 协议体
		g.putLong(1L);
		c.write(g);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

		ChannelBuffer buffs = (ChannelBuffer) e.getMessage();
		buffs.skipBytes(4);// 越过dataLength的字节
		byte[] decoded = new byte[buffs.readableBytes()];
		buffs.readBytes(decoded);
		logger.debug("客户端  recevie : {}", ByteUtilities.asHex(decoded));
		InputStream in = new ByteArrayInputStream(decoded);
		try {
			int commandId = StreamUtils.readInt(in);
			int playerId = StreamUtils.readInt(in);
			int commandType = StreamUtils.readInt(in);
			long f = StreamUtils.readLong(in);

			long serverTime = StreamUtils.readLong(in);
			logger.debug("客户端  recevie 协议体数据: {}", Long.toHexString(serverTime));
			logger.debug("client received message:" + commandId + "\t" + playerId + "\t" + commandType + "\t" + f
					+ "\t" + serverTime);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.debug("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}
}
