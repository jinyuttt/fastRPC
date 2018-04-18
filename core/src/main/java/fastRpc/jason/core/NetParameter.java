/**    
 * 文件名：NetParameter.java    
 *    
 * 版本信息：    
 * 日期：2018年3月8日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.util.List;

import org.msgpack.annotation.Message;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：NetParameter    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月8日 下午10:12:15    
 * 修改人：jinyu    
 * 修改时间：2018年3月8日 下午10:12:15    
 * 修改备注：    
 * @version     
 *     
 */
@Message
public class NetParameter {
public List<RPCParameter> list=null;
}
