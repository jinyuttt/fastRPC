package fastRpc.jason.tcpaio;



import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * 
 */


import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import fastRpc.jason.inet.JYSocket;

/**
 * @author jinyu
 *
 */

//这里的参数受实际调用它的函数决定。本例是服务端socket.accetp调用决定  
public class AioAcceptHandler implements CompletionHandler<AsynchronousSocketChannel , AsynchronousServerSocketChannel>{

   private  AioTcpServer  instance=null;
   private String localIP="";
   private int localPort;
  // private int bufSize=1024*1024;
   public AioAcceptHandler(AioTcpServer obj)
{
    this.instance=obj;
}
// //不是CompletionHandler的方法  
//   /**
//    * 读取客户端数据
//    * @param socket
//    */
//   private void startRead(AsynchronousSocketChannel socket) { 
//          ByteBuffer clientBuffer = ByteBuffer.allocate(1024); 
//          socket.read(clientBuffer, clientBuffer, new AioReadHandler(socket)); 
//          try { 
//              
//          } catch (Exception e) { 
//              e.printStackTrace(); 
//          } 
//      } 
//  
//  //不是CompletionHandler的方法  
//   
//   /**
//    * 发送数据
//    * @param socket
//    */
//      private void write(AsynchronousSocketChannel socket) {  
//         // String sendString="服务器回应,你输出的是:"+msg;  
//          byte[]data=null;
//          ByteBuffer clientBuffer=ByteBuffer.wrap(data);          
//          socket.write(clientBuffer, clientBuffer, new AioSendHandler(socket));  
//      }
      
     
   /**
    * 组织封装通信接口
    * @param client
    */
   public void callSocket(AsynchronousSocketChannel client)
   {
       AioTcpClient curClient=new AioTcpClient();
       curClient.setClient(client);
       JYSocket socket=new JYSocket();
       socket.localIP=localIP;
       socket.localPort=localPort;
       try
       {
       InetSocketAddress addr=(InetSocketAddress)client.getRemoteAddress();
       socket.srvIP=addr.getHostString();
       socket.srvPort=addr.getPort();
       }
       catch(Exception ex)
       {
           
       }
      
       socket.socket=curClient;
       socket.isTcpType=true;
       curClient.sucessCon();//才可能发送数据
       //
       if(instance.getHander()!=null)
       {
           instance.getHander().recviceData(socket);
       }
       else
       {
           instance.addClientSocket(socket);
       }
      
       //与一般TCP通信一样，将通信socket封装返回，同时要读取数据
       //开启socket读取，直接使用回调读取该socket数据
//       AioReadHandler handler=new AioReadHandler(client);
//       handler.setClient(curClient);//将客户端传递给AioReadHandler,用于数据返回
//       ByteBuffer clientBuffer = ByteBuffer.allocate(bufSize);
//       client.read(clientBuffer, clientBuffer, handler);//读取数据
   }
	public void completed(AsynchronousSocketChannel socket, AsynchronousServerSocketChannel attachment) {
		if(localIP.isEmpty()||localPort==0)
           {
		    try
		    {
		      InetSocketAddress addr=(InetSocketAddress) attachment.getLocalAddress();
              localIP=addr.getHostString();
              localPort=addr.getPort();
		    }
		    catch(Exception ex)
		    {
		        
		    }
           }
		  System.out.println("acepption");
		  //准备下一个接收
		  //
		//先安排处理下一个连接请求，异步非阻塞调用，所以不用担心挂住了  
          //这里传入this是个地雷，小心多线程 
         attachment.accept(attachment, this); 
         //将通信接口封装返回
         callSocket(socket);
         //这里不读取数据，而是将结果按照接口返回 
          //  startRead(socket); 
        
		
	}

	 @Override
     public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
         exc.printStackTrace();
         if(attachment!=null)
         {
             try {
                attachment.close();
            } catch (IOException e) {
             
                e.printStackTrace();
            }
         }
     } 
	

       
	

}
