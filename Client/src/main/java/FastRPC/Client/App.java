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
        while(true)
        {
            for(int i=0;i<10;i++)
            {
         Thread rec=new Thread(new Runnable() {

            public void run() {
                RPCProxyClient<Void> c=new RPCProxyClient<Void>();
                RPCParameter ff=ParameterConvert.StringTo("param","select * from \"Test\"");
                List<RPCParameter> list=new ArrayList<RPCParameter>();
                list.add(ff);
                 c.execute("querySql", list, Void.class);
                
            }
             
         });
         rec.setDaemon(true);
         rec.start();
            }
         try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
        
      
    }
}
