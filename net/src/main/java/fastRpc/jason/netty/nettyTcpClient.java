/**    
  * 文件名：nettyTcpClient.java    
 *    
 * 版本信息：    
 * 日期：2018年4月16日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.netty;

import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetClient;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.NetType;

/**    
 *     
 * 项目名称：net    
 * 类名称：nettyTcpClient    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年4月16日 上午2:16:29    
 * 修改人：jinyu    
 * 修改时间：2018年4月16日 上午2:16:29    
 * 修改备注：    
 * @version     
 *     
 */
@NetType("nettytcp_Client")
public class nettyTcpClient implements INetClient{
    EchoClient client=null;
    private String remoteIP="";
    private int remotePort=0;
    private  volatile boolean isRun=false;
    private volatile boolean isStop=false;
    private IRecvieHander hander=null;
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
   
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
                 JYSocket data= client.recvice();
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
        rec.setName("nettyclientrec");
        if(!rec.isAlive())
        {
            rec.start();
        }
    }
    @Override
    public void setLocalIP(String ip) {
       
        
    }

    @Override
    public void setLocalPort(int port) {
       
        
    }

    @Override
    public void setRemoteIP(String ip) {
      
        this.remoteIP=ip;
    }

    @Override
    public void setRemotePort(int port) {
       this.remotePort=port;
        
    }

    @Override
    public void setClient(Object client) {
        
        
    }

    @Override
    public boolean connect(String remoteIP, int remotePort) {
       this.remoteIP=remoteIP;
       this.remotePort=remotePort;
       connect();
        return true;
    }

    @Override
    public boolean connect() {
        client=new EchoClient(this.remoteIP,this.remotePort);
        client.start();
        return true;
    }

    @Override
    public void sendData(byte[] data) {
        client.sendData(data);
        
    }

    @Override
    public void sendData(String ip, int port, byte[] data) {
        client.sendData(data);
        
    }

    @Override
    public byte[] recviceData() {
        this.startRecvice(null);
      try {
        return  queue.take().data;
    } catch (InterruptedException e) {
    
        e.printStackTrace();
    }
    return null;
    }

    @Override
    public JYSocket recvice() {
        this.startRecvice(null);
        try {
            return queue.take();
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void startRecvice(IRecvieHander hander) {
     this.hander=hander;
     this.startThread();
        
    }

    @Override
    public void setBufferSize(int size) {
     
        
    }

    @Override
    public void close() {
     this.isStop=true;
     this.isRun=false;
     this.client.close();
        
    }
    @Override
    public boolean isClose() {
        return isStop;
    }
    @Override
    public boolean isHavRec() {
       return !queue.isEmpty();
    }
    @Override
    public boolean isConnected() {
        return isStop;
      
    }
    @Override
    public byte[] recDirect() {
        // TODO Auto-generated method stub
        return null;
    }

}
