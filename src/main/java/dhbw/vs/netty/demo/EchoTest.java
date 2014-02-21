package dhbw.vs.netty.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import de.uniluebeck.itm.util.logging.Logging;

public class EchoTest {

	public static void main(String[] args) throws InterruptedException {
		Logging.setLoggingDefaults();
		final Logger log = LoggerFactory.getLogger(EchoTest.class);

		// Event loops for the server and client
		final EventLoopGroup serverBossEventGroup = new NioEventLoopGroup();
		final EventLoopGroup serverWorkerEventGroup = new NioEventLoopGroup();
		final EventLoopGroup clientEventGroup = new NioEventLoopGroup();

		try {
			// Configure the server.
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(serverBossEventGroup, serverWorkerEventGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new EchoServerHandler(serverBossEventGroup));
						}
					});

			// Start the server.
			ChannelFuture serverChannelFuture = serverBootstrap.bind(1234).sync();

			Bootstrap clientBootstrap = new Bootstrap();
			clientBootstrap.group(clientEventGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new EchoClientHandler());
						}
					});

			// Start the client.
			ChannelFuture clientChannelFuture = clientBootstrap.connect("localhost", 1234).sync();

			// Wait until all connections are closed.
			clientChannelFuture.channel().closeFuture().sync();
			serverChannelFuture.channel().closeFuture().sync();

			log.debug("Shutting down. Please wait...");
		} finally {
			// Shut down all event loops to terminate all threads.
			serverBossEventGroup.shutdownGracefully().await();
			serverWorkerEventGroup.shutdownGracefully().await();
			clientEventGroup.shutdownGracefully().await();
		}

		log.debug("Done.");
	}
}
