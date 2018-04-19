package FastRPC.Client;

import java.util.ArrayList;
import java.util.List;

import fastRpc.jason.core.ParameterConvert;
import fastRpc.jason.core.RPCParameter;
import fastRpc.jason.core.RPCProxyClient;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        RPCProxyClient<String> c=new RPCProxyClient<String>();
        RPCParameter ff=ParameterConvert.StringTo("ss","jinyu");
        List<RPCParameter> list=new ArrayList<RPCParameter>();
        list.add(ff);
      String ss=  c.execute("callss", list, String.class);
        System.out.println( ss );
    }
}
