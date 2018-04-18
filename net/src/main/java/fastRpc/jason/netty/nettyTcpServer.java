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
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.INetServer#close()    
     */
    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

}
