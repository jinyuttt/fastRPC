/**    
 * 文件名：Client.java    
 *    
 * 版本信息：    
 * 日期：2018年5月19日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import fastRpc.jason.tcpaio.AioTcpClient;

/**    
 *     
 * 项目名称：net    
 * 类名称：Client    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年5月19日 下午7:41:01    
 * 修改人：jinyu    
 * 修改时间：2018年5月19日 下午7:41:01    
 * 修改备注：    
 * @version     
 *     
 */
public class Client {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        AioTcpClient client=new AioTcpClient();
        client.connect("127.0.0.1", 5555);
        Random random1 = new Random(System.currentTimeMillis());
        String flage=String.valueOf(random1.nextInt(100));
        while(true)
        {
           byte[] data = null;
        try {
            
            data = ("ty_"+flage+"_"+String.valueOf(System.currentTimeMillis())).getBytes("utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
           System.out.println(new String(data));
           client.sendData(data);
           byte[]rec=client.recDirect();
           if(rec!=null)
           {
               System.out.println(new String(rec));
           }
           try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }

    }

}
