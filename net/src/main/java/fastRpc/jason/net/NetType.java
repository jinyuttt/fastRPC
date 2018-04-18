/**    
 * 文件名：NetType.java    
 *    
 * 版本信息：    
 * 日期：2018年3月3日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：NetType    
 * 类描述：    通信注解
 * 服务段 名称_Server
 * 客户端 名称_Client
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 下午1:09:51    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 下午1:09:51    
 * 修改备注：    
 * @version     
 *     
 */
public @interface NetType {
public String value()default "";
}
