/**    
 * 文件名：Server.java    
 *    
 * 版本信息：    
 * 日期：2018年5月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Test;

import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.tcpaio.AioTcpServer;

/**    
 *     
 * 项目名称：net    
 * 类名称：Server    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年5月19日 下午7:40:50    
 * 修改人：jinyu    
 * 修改时间：2018年5月19日 下午7:40:50    
 * 修改备注：    
 * @version     
 *     
 */
public class Server {

    /**
     * @param args
     */
    public static void main(String[] args) {
        AioTcpServer ss=new AioTcpServer();
        ss.setLocalPort(5555);
        ss.start();
        while(true)
        {
           JYSocket c= ss.recvice();
           IRecvieHander hander=new TestRecvicer();
           c.socket.startRecvice(hander);
        }
    }

}
