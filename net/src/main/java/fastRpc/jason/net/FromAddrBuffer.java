/**    
 * 文件名：FromAddrBuffer.java    
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
 * 类名称：FromAddrBuffer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月25日 上午1:28:13    
 * 修改人：jinyu    
 * 修改时间：2018年2月25日 上午1:28:13    
 * 修改备注：    
 * @version     
 *     
 */
public class FromAddrBuffer {
    /**
     * 按照序列
     */
   private ConcurrentHashMap<String,Packet> hash=new  ConcurrentHashMap<String,Packet>();
   public byte[] add(PacketIn pack)
   {
      String id=String.valueOf(pack.head.m_nSeqNumber);
      Packet p=hash.getOrDefault(id, null);
      if(p==null)
      {
          p=new Packet(pack.head.nTotalFragment);
          hash.put(id, p);
      }
      if(p.InsertFragment(pack))
      {
          if(p.IsFragComplete())
          {
              hash.remove(id);
              return p.getBuffer();
          }
      }
     return null;
   }
   public boolean check()
   {
      return hash.isEmpty();
   }
}
