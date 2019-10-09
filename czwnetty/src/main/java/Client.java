import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.CharsetUtil;
import pojo.PeoplePojo;

public class Client {
    public static void main(String[] args) {
        EventLoopGroup gp = new NioEventLoopGroup();
        try {
            Bootstrap sb = new Bootstrap();
            sb.group(gp).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChildClientHandler());
            ChannelFuture future = sb.connect("127.0.0.1", 8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            gp.shutdownGracefully();
        }
    }
}



class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf buf = (ByteBuf) msg;
        int i = buf.readableBytes();
        byte [] bufarray = new byte [i];
        buf.readBytes(bufarray);
        System.out.println("msg :" + new String(bufarray, CharsetUtil.UTF_8));*/
        PeoplePojo pojo = (PeoplePojo) msg;
        System.out.println(pojo);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        /*byte[] bytes = "server channelActive".getBytes();
        ByteBuf buffer = Unpooled.buffer(bytes.length);
        buffer.writeBytes(bytes);
        ctx.writeAndFlush(buffer);*/
        PeoplePojo p = new PeoplePojo();
        p.setName("jack");
        p.setCode("200");
        ctx.writeAndFlush(p);
    }
}

class ChildClientHandler extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel nioServerSocketChannel) throws Exception {
        nioServerSocketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                .addLast(new ObjectEncoder())
                .addLast(new ClientHandler());
    }
}
