/**    
 * 文件名：PacketIn.java    
 *    
 * 版本信息：    
 * 日期：2018年2月24日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;

/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：PacketIn    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月24日 下午3:23:51    
 * 修改人：jinyu    
 * 修改时间：2018年2月24日 下午3:23:51    
 * 修改备注：    
 * @version     
 *     
 */
public class PacketIn {
    private   short  usHeaderSize=NetPackagetInfo.headLen;
    HeadExt head; 
    private  byte[] m_lpszBuffer;
    /**
     * 数据大小
     */
    int   m_nDataLen;
    
    /**
     * 最大数据大小
     */
    int   m_usBufferSize;
    
  private  byte[] dataBuffer=null;
    
    /**
     * 接受的数据包
     * 创建一个新的实例 PacketIn.    
     *    
     * @param lpszBuffer  数据包
     * @param usBufferSize  数据包最大大小
     * @param nDataSize   数据大小
     */
    public  PacketIn(byte[] lpszBuffer, int usBufferSize, int nDataSize)
    {
        m_lpszBuffer=lpszBuffer;
        m_usBufferSize = usBufferSize; //数据最大长度，其实没什么用，已经设置了这个值最大为64000
        m_nDataLen = nDataSize;
    }
    
    /**
     * 
     * 验证数据  
        
     * @param  @return    设定文件    
       
     * @return String    DOM对象    
       
     * @Exception 异常对象
     */
  public  boolean  Normalize() {
               if (m_lpszBuffer!=null&&m_usBufferSize < usHeaderSize )//没什么用
                {
                      return  false;
                }
                 HeadExt  pHeader = new HeadExt(m_lpszBuffer);
                if (pHeader.m_usPayloadSize != m_nDataLen - usHeaderSize )
                    {
                       return  false;
                    }
                    head =  pHeader;
                    if (pHeader.m_usPayloadSize > 0 )
                     {
                        moveHeader();
                     }
                          
                   return true;
         }
  
/**
 * 
 * 移除头
   
 * @param   m_lpszBuffer 数据    
   
 * @param  @return    设定文件    
   
 * @return String    DOM对象    
   
 * @Exception 异常对象
 */
private void moveHeader() {
    dataBuffer=new byte[head.m_usPayloadSize];
    System.arraycopy(m_lpszBuffer, usHeaderSize, dataBuffer, 0, head.m_usPayloadSize);
    m_nDataLen=m_nDataLen-usHeaderSize;
}

/**
 * 
 * 返回有效数据
   
 * @param       
   
 * @param  @return    设定文件    
   
 * @return String    DOM对象    
   
 * @Exception 异常对象
 */
public byte[] getBuffer()
{
    return dataBuffer;
}
}
