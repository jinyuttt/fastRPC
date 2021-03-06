/**    
 * 文件名：DiscardServer.java    
 *    
 * 版本信息：    
 * 日期：2018年3月18日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.netty;

import fastRpc.jason.inet.JYSocket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**    
 *     
 * 项目名称：Netty    
 * 类名称：DiscardServer    
 * 类描述：    netty服务端
 * 创建人：jinyu    
 * 创建时间：2018年3月18日 下午11:52:12    
 * 修改人：jinyu    
 * 修改时间：2018年3月18日 下午11:52:12    
 * 修改备注：    
 * @version     
 *     
 */
public class EchoServer {
    private int localPort;
    private String localIP="";
   public  EchoServerHandler  handler=new EchoServerHandler();
   private InnerRecviceHander rechander=null;
    public EchoServer(int port) {
        this.localPort = port;
    }
    public EchoServer(String ip,int port)
    {
        this.localIP=ip;
        this.localPort=port;
    }
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); 
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            rechander=new InnerRecviceHander();
            handler.hand=rechander;
            ServerBootstrap b = new ServerBootstrap(); 
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) 
             .childHandler(new ChannelInitializer<SocketChannel>() { 
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(handler);
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128) //设置TCP缓冲区  
             .option(ChannelOption.SO_SNDBUF, 32 * 1024) //设置发送数据缓冲大小  
             .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接受数据缓冲大小  
             .childOption(ChannelOption.SO_KEEPALIVE, true); //保持连接 
            ChannelFuture f =null;
                   if(localIP.isEmpty())
                   {
                      f= b.bind(localPort).sync(); // (7)
                   }
                   else
                   {
                       f=b.bind(localIP, localPort).sync();
                   }
                   f.channel().closeFuture().sync(); 
            System.out.println("Server Starting");
           // f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    /**
     * 
     * @return
     */
    public JYSocket recvice()
    {
        return rechander.getData();
    }
   public void close()
   {
       
   }
}
