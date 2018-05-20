/**
 * 
 */
package fastRpc.jason.tcpaio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * 
 *     
 * 项目名称：net    
 * 类名称：AioSendHandler    
 * 类描述：    发送数据回调
 *         需要传入顶层封装
 *         通知顶层发送数据
 * 创建人：jinyu    
 * 创建时间：2018年5月20日 上午11:00:01    
 * 修改人：jinyu    
 * 修改时间：2018年5月20日 上午11:00:01    
 * 修改备注：    
 * @version     
 *
 */
public class AioSendHandler implements CompletionHandler<Integer, ByteBuffer>{
	 private AsynchronousSocketChannel socket; 
	 private AioTcpClient client=null;
	 public AioSendHandler(AsynchronousSocketChannel socket) { 
	        this.socket = socket; 
	    } 
	 
	     /**
	      * 将顶层封装传入
	      * 用于回调写入队列
	      * @param instance
	      */
	     public void setClient(AioTcpClient instance)
	     {
	         this.client=instance;
	     }
	     
	 public void cancelled(ByteBuffer attachment) { 
	        System.out.println("cancelled"); 
	    } 
	public void completed(Integer i, ByteBuffer buf) {
		 if (i > 0) { 
		     if(buf.hasRemaining())
		     {
		         if(socket!=null)
		        socket.write(buf, buf, this); 
		     }
		     else
		     {
		         if(client!=null)
		            client.writeFromQueue();//回调顶层发送数据；
		     }
	        } else if (i == -1) { 
	            try { 
	                
	                System.out.println("客户端断线:" + socket.getRemoteAddress().toString()); 
	                //其实这个socket与client对象的socket是同一个
	                //没有统一知识为了演示通用性
	               if(socket!=null)
	               {
	                   socket.shutdownInput();
	                   socket.shutdownOutput();
	                   socket.close();
	               }
	                if(client!=null)
	                {
	                    client.close();
	                }
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
