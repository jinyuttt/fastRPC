/**    
 * 文件名：ServerExample.java    
 *    
 * 版本信息：    
 * 日期：2018年4月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Server;

import fastRpc.jason.core.RPCMap;

/**    
 *     
 * 项目名称：Server    
 * 类名称：ServerExample    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年4月19日 下午8:17:48    
 * 修改人：jinyu    
 * 修改时间：2018年4月19日 下午8:17:48    
 * 修改备注：    
 * @version     
 *     
 */
public class ServerExample {
    @RPCMap(name="getData")
public String getData(String ss)
{
    return ss+"jinyu";
    
}
}
