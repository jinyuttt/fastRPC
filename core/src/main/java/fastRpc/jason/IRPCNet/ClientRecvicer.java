/**    
 * 文件名：ClientRecvicer.java    
 *    
 * 版本信息：    
 * 日期：2018年5月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.IRPCNet;

import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;

/**    
 *     
 * 项目名称：core    
 * 类名称：ClientRecvicer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年5月19日 下午5:48:20    
 * 修改人：jinyu    
 * 修改时间：2018年5月19日 下午5:48:20    
 * 修改备注：    
 * @version     
 *     
 */
public class ClientRecvicer implements IRecvieHander{
    RPCRecvicer recvicer=null;
    @Override
    public void recviceData(byte[] data) {
    }

    @Override
    public void recviceData(JYSocket data) {
        //接收到了数据
        //data.data=data.socket.recviceData();
        recvicer.recviceData(data);
    }

}
