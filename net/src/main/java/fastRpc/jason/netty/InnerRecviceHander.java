/**    
 * 文件名：InnerRecviceHander.java    
 *    
 * 版本信息：    
 * 日期：2018年4月16日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.netty;

import java.util.concurrent.LinkedBlockingQueue;

import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;

/**    
 *     
 * 项目名称：net    
 * 类名称：InnerRecviceHander    
 * 类描述：   接收数据
 * 创建人：jinyu    
 * 创建时间：2018年4月16日 上午2:30:54    
 * 修改人：jinyu    
 * 修改时间：2018年4月16日 上午2:30:54    
 * 修改备注：    
 * @version     
 *     
 */
public class InnerRecviceHander implements IRecvieHander {
  
    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();
    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.IRecvieHander#recviceData(byte[])    
     */
    @Override
    public void recviceData(byte[] data) {
     
    }

    /* (non-Javadoc)    
     * @see fastRpc.jason.inet.IRecvieHander#recviceData(fastRpc.jason.inet.JYSocket)    
     */
    @Override
    public void recviceData(JYSocket data) {
      
        queue.offer(data);
    }
    
    /**
     * 获取接收的数据
     * @return
     */
    public JYSocket getData()
    {
      try {
          return queue.take();
      } catch (InterruptedException e) {
    
        e.printStackTrace();
     }
     return null;
    }
}
