/**    
 * 文件名：Client.java    
 *    
 * 版本信息：    
 * 日期：2018年3月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.netty;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import fastRpc.jason.inet.JYSocket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**    
 *     
 * 项目名称：Netty    
 * 类名称：Client    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月19日 上午12:00:27    
 * 修改人：jinyu    
 * 修改时间：2018年3月19日 上午12:00:27    
 * 修改备注：    
 * @version     
 *     
 */
public class EchoClient {
  private   String host = "127.0.0.1";
  private   int port = Integer.parseInt("8080");
  private  ChannelFuture  client=null;
  private EchoClientHandler hander=null;
  private InnerRecviceHander rechander=null;
  public void set(ChannelFuture client)
  {
      
  }
    public EchoClient() {  
        this("localhost", 8989);  
    }  
  
    public EchoClient(String host) {  
        this(host, 8989);  
    }  
  
    public EchoClient(String host, int port) {  
        this.host = host;  
        this.port = port;  
    }  
    public    void  sendData(byte[]data)
    {
        client.channel().writeAndFlush(data);
    }
    public String getAddr()
    {
        InetSocketAddress insocket = (InetSocketAddress) client.channel().remoteAddress();
        return insocket.getAddress().getHostAddress();
    }
    public  void start()
    {
        NioEventLoopGroup workersGroup = new NioEventLoopGroup(1); 
        try {  
            Bootstrap bootstrap = new Bootstrap();  
            hander=new EchoClientHandler();
            rechander=new InnerRecviceHander();
            hander.reader=rechander;
            bootstrap.group(workersGroup)  
                    .channel(NioSocketChannel.class)  
                    .remoteAddress(host, port)  
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override  
                        protected void initChannel(SocketChannel ch) throws Exception {  
                            ch.pipeline().addLast(new LineBasedFrameDecoder(2048));  
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));  
                            ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));  
                            ch.pipeline().addLast(hander);  
                        }  
                    });  
            ChannelFuture channelFuture = bootstrap.connect().sync();  
            channelFuture.channel().closeFuture().sync();  
            this.client=channelFuture;
        }
        catch(Exception ex)
         {
      
         }
          finally {  
            workersGroup.shutdownGracefully();  
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
       this.client.channel().close();
   }
}
