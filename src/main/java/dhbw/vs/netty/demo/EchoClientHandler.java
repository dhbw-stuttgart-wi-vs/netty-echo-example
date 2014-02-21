package dhbw.vs.netty.demo;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(EchoClientHandler.class);

	private final ByteBuf firstMessage = Unpooled.copiedBuffer("Das ist eine Nachricht...!".getBytes());

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		log.debug("Channel active, sending first message ({})", firstMessage.toString(Charset.defaultCharset()));
		ctx.writeAndFlush(firstMessage);
	}

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("Received: {}", ((ByteBuf)msg).toString(Charset.defaultCharset()));
    }
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.debug("Channel disconnected");
		ctx.close();
	}

}