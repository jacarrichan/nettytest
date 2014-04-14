package com.cp.test;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.cp.netty.coder.Decoder;
import com.cp.netty.coder.Encoder;

public class ClientPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new Decoder(Integer.MAX_VALUE, 0, 4));
		pipeline.addLast("encoder", new Encoder(4));
		Timer timer = new HashedWheelTimer();
		pipeline.addLast("timeout", new IdleStateHandler(timer, 5, 5, 0));
		// 注释hearbeat可以观察心跳包的作用效果
		pipeline.addLast("hearbeat", new Heartbeat());
		pipeline.addLast("handler", new ClientHandler());

		return pipeline;

	}

}
