/**    
 * 文件名：CustomerClassLoader.java    
 *    
 * 版本信息：    
 * 日期：2018年5月12日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**    
 *     
 * 项目名称：core    
 * 类名称：CustomerClassLoader    
 * 类描述：    加载目录下的jar
 * 创建人：jinyu    
 * 创建时间：2018年5月12日 下午5:04:14    
 * 修改人：jinyu    
 * 修改时间：2018年5月12日 下午5:04:14    
 * 修改备注：    
 * @version     
 *     
 */
public class CustomerClassLoader extends URLClassLoader {
    //类加载器名称
    private String loaderName="";
    public CustomerClassLoader(URL[] urls, ClassLoader parent,String name) {
        super(urls, parent);
        this.loaderName=name;
    }
    public CustomerClassLoader(URL[] urls,String name) {
        super(urls);
        this.loaderName=name;
    }
    public CustomerClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        
    }
    public CustomerClassLoader(URL[] urls) {
        super(urls);
    }
    
    /**
     * 加载器名称
     * @return
     */
    public String getName()
    {
        return loaderName;
    }
    @Override
    public Class<?> findClass(String name) {
        try {
            //System.out.println(loaderName+":"+name);
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
        }
        return null;
        
    }
   public void addJAR(String jar)
   {
       if(jar==null)
       {
           return;
       }
       File file=new File(jar);//类路径(包文件上一层)  
       
       URL url = null;
       try {
           url = file.toURI().toURL();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } 
    this.addURL(url);
   }
   public void addDir(String[] dirs)
   {
       if(dirs==null)
       {
           return;
       }
       for(String dir:dirs)
       {
           File f=new File(dir);
           if(f.exists()&&f.isDirectory())
           {
             File[] list=f.listFiles(); 
            for(File ff:list)
            {
              URL url = null;
              try {
                 url = ff.toURI().toURL();
                 this.addURL(url);
              } catch (MalformedURLException e) {
                 e.printStackTrace();
               } 
              }
           }
           else if(f.exists())
           {
               URL url = null;
               try {
                  url = f.toURI().toURL();
                  this.addURL(url);
               } catch (MalformedURLException e) {
                  e.printStackTrace();
                }  
           }
      
       }
   }

public void addJar(String jarFile) {
    
    addJAR(jarFile);
}

public void addLibDirs(String[] allfiles) {
    addDir(allfiles);
}
} 
