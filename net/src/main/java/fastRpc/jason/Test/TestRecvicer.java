/**    
 * 文件名：TestRecvicer.java    
 *    
 * 版本信息：    
 * 日期：2018年5月20日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Test;

import java.io.UnsupportedEncodingException;

import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;

/**    
 *     
 * 项目名称：net    
 * 类名称：TestRecvicer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年5月20日 上午1:41:12    
 * 修改人：jinyu    
 * 修改时间：2018年5月20日 上午1:41:12    
 * 修改备注：    
 * @version     
 *     
 */
public class TestRecvicer implements IRecvieHander{

    @Override
    public void recviceData(byte[] data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void recviceData(JYSocket data) {
         byte[] ss= data.data;
         String msg=new String(ss);
         System.out.println(msg);
          try {
         data.socket.sendData(("收到："+msg).getBytes("utf-8"));
       } catch (UnsupportedEncodingException e) {
              e.printStackTrace();
        
    }
    }

}
