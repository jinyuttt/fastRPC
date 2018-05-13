/**    
 * 文件名：CommlibCls.java    
 *    
 * 版本信息：    
 * 日期：2018年5月4日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Class;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import fastRpc.jason.Loader.CustomerClassLoader;
/**    
 *     
 * 项目名称：core    
 * 类名称：CommlibCls    
 * 类描述：   保持common目录类
 * 创建人：jinyu    
 * 创建时间：2018年5月4日 上午1:18:12    
 * 修改人：jinyu    
 * 修改时间：2018年5月4日 上午1:18:12    
 * 修改备注：    
 * @version     
 *     
 */
public class CommlibCls {
private  static CustomerClassLoader myclassRpc=null;
private  static CustomerClassLoader myclasslib=null;
//private  static URLClassLoader myCommonlib=null;
//private  static URLClassLoader myclassRpc=null;
//private  static RpcClassLoader myclassohter=null;
private  static ConcurrentHashMap<String,List<Class<?>>> hash=new  ConcurrentHashMap<String,List<Class<?>>>();
public static void add(String jar,List<Class<?>> list)
{
    hash.put(jar, list);
}
public static List<Class<?>> getListCls(String jar)
{
    return hash.getOrDefault(jar, null);
}
public synchronized static CustomerClassLoader getRpc()
{
    if(myclassRpc==null)
    {
        myclassRpc=new CustomerClassLoader(new URL[0],getComlib(),"RPC");
    }
    return myclassRpc;
}
public synchronized static CustomerClassLoader getComlib()
{
    if(myclasslib==null)
    {
        myclasslib=new CustomerClassLoader(new URL[0],"lib");
    }
    return myclasslib;
}

}
