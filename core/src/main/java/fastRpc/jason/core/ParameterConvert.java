package fastRpc.jason.core;

public class ParameterConvert {
 /**
  * 
  *    
    
  * @param       
    
  * @param  @return    设定文件    
    
  * @return String    DOM对象    
    
  * @Exception 异常对象
  */
   public static RPCParameter StringTo(String paraName,String prara)
   {
       RPCParameter p=new RPCParameter();
       p.clsType="String";
       p.value=prara.getBytes();
       p.name=paraName;
       return p;
   }
   
   /**
    * 
    * @Title: JsonTo   
    * @Description: json格式字符串处理   
    * @param jsonprara
    * @return      
    * RPCParameter      
    * @throws
    */
   public static RPCParameter JsonTo(String paraName,String jsonprara)
   {
       RPCParameter p=new RPCParameter();
       p.clsType="json";
       p.value=jsonprara.getBytes();
       p.name=paraName;
       return p;
   }
   
   /**
    * 
    * @Title: ByteTo   
    * @Description: 直接传值   
    * @param prara
    * @return      
    * RPCParameter      
    * @throws
    */
   public static RPCParameter ByteTo(String paraName,byte[] prara)
   {
       RPCParameter p=new RPCParameter();
       p.clsType="byte[]";
       p.value=prara;
       p.name=paraName;
       return p;
   }
   
   /**
    * 
    * @Title: JsonTo   
    * @Description: 对象转成JSON格式序列化
    * @param para
    * @return      
    * RPCParameter      
    * @throws
    */
   public static RPCParameter JsonTo(String paraName,Object para)
   {
       RPCParameter p=new RPCParameter();
       p.clsType="json";
       p.value=RPCSerialization.ConvertBitJson(para);
       p.name=paraName;
       return p;
   }
   
   /**
    * 
    * @Title: ObjectTo   
    * @Description: 对象二进制序列化 
    * @param prara
    * @return      
    * RPCParameter      
    * @throws
    */
   public static RPCParameter ObjectTo(String paraName,Object prara)
   {
       byte[]  v = RPCSerialization.ConvertBit(prara);
       RPCParameter p=new RPCParameter();
       p.clsType="bit";
       p.value= v;
       p.name=paraName;
       return p;
   }
}
