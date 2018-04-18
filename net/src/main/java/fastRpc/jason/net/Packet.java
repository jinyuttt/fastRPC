/**    
 * 文件名：Packet.java    
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
 * 类名称：Packet    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月24日 下午3:26:22    
 * 修改人：jinyu    
 * 修改时间：2018年2月24日 下午3:26:22    
 * 修改备注：    
 * @version     
 *     
 */
public class Packet {
        private  byte[]  m_pBuffer=null;
        private AppData[] buffer=null;
        private volatile int size=0;
        private int recBytesNum=0;
        public Packet(int nTotalFragment)
        {
            buffer=new AppData[nTotalFragment];
        }
         public   void reset() {
                
                  m_pBuffer =  null;
                
            }
         public    void Set(int nSeqNumber ) {  
        }
         
         /**
          * 
          * TODO(这里描述这个方法适用条件 – 可选)     
            
          * @param     pFragment  数据包  
            
          * @param  @return    设定文件    
            
          * @return String    DOM对象    
            
          * @Exception 异常对象
          */
         public  boolean  InsertFragment(PacketIn pFragment) {
             if(buffer[pFragment.head.m_nFragmentIndex]==null)
             {
                 buffer[pFragment.head.m_nFragmentIndex]=new AppData(pFragment.getBuffer());
                 size++;
                 recBytesNum+=pFragment.m_nDataLen;
                 return true;
             }
             return false;
            }
         public   boolean IsFragComplete() {
                if(size==buffer.length)
                {
                    return true;
                }
                return false; 
        }
         public byte[] getBuffer()
         {
             m_pBuffer=new byte[recBytesNum];
             int offset=0;
             for(int i=0;i<size;i++)
             {
                System.arraycopy( buffer[i].data, 0, m_pBuffer, offset, buffer[i].data.length);
                offset+=buffer[i].data.length;
             }
             return m_pBuffer;
         }
           
}
