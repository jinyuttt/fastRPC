/**    
 * 文件名：RpcClassLoader.java    
 *    
 * 版本信息：    
 * 日期：2018年5月5日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**    
 *     
 * 项目名称：core    
 * 类名称：RpcClassLoader    
 * 类描述：    自定义加载器
 * 创建人：jinyu    
 * 创建时间：2018年5月5日 下午5:27:45    
 * 修改人：jinyu    
 * 修改时间：2018年5月5日 下午5:27:45    
 * 修改备注：    
 * @version     
 *     
 */
public class RpcClassLoader extends ClassLoader {
   private static ExecutorService executorService = Executors.newCachedThreadPool();   
  //类加载器名称
    private String loaderName;
    //加载类的路径
    private String rootDir= "";
    
    private List<String> libsDir=null;
    
    private List<String> classDir=null;
    
    private final String fileType = ".class";
    
    private volatile boolean isInit=true;
    /**
     * 采取将所有的jar包中的class读取到内存中
     * 然后如果需要读取的时候，再从map中查找
     */
    private Map<String, byte[]> map;
    
   /**
    * 加载class文件目录
    */
    private String classes;
    
    
    public RpcClassLoader(String loaderName){
        //让系统类加载器成为该 类加载器的父加载器
        super();
        this.loaderName = loaderName;
        libsDir=new ArrayList<String>(64);
        classDir=new ArrayList<String>(64);
        map = new HashMap<String,byte[]>(64);
    }
   
    
    public RpcClassLoader(ClassLoader parent, String loaderName){
        //显示指定该类加载器的父加载器
        super(parent);
        libsDir=new ArrayList<String>(64);
        classDir=new ArrayList<String>(64);
        this.loaderName = loaderName;
        map = new HashMap<String,byte[]>(64);
        preReadJarFile();
    }
    /**
     * 预读lib下面的包
     */
    private void preReadJarFile(){
        List<File> list = scanDir(libsDir);
        for(File f : list){
            JarFile jar;
            try {
                jar = new JarFile(f);
                readJAR(jar);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getlibPath() {
        return rootDir;
    }

    public void setPath(String libpath) {
        libsDir.add(libpath);
        this.rootDir = libpath;
    }
   public String getClassPath()
   {
       return this.classes;
   }
   
   public void setClassPath(String clsPath)
   {
       classDir.add(clsPath);
       this.classes=clsPath;
   }
    @Override
    public String toString() {
        return this.loaderName;
    }

    /**
     * 获取文件字节
     * 获取.class文件的字节数组
     * @param classname
     * @return
     */
    private byte[] getClassData(String classname)
    {
        byte[] data = null;
        for(String str:classDir)
        {
        String name = classname.replace(".", "/");
        String path = str+"/"+name+fileType;
        //IOUtils,可以使用它将流中的数据装换成字节数组
        InputStream is = null;
        //字节输出流
        File f=new File(path);
        if(!f.exists())
        {
            continue;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            
            is = new FileInputStream(path);
            
            byte[] buffer = new byte[1024];
            int temp = 0;
            while((temp = is.read(buffer))!=-1)
            {
                baos.write(buffer,0,temp);
            }
            data= baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally
        {
            try {
                if(is!=null)
                {
                    is.close();
                }                    
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(baos!=null)
                {
                    baos.close();
                }                    
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
          return data;
        }
        return data;
    }
   
    /**
     * 获取Class对象
     */
    @Override
    public Class<?> findClass(String name) {
        
        if(name.contains("META-INF"))
        {
            return null;
        }
        
        Callable<Class<?>> myCallable = new Callable<Class<?>>() {
            @Override
            public Class<?> call() throws Exception {
                Class<?> c = findLoadedClass(name);//查找已加载的类
                //应该要先查询有没有加载过这个类。如果已经加载，不为空，则直接返回加载好的类。
                //如果没有，则加载新的类。
                if(c!=null){
                    return c;
                } else {
                    //获得他的父类，让父类去加载去加载
                    ClassLoader parent = Thread.currentThread().getContextClassLoader(); //获得
                    //采用的是双亲委派机制
                    try {
                        c = parent.loadClass(name);//委派给父类加载
                    } 
                    catch (Exception e) {
                    }
                    //如果不为空，返回父类加载。
                    if(c!=null)
                    {
                        return c;
                    }else{
                        try {
                            if(isInit)
                            {
                                preReadJarFile();
                                isInit=false;
                            }
                            byte[] result = getClassFromFileOrMap(name);
                            if(result == null){
                               System.out.println("加载失败："+name);
                            }else{
                                try
                                {
                                   c= defineClass(name, result, 0, result.length);
                                   return c;
                                }
                                catch(Exception ex)
                                {
                                    System.out.println("");
                                }
                                
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return c;

            }
           
        }
            }
        };
      
        Future<Class<?>> result=executorService.submit(myCallable);
        try {
            return result.get();
        } catch (Exception e) {
            
            e.printStackTrace();
        }
       return null;
        
    }
    
    /**
     * 获取类
     * @param name
     * @return
     */
    private byte[] getClassFromFileOrMap(String name) {
       byte[]data= getClassData(name);
        if(data!=null)
        {
            return data;
        }else{
            if(map.containsKey(name)) {
                //去除map中的引用，避免GC无法回收无用的class文件
                return map.remove(name);
            }
        }
        return null;
    }

    /**
    * 扫描lib下面的所有jar包
     * @return
     */
    private List<File> scanDir(List<String> libsDir2) {
        List<File> list = new ArrayList<File>();
        for(String dir:libsDir2)
        {
        File[] files = new File(dir).listFiles();
        if(files!=null)
        {
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".jar"))
                list.add(f);
        }
        }
        }
        return list;
    }
    
    public void addLibDir(String dir)
    {
        libsDir.add(dir);
    }
    
    public void addClassDir(String dir)
    {
        classDir.add(dir);
    }
    public void addLibDirs(String[] dirs)
    {
        if(dirs==null)
        {
            return;
        }
        for(String  dir:dirs)
        {
        libsDir.add(dir);
        }
    }
    
    public void addClassDirs(String[] dirs)
    {
        if(dirs==null)
        {
            return;
        }
        for(String  dir:dirs)
        {
        classDir.add(dir);
        }
    }
    /**
     * 添加一个jar包到加载器中去。
     * @param jarPath
     * @throws IOException 
     */
    public void addJar(String jarPath) throws IOException{
        File file = new File(jarPath);
        if(file.exists()){
            JarFile jar = new JarFile(file);
            readJAR(jar);
        }
    }
    
    /**
     * 读取一个jar包内的class文件，并存在当前加载器的map中
     * @param jar
     * @throws IOException
     */
    private void readJAR(JarFile jar) throws IOException{
        Enumeration<JarEntry> en = jar.entries();
        while (en.hasMoreElements()){
            JarEntry je = en.nextElement();
            String name = je.getName();
            if (name.endsWith(".class")){
                String clss = name.replace(".class", "").replaceAll("/", ".");
                if(this.findLoadedClass(clss) != null) continue;
                
                InputStream input = jar.getInputStream(je);
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
                int bufferSize = 4096; 
                byte[] buffer = new byte[bufferSize]; 
                int bytesNumRead = 0; 
                while ((bytesNumRead = input.read(buffer)) != -1) { 
                    baos.write(buffer, 0, bytesNumRead); 
                }
                byte[] cc = baos.toByteArray();
                input.close();
                map.put(clss, cc);//暂时保存下来
            }
        }
    }
        
}
