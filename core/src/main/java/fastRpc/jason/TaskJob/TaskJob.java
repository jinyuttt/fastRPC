/**    
 * 文件名：TaskJob.java    
 *    
 * 版本信息：    
 * 日期：2018年5月13日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.TaskJob;

import java.util.concurrent.TimeUnit;

import fastRpc.jason.core.NetProxy.SocketProxy;

import java.util.LinkedList;
import java.util.List;

/**    
 *     
 * 项目名称：core    
 * 类名称：TaskJob    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年5月13日 下午3:19:27    
 * 修改人：jinyu    
 * 修改时间：2018年5月13日 下午3:19:27    
 * 修改备注：    
 * @version     
 *     
 */
public class TaskJob {
    private static final long  liveTime=60*1000;//1分钟
      private static TaskJob instance=null;
      private List<SocketProxy> queue=new LinkedList<SocketProxy>();
      private Thread check=null;
      public static TaskJob getInstance()
      {
          if(instance==null)
          {
              instance=new TaskJob();
          }
          return instance;
      }
      private TaskJob()
      {
          start();
      }
        /**
         * 
         */
        private void start()
        {
             check=new Thread(new Runnable() {

                @Override
                public void run() {
                  try {
                    TimeUnit.SECONDS.sleep(60);
                    for(int i=0;i<queue.size();i++)
                    {
                        SocketProxy proxy= queue.get(i);
                        if(!proxy.isUse&&System.currentTimeMillis()-proxy.freeTime>liveTime)
                        {
                            proxy.close();
                        }
                    }
                } catch (InterruptedException e) {
                  
                    e.printStackTrace();
                }
                    
                }
                
            });
           check.setDaemon(true);
           check.setName("检查");
           if(!check.isAlive())
           {
             check.start();
           }
        }
        
        /**
         * 添加检查代理
         * @param proxy
         */
        public void add(SocketProxy proxy)
        {
            if(!queue.contains(proxy))
            {
                queue.add(proxy);
            }
        }
        public void addList(List<SocketProxy> proxys)
        {
            queue.addAll(proxys);
        }
    }

