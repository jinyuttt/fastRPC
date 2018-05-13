package fastRpc.jason.core;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;

import fastRpc.jason.core.PackageUtil;
import fastRpc.jason.core.RPCMap;
import fastRpc.jason.core.RPCServer;
import fastRpc.jason.core.RPCServiceInfo;

/**    
 * 文件名：RPCRegister.java    
 *    
 * 版本信息：    
 * 日期：2018年3月3日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：RPCRegister    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 上午1:12:34    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 上午1:12:34    
 * 修改备注：    
 * @version     
 *     
 */
public class RPCRegister {
    
    /**
     * 
     * @Title: register   
     * @Description: 获取注册方法信息
     * @param cls
     * @return      
     * HashMap<String,RPCServiceInfo>      
     * @throws
     */
    private static  HashMap<String,RPCServiceInfo> register(Class<?> cls)
    {
        HashMap<String,RPCServiceInfo> hash=new HashMap<String,RPCServiceInfo> ();
        Class<?> msCls = cls;
      Method[] ms=  msCls.getDeclaredMethods();
      if(ms!=null)
      {
          for(int i=0;i<ms.length;i++)
          {
              RPCMap[] map=  ms[i].getAnnotationsByType(RPCMap.class);
              if(map!=null&&map.length>0)
              {
                  String name="";
                  RPCServiceInfo info=new RPCServiceInfo();
                  if(map[0].name()==null||map[0].name().trim().isEmpty())
                  {
                      name= ms[i].getName().toLowerCase();
                    
                  }
                  else
                  {
                      name=map[0].name().trim().toLowerCase();
                  }
                  info.cls=msCls.getName();
                  info.methodName= ms[i].getName();
                  info.method=ms[i];
                  Parameter[] p=ms[i].getParameters();
                  if(p!=null&&p.length>0)
                  {
                      info.hashPara=new HashMap<String,String>();
                      for(int j=0;i<p.length;j++)
                      {
                          info.hashPara.put(p[j].getName().toLowerCase(),p[j].getType().getName());
                      }
                  }
                  
                 //创建指定个数对象
                  info.instance= (Object[]) Array.newInstance(msCls, info.maxSize);
                  hash.put(name, info);
              }
              
          }
      }
        return hash;
    }
   
    
/**
 * 
 * @Title: registerObj   
 * @Description:  手段注册   
 * @param cls      
 * void      
 * @throws
 */
    public static void  registerObj(Class<?> cls)
    {
        HashMap<String,RPCServiceInfo> info=register(cls);
        RPCServer.getInstance().register(info);
    }
    
   
    /**
     * 
     * @Title: registerPath   
     * @Description: 获取注册类型  
     * @param jarPath      
     * void      
     * @throws
     */
    public static void  registerPath(String jarPath)
    {
        System.out.println("服务注册目录："+jarPath);
        List<String> listJar= PackageUtil.getJarFiles(jarPath);
       for(String file:listJar)
       {
           List<String> cls=PackageUtil.getCls(file);
           for(String clsName:cls)
           {
               HashMap<String,RPCServiceInfo> info=PackageUtil.getRPC(file,clsName);
               if(!info.isEmpty())
               {
                  RPCServer.getInstance().register(info);
               }
           }
       }
       RPCServer.getInstance().printServer();
    }
    
   
    
   
    /**
     * 
     * @Title: registerService   
     * @Description: 直接注册服务信息  
     * @param service      
     * void      
     * @throws
     */
    public static void registerService(RPCServiceInfo service)
    {
        RPCServer.getInstance().register(service);
    }
}
