/**    
 * 文件名：BasicType.java    
 *    
 * 版本信息：    
 * 日期：2018年3月3日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：BasicType    
 * 类描述：  判断基础类型的封装类型  
 * 创建人：jinyu    
 * 创建时间：2018年3月3日 上午11:27:21    
 * 修改人：jinyu    
 * 修改时间：2018年3月3日 上午11:27:21    
 * 修改备注：    
 * @version     
 *     
 */
public class BasicType {
    String[] types = {"java.lang.Integer",  
            "java.lang.Double",  
            "java.lang.Float",  
            "java.lang.Long",  
            "java.lang.Short",  
            "java.lang.Byte",  
            "java.lang.Boolean",  
            "java.lang.Character",  
            "java.lang.String",  
            "int","double","long","short","byte","boolean","char","float"}; 
    
    /**
     * 
     * @Title: isBasicType   
     * @Description: 判断基本类型或者封装类型 
     * @param obj
     * @return      
     * boolean      
     * @throws
     */
public static boolean isBasicType(Object obj)
{
   // String clsName=obj.getClass().getName();
    boolean isSucess=false;
   if(obj.getClass().isPrimitive())
   {
       isSucess=true;
   }
   else  if(obj instanceof Integer)
  {
      isSucess=true;
  }
  else if(obj instanceof Double)
  {
      isSucess=true;
  }
  else if(obj instanceof Float)
  {
      isSucess=true;
  }
  else if(obj instanceof Long)
  {
      isSucess=true;
  }
  else if(obj instanceof Short)
  {
      isSucess=true;
  }
  else if(obj instanceof Byte)
  {
      isSucess=true;
  }
  else if(obj instanceof Boolean)
  {
      isSucess=true;
  }
  else if(obj instanceof String)
  {
      isSucess=true;
  }
  return isSucess;
}
}
