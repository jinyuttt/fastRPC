package fastRpc.jason.core;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import org.msgpack.MessagePack;

import com.alibaba.fastjson.JSON;

/**    
 * 文件名：Util.java    
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
 * 类名称：Util    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 上午2:31:53    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 上午2:31:53    
 * 修改备注：    
 * @version     
 *     
 */
public class RPCSerialization {
   private static  MessagePack pack=new MessagePack();
   public static void register(Class<?> cls)
   {
       
       pack.register(cls);
   }
   /**
    * 
    * @Title: ConvertJson   
    * @Description: 对象转成json字符串   
    * @param obj
    * @return      
    * String      
    * @throws
    */
public static String ConvertJson(Object obj)
{
    return JSON.toJSONString(obj);
    
}

/**
 * 
 * @Title: ConvertBitJson   
 * @Description: 对象转成json格式的byte数组
 * @param obj
 * @return      
 * byte[]      
 * @throws
 */
public static byte[] ConvertBitJson(Object obj)
{
    return JSON.toJSONBytes(obj);
    
}

/**
 * 
 * @Title: ConvertBit   
 * @Description: 对象转成二进制 
 * @param obj
 * @return      
 * byte[]      
 * @throws
 */
public static byte[] ConvertBit(Object obj)
{
    try {
  OutputStream out = null;
pack.createPacker(out);
     return   pack.write(obj);
    } catch (IOException e) {
     
        e.printStackTrace();
    }
    return null;
    
}

/**
 * 
 * @Title: ConvertJsonObj   
 * @Description:  json格式数组转换为对象 
 * @param json
 * @return      
 * Object      
 * @throws
 */
public static Object ConvertJsonObj(byte[] json)
{
    return JSON.parse(json);
    
}

/**
 * 
 * @Title: ConvertObj   
 * @Description:    
 * @param json
 * @return      
 * Object      
 * @throws
 */
public static Object ConvertObj(String json)
{
    return JSON.parse(json);
    
}
public static Object ConvertBitObj(byte[] bytes, Class<?> klass)
{
 try {
    
    return  pack.read(bytes, klass);
} catch (IOException e) {
    
    e.printStackTrace();
}
return null;
    
}
public static Object ConvertBitObj(byte[] bytes, Object obj)
{
 try {
    
    return  pack.read(bytes, obj);
} catch (IOException e) {
    
    e.printStackTrace();
}
return null;
    
}
public static Object ConvertBasicObj(String cls,byte[] bytes)
{
    String args=new String(bytes);
    Class<?> obj = null;
    try {
        obj = Class.forName(cls);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    Method mt = null;
    if(!obj.getName().equals(String.class.getName()))
    {
    try {
        mt = obj.getMethod("ValueOf", String.class);
    } catch (NoSuchMethodException e) {
      
        e.printStackTrace();
    } catch (SecurityException e) {
       
        e.printStackTrace();
    
    }
    Object v = null;
    try {
        v = mt.invoke(obj, args);
    } catch (Exception e) {
       
        e.printStackTrace();
    }
    return v;
    }
    else
    {
        return args;
    }
  
    
}
}
