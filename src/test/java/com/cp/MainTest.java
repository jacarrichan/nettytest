package com.cp;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cp.netty.ServerPipelineFactory;

public class MainTest {

	public static void main(String[] args) {
		ApplicationContext factory = new ClassPathXmlApplicationContext(new String[] { "propholder.xml" });

		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		ServerPipelineFactory httpServerPipelineFactory = (ServerPipelineFactory) factory
				.getBean("serverPipelineFactory");
		bootstrap.setPipelineFactory(httpServerPipelineFactory);
		// 启动端口 8888
		bootstrap.bind(new InetSocketAddress(8888));
		System.out.println("8888  server is starting……");

	}
}
