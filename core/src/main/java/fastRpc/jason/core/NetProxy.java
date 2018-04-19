/**
 * 
 */
package fastRpc.jason.core;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import fastRpc.jason.inet.INetClient;

/**
 * 
 *     
 * 项目名称：core    
 * 类名称：NetProxy    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年4月19日 下午9:42:07    
 * 修改人：jinyu    
 * 修改时间：2018年4月19日 下午9:42:07    
 * 修改备注：    
 * @version     
 *
 */
public class NetProxy {
    
    /**
     * 
     *     
     * 项目名称：core    
     * 类名称：InnerProxy    
     * 类描述：   内部请求代理
     * 创建人：jinyu    
     * 创建时间：2018年4月19日 下午9:41:58    
     * 修改人：jinyu    
     * 修改时间：2018年4月19日 下午9:41:58    
     * 修改备注：    
     * @version     
     *
     */
    public class InnerProxy
    {
        private  List<SocketProxy> socket;//通信集合
        public String netType="tcp";//通信类型
        public String address="127.0.0.1";//地址，端口
        public int port=8888;
        public volatile int index=0;
        public Class<?> cls=null;
        public String name="";
        public synchronized SocketProxy get()
        {
            if(socket.isEmpty())
            {
                SocketProxy p=new SocketProxy();
                try {
                    p.socket=(INetClient) cls.newInstance();
                    p.name=name;
                    p.address=address;
                    p.port=port;
                    p.socket.connect(address, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return p;
            }
            SocketProxy proxy=  socket.remove(0);
            proxy.address=address;
            proxy.port=port;
            proxy.socket.connect(address, port);
            return proxy;
        }
        public synchronized void free(SocketProxy e)
        {
            socket.add(e);
        }
        
    }
    
    /**
     * 
     *     
     * 项目名称：core    
     * 类名称：SocketProxy    
     * 类描述：    通信关联
     * 创建人：jinyu    
     * 创建时间：2018年4月19日 下午9:43:36    
     * 修改人：jinyu    
     * 修改时间：2018年4月19日 下午9:43:36    
     * 修改备注：    
     * @version     
     *
     */
    public class SocketProxy
    {
        private  INetClient socket;
        public String name="";
        public String address="localhost";
        public int port=8888;
        public void set(INetClient client)
        {
            this.socket=client;
        }
        public void close()
        {
            socket.close();
        }
        public void sendData(byte[]data)
        {
            socket.sendData(address, port, data);
        }
        public byte[] recvice()
        {
            return socket.recviceData();
        }
    }
  private static NetProxy  instance=null;
  public static String netJar="net.jar";
  public  static int maxSize=5;
  private static boolean isInit=true;
  
  /**
   * 通信对象
   */
  private HashMap<String,InnerProxy> sockets=new HashMap<String,InnerProxy>();
  
  /**
   * 通信类信息
   */
  private HashMap<String,String> hashCls=new HashMap<String,String>();
  
  /**
   * 配置信息
   * 主要是服务地址
   */
  private HashMap<String,String> hash=new HashMap<String,String>();
  public static NetProxy getInstance()
  {
      if(instance==null)
      {
          instance=new NetProxy();
         
      }
      return instance;
  }
  public NetProxy()
  {
      
  }
  
  /**
   * 初始化读取配置
   */
  private  void init()
  {
      Properties properties = new Properties();
      FileInputStream in = null;
      HashMap<String,String> map=new HashMap<String,String>();
      try {
          File conf=new File("config/client.properties");
          if(conf.isFile()&&conf.exists())
          {
           in = new FileInputStream("config/client.properties");
           properties.load(in);
           in.close();
          }
       
      } catch (Exception e1) {
          e1.printStackTrace();
      }
      String addr= properties.getProperty("address","tcp -h localhost -p 8888");
      netJar=properties.getProperty("netjar", "net.jar");
      map.put("address", addr);
      map.put("netjar", netJar);
      //获取所有配置
      for(Object key:properties.keySet())
      {
          map.put(key.toString().trim().toLowerCase(), properties.getProperty(key.toString()));
      }
      String size= map.getOrDefault("maxSzie", "5");
      maxSize=Integer.valueOf(size);
      hash.putAll(map);
      
  }
  
  /**
   * 注册服务地址
   * @Title: srcAddressInit   
   * @Description:   注册服务地址
   * @param map      
   * void      
   * @throws
   */
  public  void  srcAddressInit(HashMap<String,String> map)
  {
    if(map!=null)
    {
      hash.putAll(map);
    }
  }
 

 public void UtilInit()
  {
      init();
      hashCls=PackageUtil.getNet(netJar);
      isInit=false;
  }
  
 /**
  * 获取通信信息
  * @param name 服务名称
  * @return
  */
  public synchronized SocketProxy getProxy(String name)
  {
      if(name==null||name.trim().isEmpty())
      {
          name="address";
      }
      String pname=name.trim().toLowerCase();
      InnerProxy tmp= sockets.getOrDefault(pname, null);
      if(tmp==null)
      {      if(isInit)
            {
                 this.UtilInit();
            }
              tmp=new InnerProxy();
              tmp.socket=new ArrayList<SocketProxy>();
              String host=hash.getOrDefault(pname, null);
              tmp.name=name.trim().toLowerCase();
              if(host==null)
              {
                  host=hash.getOrDefault("address", null);
              }
              if(host!=null)
              {
                  
                  host=host.trim().toLowerCase();
                  String[]type=host.split(" ");
                  StringBuffer buf=new StringBuffer();
                  for(int j=0;j<type.length;j++)
                  {
                      if(!type[j].trim().isEmpty())
                      {
                          buf.append(type[j].trim().toLowerCase()+";");
                      }
                  }
                  // 按照格式解析通信
                  String[] addr=buf.toString().split(";");
                  tmp.netType=addr[0].trim();
                  for(int k=1;k<addr.length;k++)
                  {
                      if(addr[k].equals("-h"))
                      {
                          tmp.address=addr[k+1].trim();
                      }
                      else if(addr[k].equals("-p"))
                      {
                          tmp.port=Integer.valueOf(addr[k+1].trim());
                      }
                  }
                  //加载客户端通信
                  String clsName= hashCls.getOrDefault(tmp.netType+"_Client", null);
                  if(clsName==null)
                  {
                      clsName= hashCls.getOrDefault("tcp_Client", null);
                  }
                  if(clsName!=null)
                  {
                     Class<?> klss = null;
                    try {
                        klss = Class.forName(clsName);
                    } catch (ClassNotFoundException e1) {
                     
                        e1.printStackTrace();
                    }
                     tmp.cls=klss;
                    for(int i=0;i<maxSize;i++)
                    {
                       SocketProxy p=new SocketProxy();
                       p.name=name.trim().toLowerCase();
                       INetClient socket = null;
                        try {
                            socket = (INetClient)klss.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                         p.socket=socket;
                         tmp.socket.add(p);
                   
                    }
                  }
              
          }
             
           sockets.put(name, tmp);  
      }
     
    return tmp.get();
      
  }
  public void freeProxy(SocketProxy proxy)
  {
      proxy.socket.close();
      InnerProxy inner=  sockets.get(proxy.name);
      inner.free(proxy);
  }
}
