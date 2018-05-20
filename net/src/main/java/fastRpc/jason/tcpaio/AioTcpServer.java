/**
 * 
 */
package fastRpc.jason.tcpaio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetServer;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.NetType;

/**
 * @author jinyu
 *
 */
@NetType("aiotcp_Server")
public class AioTcpServer implements INetServer{
	private AsynchronousServerSocketChannel listener;
	private Thread listenerThread=null;
    private   int localPort=0;
    private  String localIP="";
    private IRecvieHander hander=null;
    private int recSize=128*1024;
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
    private AioTcpServer curObj=null;
    public void addClientSocket(JYSocket socket)
    {
        queue.offer(socket);
    }
     public IRecvieHander getHander()
     {
       return hander;
     }
@Override
public void setLocalIP(String ip) {
   this.localIP=ip;
   
    
}
@Override
public void setLocalPort(int port) {
   this.localPort=port;
    
}
@Override
public boolean start() {
    //
  //设置线程数为CPU核数  
    try
    {
        InetSocketAddress point=null;
        if(this.localIP!=null&&!this.localIP.trim().isEmpty())
        {
            point=new InetSocketAddress(this.localIP,this.localPort);
        }
        else
        {
            point=new InetSocketAddress(this.localPort);
        }
    AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());  
    listener = AsynchronousServerSocketChannel.open(channelGroup).bind(point); 
    //重用端口  
    listener.setOption(StandardSocketOptions.SO_REUSEADDR, true); 
    listener.setOption(StandardSocketOptions.SO_RCVBUF, recSize);
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
        return false;
    }
    curObj=this;
    //
    listenerThread=new Thread(new Runnable() {
        public void run() { 
             try { 
               //为服务端socket指定接收操作对象.accept原型是：  
                 //accept(A attachment, CompletionHandler<AsynchronousSocketChannel,  
                 // ? super A> handler)  
                 //也就是这里的CompletionHandler的A型参数是实际调用accept方法的第一个参数  
                 //即是listener。另一个参数V，就是原型中的客户端socket  
              AioAcceptHandler acceptHandler = new AioAcceptHandler(curObj);
             //处理第一个链接
                listener.accept(listener, acceptHandler);
                 //listener.accept(null, acceptHandler);
              Thread.sleep(400000); 
             } catch (Exception e) { 
                 e.printStackTrace(); 
             } finally { 
              System.out.println("finished server");
             } 
         } 
        
    });
    listenerThread.setDaemon(true);
    listenerThread.setName("AioTcpServer");
    if(!listenerThread.isAlive())
    {
        listenerThread.start();
    }
    return true;
}
@Override
public boolean start(IRecvieHander hander) {
   this.hander=hander;
    return start();
}
@Override
public JYSocket recvice() {
    try {
        JYSocket socket=  queue.take();
        return socket;
    } catch (InterruptedException e) {
     
        e.printStackTrace();
    }
    return null;
}
@Override
public void close() {
    try {
       
        listener.close();
    } catch (IOException e) {
        
        e.printStackTrace();
    }
    
}
   
}
