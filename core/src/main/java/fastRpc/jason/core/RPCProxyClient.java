/**
 * 
 */
package fastRpc.jason.core;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

import fastRpc.jason.core.NetProxy.SocketProxy;

/**
 * @author jinyu
 *  代理
 */
public class RPCProxyClient<T> {
    public String returnCode="json";
@SuppressWarnings("unchecked")
public  T execute(String name,List<RPCParameter> hash,Class<?> clsT)
{
    byte[] method = null;
    try {
        method = name.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
        
        e.printStackTrace();
    }
    byte[] seri = null;
    try {
        seri = returnCode.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
     
        e.printStackTrace();
    }
    byte[] para=new byte[0];
    if(hash!=null)
    {
        //序列化
        NetParameter netp=new NetParameter();
        netp.list=hash;
        para= RPCSerialization.ConvertBit(netp); 
    }
    //组包
    byte[] data=new byte[method.length+seri.length+para.length+8];
    ByteBuffer buf=ByteBuffer.wrap(data);
    buf.putInt(method.length);
    buf.put(method);
    buf.putInt(seri.length);
    buf.put(seri);
    buf.put(para);
    //发送请求
    SocketProxy proxy=NetProxy.getInstance().getProxy(name);
    proxy.sendData(data);
    buf.clear();
    //等待接收
    byte[] bytes= proxy.recvice();
    NetProxy.getInstance().freeProxy(proxy);
    //
    if(bytes!=null&&bytes.length>0)
    {
        ByteBuffer rbuf=ByteBuffer.wrap(bytes);
        int eLen=rbuf.getInt();
        byte[]error=new byte[eLen];
        rbuf.get(error);
        int clsLen=rbuf.getInt();
        byte[] clsBytes=new byte[clsLen];
        rbuf.get(clsBytes);
        //
        int rLen=rbuf.getInt();
        byte[] rdata=new byte[rLen];
        rbuf.get(rdata);
        //
        String strerror=new String(error);
        String clsName=new String(clsBytes);
        if(strerror.isEmpty())
        {
            if(clsName.equals("String"))
            {
            
               Object r = RPCSerialization.ConvertBasicObj(clsT.getName(), rdata);
               return (T) r;//基础类型转换
            }
            else if(clsName.equals("json"))
            {
                Object r=RPCSerialization.ConvertJsonObj(rdata);
                return (T) r;
            }
            else if(clsName.equals("bit"))
            {
                Object r=RPCSerialization.ConvertBitObj(rdata, clsT);
                return (T) r;
            }
            else if(clsName.equals("byte[]"))
            {
                return (T) rdata;
            }
        }
    }
    return null;
	
}
}
