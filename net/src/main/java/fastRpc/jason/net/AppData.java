/**    
 * 文件名：AppData.java    
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
 * 类名称：AppData    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月24日 下午3:04:10    
 * 修改人：jinyu    
 * 修改时间：2018年2月24日 下午3:04:10    
 * 修改备注：    
 * @version     
 *     
 */
public class AppData {
 byte[] data=null;
public AppData(byte[] buf)
{
    this.data=buf;
}
public byte[] get()
{
    return data;
}
}
