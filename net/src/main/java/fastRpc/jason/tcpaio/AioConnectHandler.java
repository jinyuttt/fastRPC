/**    
 * 文件名：AioConnectHandler.java    
 *    
 * 版本信息：    
 * 日期：2018年5月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.tcpaio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**    
 *     
 * 项目名称：net    
 * 类名称：AioConnectHandler    
 * 类描述：    客户端异步连接回调
 *         对于一般应用没有什么
 *         除非是要建立连接后自动发送或者接收数据
 *         处理自动调用模式
 * 创建人：jinyu    
 * 创建时间：2018年5月19日 下午3:01:44    
 * 修改人：jinyu    
 * 修改时间：2018年5月19日 下午3:01:44    
 * 修改备注：    
 * @version     
 *     
 */
public class AioConnectHandler implements CompletionHandler<Void,AsynchronousSocketChannel> {
    private int bufSize=1024*1024;
    private AioTcpClient instance=null;
    
    /**
     * 为封装提供对象，正常没有
     *    
     *    
     * @param client
     */
    public AioConnectHandler(AioTcpClient client)
    {
        this.instance=client;
    }
    
    /**
     * 
     * 创建一个新的实例 AioConnectHandler.    
     *
     */
    public AioConnectHandler()
    {
        
    }
    @Override
    public void completed(Void result, AsynchronousSocketChannel connector) {
        //连接后直接开启读取
        //startRead(connector);   
        instance.sucessCon();
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        exc.printStackTrace();  
        try {
            attachment.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
  //这不是 CompletionHandler接口的方法。  
    public void startRead(AsynchronousSocketChannel socket) {   
        ByteBuffer clientBuffer = ByteBuffer.allocate(bufSize);   
        //read的原型是  
       // read(ByteBuffer dst, A attachment,  
        //    CompletionHandler<Integer,? super A> handler)   
       // A的型是实际调用read的第二个参数，即clientBuffer，这里只是为了通用封装，可以使用其它类型
         
        AioReadHandler hander=new AioReadHandler(socket);
        hander.setClient(instance);
        socket.read(clientBuffer, clientBuffer, hander);   
        try {
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
    }  
  //不是CompletionHandler的方法  
    //我的封装不需要调用
    public void write(AsynchronousSocketChannel socket) {  
       // String sendString="服务器回应,你输出的是:"+msg;  
        byte[]data=null;
        ByteBuffer clientBuffer=ByteBuffer.wrap(data);          
        socket.write(clientBuffer, clientBuffer, new AioSendHandler(socket));  
    } 
}
