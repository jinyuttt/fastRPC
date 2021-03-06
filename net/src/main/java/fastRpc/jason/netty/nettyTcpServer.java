/**    
 * 文件名：nettyTcpServer.java    
 *    
 * 版本信息：    
 * 日期：2018年4月17日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.netty;

import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetServer;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.NetType;

/**    
 *     
 * 项目名称：net    
 * 类名称：nettyTcpServer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年4月17日 上午1:33:09    
 * 修改人：jinyu    
 * 修改时间：2018年4月17日 上午1:33:09    
 * 修改备注：    
 * @version     
 *     
 */
@NetType("nettytcp_Server")
public class nettyTcpServer implements INetServer {
   private  EchoServer  server=null;
   private String localIP="";
   private int localPort=0;
   private IRecvieHander hander=null;
   private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
   private volatile boolean isRun=false;
   private volatile boolean isStop=false;
   
   /**
    * 开启线程
    */
   private void startThread()
   {
       if(isRun)
       {
           return;
           
       }
       isRun=true;
       Thread rec=new Thread(new Runnable() {

        @Override
        public void run() {
            while(!isStop)
            {
            JYSocket data=  server.recvice();
            if(hander!=null)
            {
                hander.recviceData(data.data);
                hander.recviceData(data);
            }
            else
            {
                queue.offer(data);
            }
           }
        }
           
       });
       rec.setDaemon(true);
       rec.setName("netty_Server");
       if(!rec.isAlive())
       {
       rec.start();
       }
   }
    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#setLocalIP(java.lang.String)    
     */
    @Override
    public void setLocalIP(String ip) {
      this.localIP=ip;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#setLocalPort(int)    
     */
    @Override
    public void setLocalPort(int port) {
       this.localPort=port;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#start()    
     */
    @Override
    public boolean start() {
          server=new EchoServer(this.localIP,this.localPort);
          try {
            server.run();
            startThread();
        } catch (Exception e) {
           
            e.printStackTrace();
        }
        return true;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#start(fastRpc.jason.inet.IRecvieHander)    
     */
    @Override
    public boolean start(IRecvieHander hander) {
      this.hander=hander;
      start();
        return false;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#recvice()    
     */
    @Override
    public JYSocket recvice() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#close()    
     */
    @Override
    public void close() {
       isStop=true;
       isRun=false;
               

    }

}
