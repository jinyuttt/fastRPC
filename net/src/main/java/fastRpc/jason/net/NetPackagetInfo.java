/**    
 * 文件名：NetPackagetInfo.java    
 *    
 * 版本信息：    
 * 日期：2018年2月25日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.net;

/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：NetPackagetInfo    
 * 类描述：    网络包信息
 * 创建人：jinyu    
 * 创建时间：2018年2月25日 上午12:20:00    
 * 修改人：jinyu    
 * 修改时间：2018年2月25日 上午12:20:00    
 * 修改备注：    
 * @version     
 *     
 */
public class NetPackagetInfo {
    
    /**
     * 定义的头长度
     */
public static  short  headLen=16;

/**
 * 定义网络包大小
 * 含头总长
 */
public static short dataLen=1472;//65535

/**
 * 数据大小
 * 不能超过short大小
 */
public static short packagetLen=1456;//dataLen-headLen

public static short packOutTime=30;
}
