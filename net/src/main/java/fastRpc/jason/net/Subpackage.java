/**    
 * 文件名：Subpackage.java    
 *    
 * 版本信息：    
 * 日期：2018年2月25日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：Subpackage    
 * 类描述：     分包
 * 创建人：jinyu    
 * 创建时间：2018年2月25日 上午12:18:54    
 * 修改人：jinyu    
 * 修改时间：2018年2月25日 上午12:18:54    
 * 修改备注：    
 * @version     
 *     
 */
public class Subpackage {
  private  static  AtomicInteger packagetId=new AtomicInteger(0);
  
  /**
   * 
   * 获取一个分包ID   
     
   * @param       
     
   * @param  @return    设定文件    
     
   * @return String    DOM对象    
     
   * @Exception 异常对象
   */
  public  static int getPackagetId()
  {
      return packagetId.getAndIncrement();
  }
  public static int getCount(int len)
  {
      int num=len/NetPackagetInfo.packagetLen;
      if(len%NetPackagetInfo.packagetLen!=0)
      {
          num=num+1;
      }
      return num;
  }
    /**
     * 
     * 数据分包     
       
     * @param    data   分包
       
     * @param  @return    设定文件    
       
     * @return String    DOM对象    
       
     * @Exception 异常对象
     */
public static List<byte[]> subpackaget(byte[]data)
{
    if(data==null)
    {
        return null;
    }
    int nSeqNumber=packagetId.getAndIncrement();
    HeadExt head=new HeadExt();
    head.m_nSeqNumber=nSeqNumber;
    //
    int num=data.length/NetPackagetInfo.packagetLen;
    if(data.length%NetPackagetInfo.packagetLen!=0)
    {
        num=num+1;
    }
    head.nTotalFragment=(short) num;
    //开始拷贝
    int offset=0;
    short index=0;
    List<byte[]> list=new ArrayList<byte[]>();
    while(true)
    {
        head.m_nFragmentIndex=index;
        if(data.length-offset>=NetPackagetInfo.packagetLen)
        {
            //还够
            head.m_usPayloadSize=NetPackagetInfo.packagetLen;
            //
            byte[] packaget=new byte[NetPackagetInfo.dataLen];
            System.arraycopy(data, offset, packaget, NetPackagetInfo.headLen, packaget.length);
            System.arraycopy(head.getHeader(), 0, packaget, 0,  NetPackagetInfo.headLen);
            //
            list.add(packaget);
            offset+=NetPackagetInfo.packagetLen;
        }
        else
        {
            //还够
            head.m_usPayloadSize=(short) (data.length-offset);
            //
            byte[] packaget=new byte[head.m_usPayloadSize+NetPackagetInfo.headLen];
            System.arraycopy(data, offset, packaget, NetPackagetInfo.headLen, packaget.length);
            System.arraycopy(head.getHeader(), 0, packaget, 0,  NetPackagetInfo.headLen);
            //
            list.add(packaget);
            break;
        }
        index++;
    }
    return list; 
}

/**
 * 
 *  逐步分包  
   
 * @param       
   
 * @param  @return    设定文件    
   
 * @return String    DOM对象    
   
 * @Exception 异常对象
 */
public static byte[] subpackaget(byte[]data,short index,int id)
{
    if(data==null)
    {
        return null;
    }
    HeadExt head=new HeadExt();
    head.m_nSeqNumber=id;
    head.m_nFragmentIndex=index;
    //
    
    int num=data.length/NetPackagetInfo.packagetLen;
    if(data.length%NetPackagetInfo.packagetLen!=0)
    {
        num=num+1;
    }
    head.nTotalFragment=(short) num;
    //
    int len=(index+1)*NetPackagetInfo.packagetLen;
    if(len>data.length)
      {
            //不够
             int lenf=data.length-(index*NetPackagetInfo.packagetLen);
            head.m_usPayloadSize=(short) lenf;
            //
            if( head.m_usPayloadSize<0)
            {
                return null;
            }
            byte[] packaget=new byte[head.m_usPayloadSize+NetPackagetInfo.headLen];
            System.arraycopy(data, index*NetPackagetInfo.packagetLen, packaget, NetPackagetInfo.headLen, head.m_usPayloadSize);
            System.arraycopy(head.getHeader(), 0, packaget, 0,  NetPackagetInfo.headLen);
            return packaget;
      }
        else
        {
            //还够
            head.m_usPayloadSize=NetPackagetInfo.packagetLen;
            //
            byte[] packaget=new byte[NetPackagetInfo.dataLen];
            System.arraycopy(data, index*NetPackagetInfo.packagetLen, packaget, NetPackagetInfo.headLen,NetPackagetInfo.packagetLen);
            System.arraycopy(head.getHeader(), 0, packaget, 0,  NetPackagetInfo.headLen);
            //
           return packaget;
        }
    

}
}
