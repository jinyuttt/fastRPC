/**    
 * 文件名：JavassistTools.java    
 *    
 * 版本信息：    
 * 日期：2018年3月9日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.core;

import java.util.HashMap;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：JavassistTools    
 * 类描述：    获取参数信息
 * 创建人：jinyu    
 * 创建时间：2018年3月9日 下午7:48:55    
 * 修改人：jinyu    
 * 修改时间：2018年3月9日 下午7:48:55    
 * 修改备注：    
 * @version     
 *     
 */
public class JavassistTools {
   static ClassPool pool = ClassPool.getDefault();
   
   /**
    * 
    * @Title: addPath   
    * @Description: 添加路径   
    * @param clsPath      
    * void      
    * @throws
    */
    public static void addPath(String clsPath)
    {
        try {
            pool.appendClassPath(clsPath);
        } catch (NotFoundException e) {
           
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Title: getClassInfo   
     * @Description: 获取参数信息
     * @param cls  类
     * @param clz  注解
     * @return      
     * HashMap<String,HashMap<String,String>>      
     * @throws
     */
public static HashMap<String,ParameterInfo> getClassInfo(Class<?> cls,Class<?> clz)
{
   
    HashMap<String,ParameterInfo> map=new HashMap<String,ParameterInfo>();
    CtClass cc = null;
    try {
     cc = pool.get(cls.getName());
     
     CtMethod[] cms = cc.getMethods();
     for(CtMethod cm:cms)
     {
       MethodInfo info = cm.getMethodInfo();
       AnnotationsAttribute attribute = (AnnotationsAttribute) info.getAttribute(AnnotationsAttribute.visibleTag);
       if(attribute==null)
       {
           continue;
       }
       Annotation n= attribute.getAnnotation(clz.getName());
       if(n==null)
       {
           continue;
       }
        HashMap<String,String> hash=new HashMap<String,String> ();
        ParameterInfo infop=new ParameterInfo();
        
        StringMemberValue value=(StringMemberValue) n.getMemberValue("name");
        String mapName=value.getValue();
        if(mapName.isEmpty())
        {
            mapName=info.getName().toLowerCase();
        }
        CodeAttribute codeAttribute = info.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        CtClass[] p= cm.getParameterTypes();
        String[] pName=new String[p.length];
         if(p!=null)
         {
           for(int i=0;i<p.length;i++)
           {
           String pname= p[i].getName();
           String namec= attr.variableName(i+pos);
           hash.put(namec, pname);
           pName[i]=namec;
           }
         }
         infop.map=hash;
         infop.params=pName;
         map.put(mapName.trim(), infop);
     }
     cc.detach();
    
    } catch (NotFoundException e) {
     e.printStackTrace();
    }
   
    return map;
    
}
}
