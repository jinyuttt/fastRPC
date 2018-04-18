/**    
 * 文件名：Parameter.java    
 *    
 * 版本信息：    
 * 日期：2018年3月9日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：Parameter    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月9日 上午12:51:48    
 * 修改人：jinyu    
 * 修改时间：2018年3月9日 上午12:51:48    
 * 修改备注：    
 * @version     
 *     
 */
public @interface Parameter {
    String value(); 
}
