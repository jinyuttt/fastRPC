/**    
 * 文件名：TcpServer.java    
 *    
 * 版本信息：    
 * 日期：2018年4月15日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.INetServer;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;

/**    
 *     
 * 项目名称：net    
 * 类名称：TcpServer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年4月15日 下午4:32:16    
 * 修改人：jinyu    
 * 修改时间：2018年4月15日 下午4:32:16    
 * 修改备注：    
 * @version     
 *     
 */
public class TcpServer implements INetServer {

    private String localIP="";
    private int localPort=0;
    private ServerSocket serverSocket = null;
    private IRecvieHander hander=null;
    private  boolean isStop=false;
    private  volatile boolean isRun=false;
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
    public TcpServer()
    {
        try {
            serverSocket=new ServerSocket();
        } catch (IOException e) {
           
            e.printStackTrace();
        }
    }
    private void startThread()
    {
        if(isRun)
        {
            return;
        }
        isRun=true;
        Thread server=new Thread(new Runnable() {

            @Override
            public void run() {
              while(!isStop)
              {
                 try {
                    Socket  client= serverSocket.accept();
                    tcpClient curClient=new tcpClient();
                    curClient.setClient(curClient);
                    JYSocket socket=new JYSocket();
                    if(localIP.isEmpty()||localPort==0)
                    {
                        localIP=serverSocket.getLocalSocketAddress().toString();
                        localPort=serverSocket.getLocalPort();
                    }
                    socket.localIP=localIP;
                    socket.localPort=localPort;
                    socket.srvIP=client.getInetAddress().getHostAddress();
                    socket.srvPort=client.getPort();
                    socket.socket=curClient;
                    if(hander!=null)
                    {
                       
                        hander.recviceData(socket);
                    }
                    else
                    {
                        queue.offer(socket);
                    }
                } catch (IOException e) {
                 
                    e.printStackTrace();
                }
              }
                
            }
            
        });
        server.setDaemon(true);
        server.setName("tcpserver");
        if(!server.isAlive())
        {
          server.start();
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
        return queue.take();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return null;
    }
    @Override
    public void close() {
       this.isStop=true;
       this.isRun=false;
       try {
        this.serverSocket.close();
    } catch (IOException e) {
        
        e.printStackTrace();
    }
        
    }

}
