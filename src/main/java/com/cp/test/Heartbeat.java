package com.cp.test;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;
import com.cp.utils.StreamUtils;

public class Heartbeat extends IdleStateAwareChannelHandler {
	private static Logger LOG = LoggerFactory.getLogger(Heartbeat.class);

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		super.channelIdle(ctx, e);
		if (e.getState() == IdleState.WRITER_IDLE) {
			LOG.debug("发送{}心跳包", "写跳包");
			Channel c = e.getChannel();
			StreamUtils s = new StreamUtils();
			// 心跳包的协议号0000
			s.writeInt(0000);
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
		} else if (e.getState() == IdleState.READER_IDLE) {
			LOG.debug("发送{}心跳包", "读跳包");
		}
	}
}