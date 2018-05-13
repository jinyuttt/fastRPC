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


import java.util.LinkedList;
import java.util.List;
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
    
 private static List<JYSocket> list=new LinkedList<JYSocket>();
// private static SynchronousQueue<Integer> queue=new SynchronousQueue<Integer>();
// private static AtomicInteger  id=new AtomicInteger(); 
 private static Thread check=null;
 private static volatile boolean isInit=false;
    public void recviceData(byte[] data) {
        
    }
    public void recviceData(JYSocket recvicer) {
        System.out.println("收到请求："+recvicer.srvIP);
        RequestHander hander=new RequestHander();
        if(recvicer.data==null&&recvicer.isTcpType)
        {
            recvicer.data=recvicer.socket.recviceData();
            list.add(recvicer);
            start();
            if(recvicer.data==null||recvicer.data.length==0)
            {
                System.out.println("接受到0字节数据");
            }
        }
        byte[] result=hander.process(recvicer.data);
        recvicer.socket.sendData(result);
        if(!recvicer.isTcpType)
        {
            //tcp类型由客户端关闭
            recvicer.socket.close();
        }
        
    }
    private void start()
    {
        if(isInit)
        {
            return;
        }
       check=new Thread(new Runnable() {

        @Override
        public void run() {
            while(true)
            {
           for(int i=0;i<list.size();i++)
           {
               if(list.get(i).socket.isHavRec())
               {
                   list.get(i).data=null;
                   recviceData(list.get(i));
                   list.remove(i);
               }
               else if(list.get(i).socket.isClose())
               {
                   list.remove(i);
               }
           }
           try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
        }
        }
           
       });
       check.setDaemon(true);
       check.setName("123");
       if(!check.isAlive())
       {
           isInit=true;
          check.start();
       }
    }
}

