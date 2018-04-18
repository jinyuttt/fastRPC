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
        RPCProxyClient<Void> c=new RPCProxyClient<Void>();
        RPCParameter ff=ParameterConvert.StringTo("ss","jinyu");
        List<RPCParameter> list=new ArrayList<RPCParameter>();
        list.add(ff);
        c.execute("callss", list, Void.class);
        System.out.println( "Hello World!" );
    }
}
