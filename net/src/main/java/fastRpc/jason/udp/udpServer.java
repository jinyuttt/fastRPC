/**    
 * 文件名：udpServer.java    
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
import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetServer;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.BufferManager;
import fastRpc.jason.net.NetType;

/**    
 *     
 * 项目名称：net    
 * 类名称：udpServer    
 * 类描述：    udp
 * 创建人：jinyu    
 * 创建时间：2018年4月16日 上午1:44:27    
 * 修改人：jinyu    
 * 修改时间：2018年4月16日 上午1:44:27    
 * 修改备注：    
 * @version     
 *     
 */
@NetType("udp_Server")
public class UdpServer implements INetServer {
    private DatagramSocket datagramSocket = null;
    private   int localPort=0;
    private  String localIP="";
    private IRecvieHander hander=null;
    private int maxSzie=65535;
    private int recSize=128*1024;
    private byte[] buffer=null;
    private volatile boolean isRun=false;
    private volatile boolean isStop=false;
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
    
    /**
     * 开启数据接收
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
                DatagramPacket datagramPacket=null;
                if(localIP.isEmpty()||localPort==0)
                {
                    localIP=datagramSocket.getLocalAddress().getHostAddress();
                    localPort=datagramSocket.getLocalPort();
                }
                buffer=new byte[maxSzie];
                datagramPacket = new DatagramPacket(buffer, buffer.length);
                while(!isStop){   
                    try {
                        datagramSocket.receive(datagramPacket);
                        byte[] tmp=new byte[datagramPacket.getLength()];
                        System.arraycopy(buffer, 0, tmp, 0, tmp.length);
                        String from=datagramPacket.getAddress().getHostAddress()+":"+datagramPacket.getPort();
                        byte[] recbytes=BufferManager.getInstance().add(from, tmp);
                       if(recbytes!=null)
                       {
                           JYSocket jclient=new JYSocket();
                           jclient.isTcpType=true;
                           UdpClient c=new UdpClient();
                           c.setLocalIP(localIP);
                           c.setLocalPort(localPort);
                           c.setRemoteIP(datagramPacket.getAddress().getHostAddress());
                           c.setRemotePort(datagramPacket.getPort());
                           c.setClient(datagramPacket);
                           jclient.socket=c;
                           jclient.localIP=localIP;
                           jclient.localPort=localPort;
                           jclient.srvIP=datagramPacket.getAddress().getHostAddress();
                           jclient.srvPort=datagramPacket.getPort();
                           if(hander!=null)
                           {
                              hander.recviceData(recbytes);
                              hander.recviceData(jclient);
                           }
                           else
                           {
                               queue.offer(jclient);
                           }
                       }
                    } catch (IOException e) {
                       
                        e.printStackTrace();
                    }
                   
                  
                }    
                
            }
            
        });
        rec.setDaemon(true);
        rec.setName("udpserverrec");
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
        try
        {
        if(this.localIP.isEmpty())
         {
           datagramSocket=new DatagramSocket(this.localPort);
        }
        else
        {
            InetAddress addr=InetAddress.getByName(localIP);
            datagramSocket=new DatagramSocket(this.localPort,addr);
        }
        datagramSocket.setReceiveBufferSize(recSize);
        datagramSocket.setSendBufferSize(recSize);
        }
        catch(Exception ex)
        {
            ex.getStackTrace();
            return false;
        }
        //
       
        startThread();
        return true;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#start(fastRpc.jason.inet.IRecvieHander)    
     */
    @Override
    public boolean start(IRecvieHander hander) {
       this.hander=hander;
       return start();
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#recvice()    
     */
    @Override
    public JYSocket recvice() {
      try {
        return  queue.take();
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
        isRun=false;
        isStop=true;
        datagramSocket.close();

    }

}
