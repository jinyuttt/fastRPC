/**
 * 
 */
package fastRpc.jason.core;

import org.msgpack.annotation.Message;

/**
 * @author jinyu
 * 方法的参数信息
 *
 */
@Message
public class RPCParameter {
	/**
	 * 参数名称 统一小写
	 */
public String  name;

/**
 *String或者是json byte[]
 */
public String  clsType;

/**
 * 参数值 json时是字符串的数组
 */
public byte[]  value;
}
