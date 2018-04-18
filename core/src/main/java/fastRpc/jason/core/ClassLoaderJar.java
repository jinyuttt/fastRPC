/**    
 * 文件名：ClassLoaderJar.java    
 *    
 * 版本信息：    
 * 日期：2018年3月6日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：ClassLoaderJar    
 * 类描述：    jar中动态加载
 * 创建人：jinyu    
 * 创建时间：2018年3月6日 上午1:59:27    
 * 修改人：jinyu    
 * 修改时间：2018年3月6日 上午1:59:27    
 * 修改备注：    
 * @version     
 *     
 */
public class ClassLoaderJar {
public static Class<?> loader(String jarFile,String clsName)
{
    URL url = null;
    try {
        url =new File(jarFile).toURI().toURL();
    } catch (MalformedURLException e) {
        return null;
    } 
    @SuppressWarnings("resource")
    URLClassLoader myClassLoader = new URLClassLoader(new URL[] { url }, Thread.currentThread()  
            .getContextClassLoader());  
    try {
        Class<?> myClass= myClassLoader.loadClass(clsName);
        return myClass;
    } catch (ClassNotFoundException e) {
    }
    return null; 
}
}
