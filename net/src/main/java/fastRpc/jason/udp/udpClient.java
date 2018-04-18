/**    
 * 文件名：udpClient.java    
 *    
 * 版本信息：    
 * 日期：2018年4月16日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetClient;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.Subpackage;

/**    
 *     
 * 项目名称：net    
 * 类名称：udpClient    
 * 类描述：    udp
 * 创建人：jinyu    
 * 创建时间：2018年4月16日 上午1:08:22    
 * 修改人：jinyu    
 * 修改时间：2018年4月16日 上午1:08:22    
 * 修改备注：    
 * @version     
 *     
 */
public class udpClient implements INetClient {
    private  DatagramSocket client = null;
    private String srcIP="";
    private int  srcPort=0;
    private String localIP="";
    private int localPort=0;
    private IRecvieHander hander=null;
    private  volatile boolean isRun=false;
    private  volatile boolean isStop=false; 
    private int recSize=128*1024;
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
    public udpClient()
    {
        try {
            client=new DatagramSocket();
            client.setSendBufferSize(recSize);
            client.setSendBufferSize(recSize);
        } catch (SocketException e) {
         
            e.printStackTrace();
        }
    }
    /**
     * 绑定本地IP端口
     */
    private  void bind()
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
                DatagramPacket p=new DatagramPacket(buffer, buffer.length);
                while(!isStop)
                {
                  try {
                  
                    client.receive(p);
                    JYSocket data=new JYSocket();
                    byte[] tmp=new byte[p.getLength()];
                    System.arraycopy(p.getData(), 0, tmp, 0, p.getLength());
                     data.data=tmp;
                     if(localIP.isEmpty()||localPort==0)
                     {
                          localIP=client.getLocalAddress().getHostAddress();
                         localPort=client.getLocalPort();
                     }
                     data.localIP=localIP;
                     data.localPort=localPort;
                     data.srvIP=p.getAddress().getHostAddress();
                     data.srvPort=p.getPort();
                     if(hander!=null)
                     {
                         hander.recviceData(p.getData());
                         hander.recviceData(data);
                     }
                     else
                     {
                         queue.offer(data);
                     }
                     
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
                }
            }
            
        });
        rec.setDaemon(true);
        rec.setName("udpClintRec");
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
        this.srcIP=ip;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setRemotePort(int)    
     */
    @Override
    public void setRemotePort(int port) {
       this.srcPort=port;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setClient(java.lang.Object)    
     */
    @Override
    public void setClient(Object client) {
      this.client=(DatagramSocket) client;

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#connect(java.lang.String, int)    
     */
    @Override
    public boolean connect(String remoteIP, int remotePort) {
        bind();
        return true;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#connect()    
     */
    @Override
    public boolean connect() {
        bind();
        return true;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#sendData(byte[])    
     */
    @Override
    public void sendData(byte[] data) {
        this.sendData(this.srcIP, this.srcPort, data);
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#sendData(java.lang.String, int, byte[])    
     */
    @Override
    public void sendData(String ip, int port, byte[] data) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e1) {
        
            e1.printStackTrace();
        }
        short index=0;
        int id=Subpackage.getPackagetId();
       byte[]tmp= null;
            do
            {
               tmp=Subpackage.subpackaget(data, index, id);
               if(tmp!=null)
               {
                   DatagramPacket sendPacket = new DatagramPacket(tmp, tmp.length, addr,port);
                   try {
                      client.send(sendPacket);
                  } catch (IOException e) {
                  
                      e.printStackTrace();
                  }
                   index++;
               }
            }while(tmp!=null);

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#recviceData()    
     */
    @Override
    public byte[] recviceData() {
        this.startRec();
        try {
            return queue.take().data;
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
        this.startRec();
        try {
            return queue.take();
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
       this.startRec();

    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#setBufferSize(int)    
     */
    @Override
    public void setBufferSize(int size) {
       this.recSize=size;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetClient#close()    
     */
    @Override
    public void close() {
      this.isStop=true;
      this.isRun=false;
      this.queue.clear();
      this.client.close();

    }

}
