package dhbw.vs.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(EchoServerHandler.class);
	private EventLoopGroup eventLoopGroup;

	public EchoServerHandler(EventLoopGroup eventLoopGroup) {
		super();
		this.eventLoopGroup = eventLoopGroup;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// Log the received message
		log.info("Received: {}", ((ByteBuf) msg).toString(Charset.defaultCharset()));

		// Echo the received message back to the client
		ctx.write(msg);

		// Disconnect and shut the server down
		// ctx.disconnect();
		// eventLoopGroup.shutdownGracefully();
	}

}