/**    
 * 文件名：RecviceBuffer.java    
 *    
 * 版本信息：    
 * 日期：2018年2月24日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;

import java.util.concurrent.ConcurrentHashMap;

/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：RecviceBuffer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月24日 下午2:31:47    
 * 修改人：jinyu    
 * 修改时间：2018年2月24日 下午2:31:47    
 * 修改备注：    
 * @version     
 *     
 */
public class RecviceBuffer {
    /*
     * 按照IP+端口
     */
   private ConcurrentHashMap<String,FromAddrBuffer> hash=new  ConcurrentHashMap<String,FromAddrBuffer>();
    public RecviceBuffer()
    {
      
    }
    public byte[] add(String from,byte[]data)
    {
        FromAddrBuffer buf=  hash.getOrDefault(from, null);
        if(buf==null)
        {
            buf=new FromAddrBuffer();
            hash.put(from, buf);
        }
        PacketIn pack=new PacketIn(data,0,data.length);
        return buf.add(pack);
      
    }

}
