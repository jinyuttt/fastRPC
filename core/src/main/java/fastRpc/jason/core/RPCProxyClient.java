/**
 * 
 */
package fastRpc.jason.core;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

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
        NetParameter netp=new NetParameter();
        netp.list=hash;
        para= RPCSerialization.ConvertBit(netp); 
    }
    byte[] data=new byte[method.length+seri.length+para.length+8];
    ByteBuffer buf=ByteBuffer.wrap(data);
    buf.putInt(method.length);
    buf.put(method);
    buf.putInt(seri.length);
    buf.put(seri);
    buf.put(para);
    NetProxy.getInstance().getProxy(name).sendData(data);
    byte[] bytes=  NetProxy.getInstance().getProxy(name).recvice();
    //
    if(bytes!=null)
    {
        ByteBuffer rbuf=ByteBuffer.wrap(bytes);
        int eLen=rbuf.getInt();
        byte[]error=new byte[eLen];
        rbuf.get(error);
        int clsLen=rbuf.getInt();
        byte[] clsBytes=new byte[clsLen];
        buf.get(clsBytes);
        byte[] rdata=new byte[rbuf.capacity()-rbuf.position()];
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
