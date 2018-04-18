/**    
 * 文件名：HeadExt.java    
 *    
 * 版本信息：    
 * 日期：2018年2月24日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;

import java.nio.ByteBuffer;

/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：HeadExt    
 * 类描述：     16字节头
 * 创建人：jinyu    
 * 创建时间：2018年2月24日 下午3:21:47    
 * 修改人：jinyu    
 * 修改时间：2018年2月24日 下午3:21:47    
 * 修改备注：    
 * @version     
 *     
 */
public class HeadExt {
    //每帧所有分片公用信息
    byte[]  flag="RPC".getBytes(); //可以任意字符或数字或其他方法，用来标志我们发送的数据
    public  int   m_nSeqNumber=-1; //本帧序数  4
    short nTotalFragment=-1;//本帧数据可分成的总片数 2
   // short  m_nTotalSize;  //本帧数据总长度  2
    //每片各自信息
    short  m_nFragmentIndex=-1; //每帧分片序数,0 1 2  ... ,\
    short  m_usPayloadSize=-1; //本片数据长度
   // short  m_usFragOffset;//本片数据相对总数据的偏移量
  //  short  m_bLastFragment;//是否最后一帧
    public HeadExt(byte[] m_lpszBuffer) {
      
        if(m_lpszBuffer!=null)
        {
            ByteBuffer buf=ByteBuffer.wrap(m_lpszBuffer);
            buf.get(flag);
            m_nSeqNumber= buf.getInt();
            nTotalFragment=buf.getShort();
            m_nFragmentIndex=buf.getShort();
            m_usPayloadSize=buf.getShort();
        } 
    }
            public HeadExt() {
        
    }
     public byte[] getHeader()
     {
         byte[] head=new byte[NetPackagetInfo.headLen];
         ByteBuffer buf=ByteBuffer.wrap(head);
         buf.put(flag);
         buf.putInt(m_nSeqNumber);
         buf.putShort(nTotalFragment);
         buf.putShort(m_nFragmentIndex);
         buf.putShort(m_usPayloadSize);
         
         return head;
     }
          
}
