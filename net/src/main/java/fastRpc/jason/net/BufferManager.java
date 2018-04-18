/**    
 * 文件名：BufferManager.java    
 *    
 * 版本信息：    
 * 日期：2018年2月25日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;

import java.util.concurrent.ConcurrentHashMap;

/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：BufferManager    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月25日 下午10:35:38    
 * 修改人：jinyu    
 * 修改时间：2018年2月25日 下午10:35:38    
 * 修改备注：    
 * @version     
 *     
 */
public class BufferManager {
    private static   BufferManager instance=null;
    private ConcurrentHashMap<String,FromAddrBuffer> hash=new  ConcurrentHashMap<String,FromAddrBuffer>();
public static BufferManager getInstance()
{
    if(instance==null)
    {
        synchronized(BufferManager.class)
        {
            if(instance==null) {
                 instance=new BufferManager();
            }
        }
    }
    return instance;
}
public byte[] add(String from,byte[]data)
{
    FromAddrBuffer buf=  hash.getOrDefault(from, null);
    if(buf==null)
    {
        buf=new FromAddrBuffer();
        hash.put(from, buf);
    }
    PacketIn pack=new PacketIn(data,6400,data.length);
    pack.Normalize();
    byte[] recBytes= buf.add(pack);
    if(recBytes!=null)
    {
        if(buf.check())
        {
            hash.remove(from);
        }
    }
    return recBytes;
  
}
}
