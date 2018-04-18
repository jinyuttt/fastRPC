/**    
 * 文件名：RPCServiceInfo.java    
 *    
 * 版本信息：    
 * 日期：2018年3月3日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.lang.reflect.Method;
import java.util.HashMap;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：RPCServiceInfo    
 * 类描述：    RPC服务参数信息
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 上午12:08:51    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 上午12:08:51    
 * 修改备注：    
 * @version     
 *     
 */
public class RPCServiceInfo {

    public byte maxSize=5;
    public String rpcName;
    public String methodName;
    public String cls;
    public Method method;
    public Object[] instance;
    public int index=0;
    public HashMap<String, String> hashPara=null;
    public String[] parameter;
    public String returnType;
    public Object getObj()
    {
        return instance[index++%maxSize];
    }
}
