/**    
 * 文件名：TcpClient.java    
 *    
 * 版本信息：    
 * 日期：2018年4月15日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetClient;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.NetType;

/**    
 *     
 * 项目名称：net    
 * 类名称：TcpClient    
 * 类描述：    tcp客户端
 * 创建人：jinyu    
 * 创建时间：2018年4月15日 下午1:33:13    
 * 修改人：jinyu    
 * 修改时间：2018年4月15日 下午1:33:13    
 * 修改备注：    
 * @version     
 *     
 */
@NetType("tcp_Client")
public class TcpClient implements INetClient {
    private  Socket client=null;
    private IRecvieHander hander=null;
    private  volatile boolean isRun=false;
    private String localIP="";
    private int localPort=0;
    private String remoteIP="";
    private int remotePort=0;
    private int recSize=128*1024;
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
    private volatile boolean isStop=false;
    private TcpClient  instance=null;
    public TcpClient()
    {
        client=new Socket();
        try {
            client.setSendBufferSize(recSize);
            client.setReceiveBufferSize(recSize);
        } catch (SocketException e) {
           
            e.printStackTrace();
        }
        instance=this;
    }
    
    /**
     * 绑定本地IP端口
     */
    private   void bind()
    {
        if(!localIP.isEmpty()||localPort!=0)
        {
            InetSocketAddress bindpoint=null;
            if(localIP.isEmpty())
            {
                bindpoint = new InetSocketAddress(localPort);
            }
            else
            {
               bindpoint = new InetSocketAddress(localIP, localPort);
            }
            try {
                client.bind(bindpoint);
            } catch (IOException e) {
            
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 开启线程接收数据
     */
    private void  startRec()
    {
        if(isRun)
        {
            return;
        }
        isRun=true;
        
        Thread rec=new Thread(new Runnable() {

            @Override
            public void run() {
                byte[] buffer=new byte[recSize];
                int errorNum=10;
                while(!isStop)
                {
                  try {
                      if(client.isInputShutdown())
                      {
                          isStop=true;
                      }
                     int r= 0;
                     try
                     {
                         r=client.getInputStream().read(buffer);
                     }
                     catch(SocketException ex)
                     {
                         errorNum--;
                         if(client.isClosed()||client.isInputShutdown())
                         {
                             break;
                         }
                         if(errorNum==0)
                         {
                             client.shutdownInput();
                             
                         }
                         continue;
                     }
                     errorNum=10;
                     JYSocket data=new JYSocket();
                     byte[] tmp=new byte[r];
                     System.arraycopy(buffer, 0, tmp, 0, r);
                     data.data=tmp;
                     if(localIP.isEmpty()||localPort==0)
                     {
                          localIP=client.getLocalAddress().getHostAddress();
                         localPort=client.getLocalPort();
                     }
                     data.localIP=localIP;
                     data.localPort=localPort;
                     data.srvIP=remoteIP;
                     data.srvPort=remotePort;
                     data.socket=instance;
                     if(hander!=null)
                     {
                         hander.recviceData(tmp);
                         hander.recviceData(data);
                     }
                     else
                     {
                         queue.offer(data);
                     }
                     
                } 
                  
                  catch (IOException e) {
                    
                    e.printStackTrace();
                }
                }
            }
            
        });
        rec.setDaemon(true);
        rec.setName("clietrec");
        if(!rec.isAlive())
        {
           rec.start();
        }
    }
    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setLocalIP(java.lang.String)    
     */
    @Override
    public void setLocalIP(String ip) {
      this.localIP=ip;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setLocalPort(int)    
     */
    @Override
    public void setLocalPort(int port) {
      this.localPort=port;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setRemoteIP(java.lang.String)    
     */
    @Override
    public void setRemoteIP(String ip) {
       this.remoteIP=ip;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setRemotePort(int)    
     */
    @Override
    public void setRemotePort(int port) {
      this.remotePort=port;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#connect(java.lang.String, int)    
     */
    @Override
    public boolean connect(String remoteIP, int remotePort) {
        this.remoteIP=remoteIP;
        this.remotePort=remotePort;
        //
         bind();
         SocketAddress endpoint=new InetSocketAddress(remoteIP,remotePort);
         try {
            client.connect(endpoint);
            return true;
        } catch (IOException e) {
         
            e.printStackTrace();
            return false;
        }
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#connect()    
     */
    @Override
    public boolean connect() {
        bind();
        SocketAddress endpoint=new InetSocketAddress(remoteIP,remotePort);
        try {
           client.connect(endpoint);
           return true;
       } catch (IOException e) {
        
           e.printStackTrace();
           return false;
       }
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#sendData(byte[])    
     */
    @Override
    public void sendData(byte[] data) {
       try {
           if(!client.isClosed())
           {
              client.getOutputStream().write(data);
           }
    } catch (IOException e) {
        try {
            client.close();
        } catch (IOException e1) {
          
            e1.printStackTrace();
        }
        e.printStackTrace();
    }
   
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#sendData(java.lang.String, int, byte[])    
     */
    @Override
    public void sendData(String ip, int port, byte[] data) {
         this.sendData(data);
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#recviceData()    
     */
    @Override
    public byte[] recviceData() {
        startRec();
      try {
        return  queue.take().data;
    } catch (InterruptedException e) {
      
        e.printStackTrace();
    }
    return null;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#recvice()    
     */
    @Override
    public JYSocket recvice() {
        startRec();
        try {
          
         return  queue.take();
        } catch (InterruptedException e) {
          
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#startRecvice(fastRpc.jason.inet.IRecvieHander)    
     */
    @Override
    public void startRecvice(IRecvieHander hander) {
        this.hander=hander;
        startRec();
    }
    @Override
    public void setBufferSize(int size) {
       this.recSize=size;
        
    }
    @Override
    public void setClient(Object client) {
       try {
        this.client.close();
    } catch (IOException e) {
      
    }
       this.client=null;
       this.client=(Socket) client;
        
    }
    @Override
    public void close() {
       this.isStop=true;
       try {
        this.client.close();
    } catch (IOException e) {
      
        e.printStackTrace();
    }
       this.isRun=false;
       this.queue.clear();
        
    }

    @Override
    public boolean isClose() {
      return client.isClosed();
    
    }
    
    @Override
    public boolean isHavRec() {
        return  !queue.isEmpty();
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public byte[] recDirect() {
        byte[] buffer=new byte[recSize];
        int r= 0;
            try {
                r=client.getInputStream().read(buffer);
            } catch (IOException e) {
                try {
                    client.close();
                } catch (IOException e1) {
                  
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
       // JYSocket data=new JYSocket();
        byte[] tmp=new byte[r];
        System.arraycopy(buffer, 0, tmp, 0, r);
        return tmp;
    }

}
