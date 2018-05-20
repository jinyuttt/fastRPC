/**    
 * 文件名：NetRecvicer.java    
 *    
 * 版本信息：    
 * 日期：2018年2月27日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.IRPCNet;


import fastRpc.jason.core.RequestHander;
import fastRpc.jason.inet.IRecvieHander;
import fastRpc.jason.inet.JYSocket;

/**    
 *     
 * 项目名称：RPCNet    
 * 类名称：NetRecvicer    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年2月27日 上午12:54:55    
 * 修改人：jinyu    
 * 修改时间：2018年2月27日 上午12:54:55    
 * 修改备注：    
 * @version     
 *     
 */
public class RPCRecvicer implements IRecvieHander {
    
 //private static List<JYSocket> list=new LinkedList<JYSocket>();
 //private static AtomicInteger  id=new AtomicInteger(0); 
 //private static final  int maxSize=5;
 //private static Thread check=null;
 //private static volatile boolean isInit=false;
 //private static  final long idleTime=60*10000;//没有线程需要监测的时间
 //private static  final long waitidleTime=30*10000;//每个线程空闲时间
 //private static  final int   threadSize=100;//每个线程承担的最小数据量
 //private long  startThreadTime=System.currentTimeMillis();
 private ClientRecvicer clientRec=new ClientRecvicer();
    public void recviceData(byte[] data) {
        
    }
    public void recviceData(JYSocket recvicer) {
        System.out.println("收到请求："+recvicer.srvIP);
        if(recvicer.data==null&&recvicer.isTcpType)
        {
//            if(recvicer.socket.isHavRec())
//            {
//                //有数据才读取
//                recvicer.data=recvicer.socket.recviceData();
//            }
//            list.add(recvicer);
//            
//            startProxyCheck();
//            if(recvicer.data==null||recvicer.data.length==0)
//            {
//                System.out.println("接受到0字节数据");
//                return;
//            }
            //注册回调
             clientRec.recvicer=this;
             //注册数据接收
             recvicer.socket.startRecvice(clientRec);
            return;
        }
        RequestHander hander=new RequestHander();
        byte[] result=hander.process(recvicer.data);
        recvicer.socket.sendData(result);
        if(!recvicer.isTcpType)
        {
            //tcp类型由客户端关闭
            recvicer.socket.close();
        }
        
    }
    
    
    /**
//     * 开启判断
//     */
//    private void startProxyCheck()
//    {
//        if(isInit)
//        {
//            return;
//        }
//        isInit=true;
//       check=new Thread(new Runnable() {
//        @Override
//        public void run() {
//            int num=1;
//            while(true)
//            {
//              startThread();
//              if(System.currentTimeMillis()-startThreadTime>idleTime)
//              {
//                  //1分钟内没有监测开启线程，增加判断线程的时间
//                  num++;
//              }
//              else
//              {
//                  num=1;
//              }
//              if(num>10)
//              {
//                  //10分钟内没有监测，则关闭一次判断线程，等待开启
//                  break;
//              }
//             try {
//            Thread.sleep(num*500);
//            } catch (InterruptedException e) {
//            
//            e.printStackTrace();
//        }
//             
//        }
//            isInit=false;
//        }
//           
//       });
//       check.setDaemon(true);
//       check.setName("ProxyCheck");
//       if(!check.isAlive())
//       {
//           isInit=true;
//           check.start();
//       }
//    }
//    
//    /**
//     * 判断是否需要启动新线程
//     */
//    private void startThread()
//    {
//       
//       if(id.get()==0)
//       {
//           startCheck();
//       }
//       if(list.size()/threadSize>id.get()&&id.get()<maxSize)
//       {
//           //小于于maxSize不断启动
//           startCheck();
//           startThreadTime=System.currentTimeMillis();
//           System.out.println("111");
//       }
//    }
//    
//    /**
//     * 启动监测线程
//     */
//    private void startCheck()
//    {
//      
//       Thread checkList=new Thread(new Runnable() {
//        @Override
//        public void run() {
//            int curid=id.incrementAndGet();
//            long curCheckTime=System.currentTimeMillis();
//            int numid=1;
//            while(true)
//            {
//               
//                int curMax=id.get();//id,从1开始
//                int num=list.size()/maxSize;
//                int index=(curid-1)*num;//id与索引差1
//                int max=index+num;
//                if(curid==curMax)
//                {
//                    //如果说是最后一个线程，则最后的部分也交给它
//                    max=max+list.size()%maxSize;
//                }
//                //
//                if(index>=max)
//                {
//                    numid=num++%10;
//                }
//                else
//                {
//                    numid=1;
//                    curCheckTime=System.currentTimeMillis();
//                }
//                 for(int i=index;i<max;i++)
//                  {
//                     try
//                     {
//                     JYSocket curSocket=list.get(i);
//                     //删除时会造成数据索引移动，所以要锁定
//                     synchronized(curSocket)
//                     {
//                        if(curSocket.socket.isHavRec())
//                          {
//                       curSocket.data=null;
//                    //先执行，再删除，删除会造成数据移动
//                   //已经执行了的数据向前移动，因为没有了数据，向前移动重复使用没有关系
//                    list.remove(i);
//                    i--;
//                    max--;
//                    recviceData(curSocket);
////                    StringBuilder sbr=new StringBuilder();
////                    sbr.append("curid:"+curid);
////                    sbr.append(",");
////                    sbr.append("id:"+curMax);
////                    sbr.append(",");
////                    sbr.append("size:"+list.size());
////                    System.out.println(sbr.toString());
//                        }
//                        else if(curSocket.socket.isClose())
//                        {
//                          list.remove(i);
//                        }
//                     }
//                     }
//                     catch(Exception ex)
//                     {
//                         //只能索引问题，
//                     }
//                 }
//                
//                 if(System.currentTimeMillis()-curCheckTime>waitidleTime)
//                 {
//                     break;
//                 }
//                 if(curid>id.get())
//                 {
//                     //说明前面先启动的线程有关闭，则立即停止后面的线程，从新启动分配数据
//                     //侧面说明当前的线程已经足够了，不需要后面的线程了
//                     break;
//                 }
//                 
//           try {
//            Thread.sleep(numid*500);
//        } catch (InterruptedException e) {
//            
//            e.printStackTrace();
//        }
//        }
//            id.decrementAndGet();
//        }
//           
//       });
//       checkList.setDaemon(true);
//       checkList.setName("check"+String.valueOf(id.get()));
//       if(!checkList.isAlive())
//       {
//           checkList.start();
//       }
//    }
//

}

