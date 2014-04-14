package com.cp.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class Heartbeat extends IdleStateAwareChannelHandler {
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		super.channelIdle(ctx, e);
		System.out.println(System.currentTimeMillis());
		if (e.getState() == IdleState.WRITER_IDLE) {
			System.out.println("不给我发数据,我就把你断了.");
			e.getChannel().close();
		} else if (e.getState() == IdleState.READER_IDLE) {
			System.out.println("发数据超时,我就把你断了.");
			e.getChannel().close();
		}
	}
}