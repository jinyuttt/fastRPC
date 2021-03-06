/**    
 * 文件名：IClient.java    
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
 * 类名称：IClient    
 * 类描述：   客户端接口 
 * 创建人：jinyu    
 * 创建时间：2018年4月15日 上午12:57:04    
 * 修改人：jinyu    
 * 修改时间：2018年4月15日 上午12:57:04    
 * 修改备注：    
 * @version     
 *     
 */
public interface INetClient {
    
 /**
  * 设置本地
  * @param ip
  */
public void setLocalIP(String ip);

/**
 * 设置本地端口
 * @param port
 */
public void setLocalPort(int port);

/**
 * 设置远端ip
 * @param ip
 */
public void setRemoteIP(String ip);

/**
 * 设置远端端口
 * @param port
 */
public void setRemotePort(int port);

/**
 * 设置底层通信对象
 * @param client
 */
public void setClient(Object client);

/**
 * 连接远端
 * @param remoteIP
 * @param remotePort
 * @return
 */
public boolean connect(String remoteIP,int remotePort);

/**
 * 连接远端
 * @return
 */
public boolean connect();

/**
 * 连接的通信发送数据
 * @param data
 */
public void sendData(byte[]data);

/**
 * 非连接的远端发送数据
 * 连接的远端ip,端口无效
 * @param ip
 * @param port
 * @param data
 */
public void sendData(String ip,int port,byte[]data);

/**
 * 接收数据
 * 仅仅返回接收的数据
 * @return
 */
public byte[] recviceData();

public byte[] recDirect();

/**
 * 返回特定的格式
 * 有回调时无效
 * @return
 */
public JYSocket recvice();

/**
 * 接收数据设置回调
 * @param hander
 */
public void startRecvice(IRecvieHander hander);

/**
 * 设置接收缓存，默认1
 * @param size
 */
public void setBufferSize(int size);

/**
 * 关闭
 */
public void close();

/**
 * 是否关闭
 * @return
 */
public boolean isClose();

/**
 * 是否有接收的数据
 * @return
 */
public boolean isHavRec();

public boolean isConnected();

}
