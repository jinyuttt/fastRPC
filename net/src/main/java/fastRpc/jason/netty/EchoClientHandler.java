/**    
 * 文件名：TimeClientHandler.java    
 *    
 * 版本信息：    
 * 日期：2018年3月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.netty;


import java.net.InetSocketAddress;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;


/**    
 *     
 * 项目名称：Netty    
 * 类名称：TimeClientHandler    
 * 类描述：    客户端处理数据
 * 创建人：jinyu    
 * 创建时间：2018年3月19日 上午12:01:10    
 * 修改人：jinyu    
 * 修改时间：2018年3月19日 上午12:01:10    
 * 修改备注：    
 * @version     
 *     
 */
public class EchoClientHandler extends  ChannelHandlerAdapter   {
public IRecvieHander reader=null;

    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        ctx.close();  
    }  

    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
     //   ctx.writeAndFlush(NettyUtil.appenEndOfLine("我要连接...."));  
 //       new Thread(new Hander(ctx)).start();  
    }  

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        try {  
           
            JYSocket recvicer=new JYSocket();
            recvicer.data=(byte[]) msg;
            recvicer.isTcpType=true;
            InetSocketAddress localAddr = (InetSocketAddress) ctx.channel().localAddress();
            recvicer.localIP=localAddr.getAddress().getHostAddress();
            recvicer.localPort=localAddr.getPort();
            InetSocketAddress remoteAddr=(InetSocketAddress) ctx.channel().remoteAddress();
            recvicer.srvIP=remoteAddr.getAddress().getHostAddress();
            recvicer.srvPort=remoteAddr.getPort();
            reader.recviceData(recvicer);
            
        } finally {  
            // 读完消息记得释放，那写消息为什么不这样操作呢，因为写完消息netty自动释放。  
            // 其操作见：DefaultChannelHandlerInvoker L331-332,不过有这个注释-> promise cancelled  
            // 是不少netty5正式发布的时候会取消呢。  
            // 我们可以使用SimpleChannelInboundHandler作为父类，因为释放操作已实现。  
            ReferenceCountUtil.release(msg);  
        }  

    }  
}
