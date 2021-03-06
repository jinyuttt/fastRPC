/**    
 * 文件名：JYData.java    
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
 * 类名称：JYData    
 * 类描述：    接收的数据格式
 * 创建人：jinyu    
 * 创建时间：2018年4月15日 上午1:39:04    
 * 修改人：jinyu    
 * 修改时间：2018年4月15日 上午1:39:04    
 * 修改备注：    
 * @version     
 *     
 */
public class JYSocket {
 /**
  * 远端IP
  */
public String  srvIP;

/**
 * 远端端口
 */
public int srvPort;

/**
 * 本地Ip
 */
public String localIP;

/**
 * 本地端口
 */
public int localPort;

/**
 * 数据
 */
public byte[]data;

/**
 * 通信端口
 */
public INetClient socket=null;

public boolean isTcpType;
}
