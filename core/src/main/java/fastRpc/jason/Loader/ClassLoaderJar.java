/**    
 * 文件名：ClassLoaderJar.java    
 *    
 * 版本信息：    
 * 日期：2018年3月6日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import fastRpc.jason.Class.CommlibCls;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：ClassLoaderJar    
 * 类描述：    jar中动态加载
 * 加载Jar中的类
 * 创建人：jinyu    
 * 创建时间：2018年3月6日 上午1:59:27    
 * 修改人：jinyu    
 * 修改时间：2018年3月6日 上午1:59:27    
 * 修改备注：    
 * @version     
 *     
 */
public class ClassLoaderJar {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 加载类
     * @param jarFile  jar文件
     * @param clsName  类名称
     * @return
     */
private static Class<?> loaderThread(String jarFile,String clsName)
{

    Class<?> myClass = null;
    CommlibCls.getRpc().addJar(jarFile);
    try {
        myClass = CommlibCls.getRpc().loadClass(clsName);
    } catch (ClassNotFoundException e) {
       
        e.printStackTrace();
    }
    return myClass;
}
/**
 * 加载网络包及服务
 * @param jarFile
 * @param clsName
 * @return
 */
public static  Class<?> loader(String jarFile,String clsName)
{
   
    Callable<Class<?>> task=new  Callable<Class<?>>() {

        @Override
        public Class<?> call() throws Exception {
          
            return loaderThread(jarFile,clsName);
        }
        
    };
    Future<Class<?>> result = executorService.submit(task);
    try {
      return  result.get(1000, TimeUnit.SECONDS);
    } catch (Exception e) {
       return null;
    }
}
///**
// * 加载commonlib目录文件
// * @param jarFile  jar文件
// * @param clsList  所有要加载的类
// * @return
// */
//public static List<Class<?>> loaderCommlib(String dir,List<String> clsList)
//{
//    List<Class<?>> list=new ArrayList<Class<?>>();
//    File file=new File(dir);//类路径(包文件上一层)  
// 
//        URL url = null;
//        try {
//            url = file.toURI().toURL();
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
//     System.out.println("正在加载指定目录");
//        for(String clsName:clsList)
//        {
//            try
//            {
//              Class<?> myClass=  loader.loadClass(clsName);
//              if(myClass!=null)
//              list.add(myClass);
//            }
//            catch(Exception ex)
//            {
//                ex.printStackTrace();
//            }
//        }
//    
//    return list; 
//}

/**
 * 加载commonlib目录文件
 * @param jarFile  jar文件
 * @param clsList  所有要加载的类
 * @return
 */
public static List<Class<?>> loaderJar(String jarFile,List<String> clsList)
{
    List<Class<?>> list=new ArrayList<Class<?>>();
 
     System.out.println("正在加载指定目录");
     CommlibCls.getComlib().addJar(jarFile);
        for(String clsName:clsList)
        {
            try
            {
              Class<?> myClass=  CommlibCls.getComlib().loadClass(clsName);
              if(myClass!=null)
              list.add(myClass);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    
    return list; 
}
/**
 * 加载配置目录文件
 * @param jarFile  jar文件 加载其余包
 * @param clsList  所有要加载的类
 * @return
 */
public static List<Class<?>> loader(String jarFile,List<String> clsList)
{
  List<Class<?>> list=new ArrayList<Class<?>>();

      System.out.println("正在加载指定目录jar");
      for(String clsName:clsList)
      {
           Callable<Class<?>> task=new  Callable<Class<?>>() {
             @Override
             public Class<?> call() throws Exception {
                 Class<?> myClass= CommlibCls.getComlib().loadClass(clsName);
                  return myClass;
             }
         };
         Future<Class<?>> result = executorService.submit(task);
         try {
             Class<?> myClass=  result.get(300, TimeUnit.MILLISECONDS);
             list.add(myClass);
         } catch (Exception e) {
           
         }
           
        }
    
    return list; 
}
}
