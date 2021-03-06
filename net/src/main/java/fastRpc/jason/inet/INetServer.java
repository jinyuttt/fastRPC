/**    
 * 文件名：IServer.java    
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
 * 类名称：IServer    
 * 类描述：    通信服务端接口
 * 创建人：jinyu    
 * 创建时间：2018年4月15日 上午2:23:33    
 * 修改人：jinyu    
 * 修改时间：2018年4月15日 上午2:23:33    
 * 修改备注：    
 * @version     
 *     
 */
public interface INetServer {
    
    /**
     * 
     * @param ip
     */
public void setLocalIP(String ip);

/**
 * 设置端口
 * @param port
 */
public void setLocalPort(int port);

/**
 * 开启监听
 * @return
 */
public boolean start();

/**
 * 需要设置回调开启监听
 * @param hander
 * @return
 */
public boolean start(IRecvieHander hander);

/**
 * 没有回调时返回特定格式
 * 有回调时无效
 * @return
 */
public JYSocket recvice();

/**
 * 关闭
 */
public void close();

}
