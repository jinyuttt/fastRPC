/**    
 * 文件名：RPCServer.java    
 *    
 * 版本信息：    
 * 日期：2018年3月3日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：RPCServer    
 * 类描述：    RPC服务信息
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 上午12:04:36    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 上午12:04:36    
 * 修改备注：    
 * @version     
 *     
 */
public class RPCServer {
    private static RPCServer instance=null;
    private HashMap<String,RPCServiceInfo> mapSerivce=new HashMap<String,RPCServiceInfo>();
    
    /**
     * 
     * @Title: getInstance   
     * @Description: 单例
     * @return      
     * RPCServer      
     * @throws
     */
    public static RPCServer getInstance()
    {
        if(instance==null)
        {
            instance=new RPCServer();
        }
        return instance;
    }
    public  void printServer()
    {
        StringBuffer buf=new StringBuffer();
        buf.append("所有服务信息：");
        for(String key:mapSerivce.keySet())
        {
            buf.append(key+"\r\n");
        }
        System.out.println(buf.toString());
    }
    
    /**
     * 
     * @Title: getServer   
     * @Description: 返回json格式的服务信息 
     * @return      
     * String      
     * @throws
     */
    public String getServer()
    {
        return JSON.toJSONString(mapSerivce);
    }
    
    /**
     * 
     * @Title: register   
     * @Description: 注册服务
     * @param info      
     * void      
     * @throws
     */
    public void register(RPCServiceInfo info)
    {
        mapSerivce.put(info.rpcName, info);
    }
    
    /**
     * 
     * @Title: register   
     * @Description: 注册服务   
     * @param info      
     * void      
     * @throws
     */
    public void register(HashMap<String,RPCServiceInfo> info)
    {
        mapSerivce.putAll(info);
    }
    
    /**
     * 
     * @Title: getSerivce   
     * @Description: 获取服务  
     * @param name
     * @return      
     * RPCServiceInfo      
     * @throws
     */
    public RPCServiceInfo getSerivce(String name)
    {
        return mapSerivce.getOrDefault(name, null);
    }
}
