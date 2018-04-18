/**    
 * 文件名：IRecvieHander.java    
 *    
 * 版本信息：    
 * 日期：2018年4月15日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.inet;

/**    
 *     
 * 项目名称：net    
 * 类名称：IRecvieHander    
 * 类描述：    回调接收数据
 * 创建人：jinyu    
 * 创建时间：2018年4月15日 上午1:34:00    
 * 修改人：jinyu    
 * 修改时间：2018年4月15日 上午1:34:00    
 * 修改备注：    
 * @version     
 *     
 */
public interface IRecvieHander {
public void  recviceData(byte[]data);
public void  recviceData(JYSocket data);
}
