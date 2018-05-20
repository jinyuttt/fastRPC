/**
 * 
 */
package fastRpc.jason.tcpaio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import fastRpc.jason.inet.INetClient;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;
import fastRpc.jason.net.NetType;


/**
 * 
 *     
 * 项目名称：net    
 * 类名称：AioTcpClient    
 * 类描述：     客户端通信封装，顶层封装
 *         在建立连接时开启读取
 * 创建人：jinyu    
 * 创建时间：2018年5月20日 上午11:01:37    
 * 修改人：jinyu    
 * 修改时间：2018年5月20日 上午11:01:37    
 * 修改备注：    
 * @version     
 *
 */
@NetType("aiotcp_Client")
public class AioTcpClient implements INetClient{
	    private AsynchronousSocketChannel socket=null;  
	    private String srcIP="";
	    private int  srcPort=0;
	    private String localIP="";
	    private int localPort=0;
	    private IRecvieHander hander=null;//数据接收回调
	    private int recSize=128*1024;//socket缓存
	    private int bufSize=1024*1024;
	    private LinkedBlockingQueue<JYSocket> queue=new LinkedBlockingQueue<JYSocket>();//数据队列
	    private  AsynchronousChannelGroup channelGroup = null;//线程
	    private boolean isClose=false;//是否关闭
	    private boolean isConnect=false;//是否连接
	    private boolean connecting=false;//正在连接
	    private AioSendHandler sendHandler=null;//发送回调
	    private AioReadHandler readHander=null;
	    //写队列，因为当前一个异步写调用还没完成之前，调用异步写会抛WritePendingException  
	    //所以需要一个写队列来缓存要写入的数据，这是AIO比较坑的地方  
	    private final Queue<ByteBuffer> writequeue = new LinkedList<ByteBuffer>();  
	    private boolean writing = false; //当前是否正在写入
        private volatile boolean isRead=false;//是否已经开启异步读取
        private boolean isAsyncon=false;//是否使用异步连接;
        private int   sendNum=0;//异步连接发送数据时，没有连接上缓存的数据量；
       // private  static Executor recData=  Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),Executors.defaultThreadFactory());
       /**
        * 当需要客户端使用时开启
        */
       private void open()
       {
           try {
               channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
           //在默认channel group下创建一个socket channel  
                socket = AsynchronousSocketChannel.open(channelGroup);  
           //设置Socket选项  
                socket.setOption(StandardSocketOptions.TCP_NODELAY, true);  
                socket.setOption(StandardSocketOptions.SO_KEEPALIVE, true);  
                socket.setOption(StandardSocketOptions.SO_REUSEADDR, true);  
                socket.setOption(StandardSocketOptions.SO_RCVBUF, recSize);
                socket.setOption(StandardSocketOptions.SO_SNDBUF, recSize);
           } catch (IOException e) {
               e.printStackTrace();
           } 
       }
       
       
       /**
        * 如果需要读取数据则开启
        */
       private void  read()
       {
           //已经开启
           if(isRead)
           {
               return;
           }
           //read的原型是  
           //read(ByteBuffer dst, A attachment,  
           //    CompletionHandler<Integer,? super A> handler)   
           //A的型是实际调用read的第二个参数，即clientBuffer，这里只是为了通用封装，可以使用其它类型
           isRead=true;
           System.out.println("读取："+Thread.currentThread().getId());
           try
           {
             ByteBuffer clientBuffer = ByteBuffer.allocate(bufSize); 
             readHander=new AioReadHandler(socket);
             readHander.setClient(this);
             socket.read(clientBuffer, clientBuffer, readHander);
           }
           catch(Exception ex)
           {
               ex.printStackTrace();
           }
          // recData.execute(new Runnable() {

//            @Override
//            public void run() {
//              
//                System.out.println("读取："+Thread.currentThread().getId());
//                try
//                {
//                  ByteBuffer clientBuffer = ByteBuffer.allocate(bufSize); 
//                  readHander=new AioReadHandler(socket);
//                  socket.read(clientBuffer, clientBuffer, readHander);
//                }
//                catch(Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//               
//           });
           
       }
       
       /**
        * 连接成功
        */
       public void sucessCon()
       {
           isConnect=true;
           connecting=false;
       }
       
  /**
   * 为了适应接口封装添加
   * @param socket
   */
  public void addSocket(JYSocket socket)
  {
      queue.offer(socket);
  }
  
  /**
   * 为了适应接口封装添加
   * @return
   */
  public IRecvieHander getHander()
  {
      return hander;
  }
  
  /**
   * 绑定本地端口
   */
  private   void bind()
  {
      if(!localIP.isEmpty()||localPort!=0)
      {
          InetSocketAddress bindpoint=null;
          if(localIP.isEmpty())
          {
              bindpoint = new InetSocketAddress(localPort);
          }
          else
          {
             bindpoint = new InetSocketAddress(localIP, localPort);
          }
          try {
              socket.bind(bindpoint);
          } catch (IOException e) {
          
              e.printStackTrace();
          }
      }
  }
  
  /**
   * 开启异步连接
   */
  public void setAsyncon()
  {
      this.isAsyncon=true;
  }
   
    @Override
    public void setLocalIP(String ip) {
        this.localIP=ip;
        
    }
    @Override
    public void setLocalPort(int port) {
        this.localPort=port;
    }
    @Override
    public void setRemoteIP(String ip) {
       this.srcIP=ip;
        
    }
    @Override
    public void setRemotePort(int port) {
       this.srcPort=port;
        
    }
    @Override
    public void setClient(Object client) {
       this.socket=(AsynchronousSocketChannel) client;
        
    }
    @Override
    public boolean connect(String remoteIP, int remotePort) {
        open();//需要连接时再开启;
        bind();
       
        if(this.isAsyncon)
        {
            connecting=true;
            socket.connect(new InetSocketAddress(remoteIP, remotePort),socket,new AioConnectHandler(this));
        }
        else
        {
           Future<Void> r = socket.connect(new InetSocketAddress(remoteIP, remotePort));
           
           try {
            r.get(1, TimeUnit.MINUTES);
            this.isConnect=true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        }
        return true;
    }
    @Override
    public boolean connect() {
       return  connect(this.srcIP, this.srcPort);
    }
    
    /**
     * 从发送队列中取出数据
     */
    public void writeFromQueue() {  
        ByteBuffer buffer = null;
        synchronized (writequeue) { 
            if(isConnect)
            {
              buffer = writequeue.poll();  
              if (buffer == null) {
                //没有数据则不发送数据了
                writing = false;  
              } 
            }
            else
            {
                writing = false;
                if(writequeue.size()>sendNum&&isAsyncon)
                {
                    writequeue.clear();
                }
            }
        }  
        // No new data in buffer to write  
        if (writing) {  
            sendQueue(buffer);
        }  
    }
    
    /**
     * 将数据放入发送队列
     * @param buffer
     */
    private void writeMessage(final ByteBuffer buffer) {  
        boolean threadShouldWrite = false;  
        synchronized(writequeue) {  
            writequeue.add(buffer); 
            if (!writing) {  
                //如果当前已经没有写入数据
                //则让数据准备写入
                writing = true;  
                threadShouldWrite = true;  //需要开启发送
            }  
        }
        if (threadShouldWrite) {  
              writeFromQueue();  
        }  
    } 
    
    /**
     * 真实发送数据
     * 一定是单线程的
     * @param buffer
     */
    private void sendQueue(ByteBuffer buffer)
    {
        if(sendHandler==null)
        {
            sendHandler=new AioSendHandler(socket);
            sendHandler.setClient(this);
        }
        socket.write(buffer, buffer, sendHandler);
    }
    @Override
    public void sendData(byte[] data) {
        ByteBuffer  buf = ByteBuffer.wrap(data);
        writeMessage(buf);
    }
    @Override
    public void sendData(String ip, int port, byte[] data) {
       sendData(data);
        
    }
    @Override
    public byte[] recviceData() {
        try {
            return queue.take().data;
        } catch (InterruptedException e) {
         
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public byte[] recDirect() {
       read();
     if(queue.isEmpty())
     {
         return null;
     }
     else
     {
         try {
             return queue.take().data;
        } catch (InterruptedException e) {
           
            e.printStackTrace();
        }
     }
    return null;
        
    }
    @Override
    public JYSocket recvice() {
      read();
      try {
        return queue.take();
    } catch (InterruptedException e) {
    
        e.printStackTrace();
    }
    return null;
    }
    @Override
    public void startRecvice(IRecvieHander hander) {
       this.hander=hander;
       read();
    }
    @Override
    public void setBufferSize(int size) {
       this.recSize=size;
        
    }
    @Override
    public void close() {
        try {
            isClose=true;
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      
        if(channelGroup !=null){  
            channelGroup.shutdown();  
        } 
        
    }
    @Override
    public boolean isClose() {
        return isClose;
    }
    @Override
    public boolean isHavRec() {
        return !queue.isEmpty();
    }
    @Override
    public boolean isConnected() {
      return isConnect||connecting;
    }
}
