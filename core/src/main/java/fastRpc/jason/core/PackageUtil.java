/**    
 * 文件名：PackageUtil.java    
 *    
 * 版本信息：    
 * 日期：2018年3月3日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import fastRpc.jason.inet.INetClient;
import fastRpc.jason.inet.INetServer;
import fastRpc.jason.net.NetType;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：PackageUtil    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 上午12:27:46    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 上午12:27:46    
 * 修改备注：    
 * @version     
 *     
 */
public class PackageUtil {

   
    /**
     * 
     * @Title: getJarFiles   
     * @Description: 获取jar文件  
     * @param path
     * @return      
     * ArrayList<String>      
     * @throws
     */
    public static ArrayList<String> getJarFiles(String path)
    {
        if(path==null||path.trim().isEmpty())
        {
            path=PackageUtil.class.getClassLoader().getResource("").toString()+"/server";
            System.out.println("读取的目录："+path);
        }
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if(tempList!=null)
        {
          for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                if(tempList[i].toString().endsWith(".jar"))
                files.add(tempList[i].toString());
            }
           
          }
        }
        return files;
    }
    
    /**
     * 
     * @Title: getCls   
     * @Description: 获取jar文件中的类  
     * @param filejar
     * @return      
     * ArrayList<String>      
     * @throws
     */
   public static ArrayList<String> getCls(String filejar)
   {
    
       ArrayList<String> myClassName=new ArrayList<String>();
       JarFile file = null;
    try {
        file = new JarFile(filejar);
    } catch (IOException e) {
      
        e.printStackTrace();
    }
       Enumeration<JarEntry> entrys = file.entries();
       while(entrys.hasMoreElements()){
           try
           {
           JarEntry jar = entrys.nextElement();
           String entryName = jar.getName();  
           entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));  
            myClassName.add(entryName);  
           }
           catch(Exception ex)
           {
               
           }
           
       }
       
       
       try {
        file.close();
    } catch (IOException e) {
       
        e.printStackTrace();
    }
    return myClassName;
   }
   
   /**
    * 
    * @Title: getRPC   
    * @Description: 获取RPC信息  
    * @param cls
    * @return      
    * HashMap<String,RPCServiceInfo>      
    * @throws
    */
   public static HashMap<String,RPCServiceInfo>  getRPC(String jarFile,String cls)
   {
       HashMap<String,RPCServiceInfo> hash=new HashMap<String,RPCServiceInfo> ();
       Class<?> msCls = null;
       msCls= ClassLoaderJar.loader(jarFile, cls);
       if(msCls==null)
       {
           return hash;
       }
       //添加包
     JavassistTools.addPath(jarFile);
     HashMap<String, ParameterInfo> mapPara=JavassistTools.getClassInfo(msCls,RPCMap.class);
     if(mapPara.isEmpty())
     {
         return hash;
     }
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
                 info.cls=cls;
                 info.method=ms[i];
                 info.methodName= ms[i].getName();
           
                  ParameterInfo pinfo = mapPara.getOrDefault(name, null);
                  if(pinfo!=null)
                  {
                      info.hashPara=pinfo.map;
                      info.parameter=pinfo.params;
                  }
                //创建指定个数对象
                 try {
                     info.instance=new Object[info.maxSize];
                     for(int j=0;j<info.maxSize;j++)
                     {
                        Object obj=msCls.newInstance();
                        info.instance[j]=obj;
                     }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 hash.put(name, info);
             }
             
         }
     }
     mapPara.clear();
     return hash;
   }
 
   
   /**
    * 
    * @Title: getNet   
    * @Description: 获取通信类接口   
    * @param jarPath
    * @return      
    * HashMap<String,String>      
    * @throws
    */
   public static HashMap<String,String>  getNet(String jarPath)
   {
       HashMap<String,String> hash=new HashMap<String,String> ();
       ArrayList<String> list= getCls(jarPath);
        for(String cls:list)
        {
            Class<?> msCls = null;
            msCls=ClassLoaderJar.loader(jarPath, cls);
            if(msCls==null)
            {
                continue;
            }
            NetType[] net= msCls.getAnnotationsByType(NetType.class);
            if(net!=null&&net.length>0&&INetClient.class.isAssignableFrom(msCls))
            {
                hash.put(net[0].value(), msCls.getName());
            }
            if(net!=null&&net.length>0&&INetServer.class.isAssignableFrom(msCls))
            {
                hash.put(net[0].value(), msCls.getName());
            }
        }
       return hash;
   }
}
