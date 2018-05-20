/**
 * 
 */
package fastRpc.jason.tcpaio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import fastRpc.jason.inet.JYSocket;


/**
 * 
 *     
 * 项目名称：net    
 * 类名称：AioReadHandler    
 * 类描述：    读取数据完成回调
 *         需要传入顶层
 *         将接收的数据回传给顶层
 * 创建人：jinyu    
 * 创建时间：2018年5月19日 下午5:11:03    
 * 修改人：jinyu    
 * 修改时间：2018年5月19日 下午5:11:03    
 * 修改备注：    
 * @version     
 *
 */
//这里的参数型号，受调用它的函数决定。这里是受客户端socket.read调用  
public class AioReadHandler implements CompletionHandler<Integer, ByteBuffer>{
	 private AsynchronousSocketChannel socket; 
	    public AioReadHandler(AsynchronousSocketChannel socket) { 
	        this.socket = socket; 
	    } 
	private AioTcpClient client=null;
    private String localIP;
    private int localPort;
    private String remoteIP;
    private int remotePort;
    
    /**
     * 将顶层封装传入，
     * 用于回传数据
     * @param obj
     */
	 public void setClient(AioTcpClient obj)
	 {
	     this.client=obj;
	 }
	 
	 /**
	  * 将读取的数据整理结构后回传
	  * 通信对象一样回传
	  * @param buffer
	  * @param r
	  */
	 public void callSocket(ByteBuffer buffer, int r)
	 {
	     //将数读入
	     JYSocket data=new JYSocket();
         byte[] tmp=new byte[r];
         System.arraycopy(buffer.array(), 0, tmp, 0, r);
         data.data=tmp;
         try
         {
          if(localIP.isEmpty()||localPort==0)
           {
               InetSocketAddress addr=(InetSocketAddress) socket.getLocalAddress();
                localIP=addr.getHostString();
                localPort=addr.getPort();
          }
         if(remoteIP.isEmpty()||remotePort==0)
         {
            InetSocketAddress addr=(InetSocketAddress) socket.getRemoteAddress();
            remoteIP=addr.getHostString();
            remotePort=addr.getPort();
         }
         }
         catch(Exception ex)
         {
             
         }
         data.localIP=localIP;
         data.localPort=localPort;
         data.srvIP=remoteIP;
         data.srvPort=remotePort;
         data.socket=client;//可以不用，数据已经交给线程读取了
         data.isTcpType=true;
         //顶层传入了回调则使用回调，否则放入队列
         if(client.getHander()!=null)
         {
             client.getHander().recviceData(tmp);
             client.getHander().recviceData(data);
         }
         else
         {
            //
             client.addSocket(data);
         }
	 }
	    public void cancelled(ByteBuffer attachment) { 
	        System.out.println("cancelled"); 
	    } 
	    public void completed(Integer i, ByteBuffer buf) { 
	        System.out.println("AioReadHandler"); 
	        if (i > 0) { 
	            //有数据读入
	            buf.flip(); //准备拷贝数据
	            callSocket(buf,i);
	            //buf.compact(); 
	            buf.clear();//重置buffer
	            try
	            {
	              
	               socket.read(buf, buf, this); //准备下一次读取，将数据读入buf
	            }
	            catch(Exception ex)
	            {
	                ex.printStackTrace();
	            }
	        } else if (i == -1) { 
	            try { 
	                System.out.println("客户端断线:" + socket.getRemoteAddress().toString()); 
	                
	            }
	        catch(Exception ex)
	            {
	        	
	            }
	        }
	    }


		public void failed(Throwable exc, ByteBuffer attachment) {
			exc.printStackTrace();
			attachment.clear();
			attachment=null;

		}
}