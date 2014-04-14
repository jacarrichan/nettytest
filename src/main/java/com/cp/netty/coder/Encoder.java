package com.cp.netty.coder;

import org.apache.mina.proxy.utils.ByteUtilities;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cp.netty.domain.GameResponse;

public class Encoder extends LengthFieldPrepender {
	private Logger LOG = LoggerFactory.getLogger(Encoder.class);

	public Encoder(int lengthFieldLength) {
		super(lengthFieldLength);
	}

	@Override
	protected Object encode(ChannelHandlerContext cxt, Channel channel, Object msg) throws Exception {

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		GameResponse response = (GameResponse) msg;
		// 20=4*3+8*1
		buffer.writeInt(response.getRtMessage().length + 20);
		buffer.writeInt(response.getCommondId());
		buffer.writeInt(response.getPlayerId());
		buffer.writeInt(response.getCommandType());
		buffer.writeLong(response.getTime());
		buffer.writeBytes(response.getRtObj().getBytesM());
		byte[] out = new byte[buffer.writerIndex() - buffer.readerIndex()];
		buffer.getBytes(buffer.readerIndex(), out);
		LOG.debug("发送数据{}", ByteUtilities.asHex(out));
		LOG.debug("发送协议体数据{}", ByteUtilities.asHex(response.getRtObj().getBytesM()));
		return buffer;

	}
}
