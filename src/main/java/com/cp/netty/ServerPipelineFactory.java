package com.cp.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.cp.netty.coder.Decoder;
import com.cp.netty.coder.Encoder;

public class ServerPipelineFactory implements ChannelPipelineFactory {
	public ServerHandler serverHandler;

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeLine = Channels.pipeline();
		pipeLine.addLast("decoder", new Decoder(Integer.MAX_VALUE, 0, 4));
		pipeLine.addLast("encoder", new Encoder(4));
		Timer timer = new HashedWheelTimer();
		pipeLine.addLast("timeout", new IdleStateHandler(timer, 10, 8, 0));
		pipeLine.addLast("hearbeat", new Heartbeat());
		pipeLine.addLast("handler", serverHandler);
		return pipeLine;
	}

	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

}
