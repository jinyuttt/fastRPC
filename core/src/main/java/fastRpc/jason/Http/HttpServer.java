/**    
 * 文件名：HttpServer.java    
 *    
 * 版本信息：    
 * 日期：2018年3月11日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import fastRpc.jason.JSONFomat.JsonPro;
import fastRpc.jason.core.RPCServer;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：HttpServer    
 * 类描述：    http服务端信息
 * 创建人：jinyu    
 * 创建时间：2018年3月11日 上午9:48:46    
 * 修改人：jinyu    
 * 修改时间：2018年3月11日 上午9:48:46    
 * 修改备注：    
 * @version     
 *     
 */
public class HttpServer {
    public static int port=7777;
    
   
    /**
     * 
     * @Title: init   
     * @Description: 读取配置        
     * void      
     * @throws
     */
private void init(String conf)
{
    Properties properties = new Properties();
    FileInputStream in = null;
    try {
        
        File config=new File(conf);
        if(config.isFile()&&config.exists())
        {
         in = new FileInputStream(conf);
         properties.load(in);
         in.close();
        }
     
    } catch (Exception e1) {
        e1.printStackTrace();
    }
     String srcport= properties.getProperty("server.port","7777");
     port=Integer.valueOf(srcport);
}

/**
 * 读取配置文件
 * @param conf 配置文件
 */
public void UtilInit(String conf)
{
    init(conf);
    
}
/**
 * 
 * @Title: start   
 * @Description: 启动服务       
 * void      
 * @throws
 */
public void start()
{
    System.out.println("端口："+port);
    Thread recRequest=new Thread(new Runnable() {
     public void run() {
            ServerSocket ser = null;
            try {
                ser = new ServerSocket(port);
            } catch (IOException e1) {
              
                e1.printStackTrace();
            }
            try
            {
            while(true)
            {
                Socket socket=ser.accept();
                BufferedReader bd=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                /**
                 * 接受HTTP请求
                 */
                String requestHeader;
                int contentLength=0;
                String condition="";
                while((requestHeader=bd.readLine())!=null&&!requestHeader.isEmpty()){
                    System.out.println(requestHeader);
                    /**
                     * 获得GET参数
                     */
                    if(requestHeader.startsWith("GET")){
                        int begin = requestHeader.indexOf("/?")+2;
                        int end = requestHeader.indexOf("HTTP/");
                         condition=requestHeader.substring(begin, end);
                        System.out.println("GET参数是："+condition);
                    }
                    /**
                     * 获得POST参数
                     * 1.获取请求内容长度
                     */
                    if(requestHeader.startsWith("Content-Length")){
                        int begin=requestHeader.indexOf("Content-Lengh:")+"Content-Length:".length();
                        String postParamterLength=requestHeader.substring(begin).trim();
                        contentLength=Integer.parseInt(postParamterLength);
                        System.out.println("POST参数长度是："+Integer.parseInt(postParamterLength));
                    }
                }
                StringBuffer sb=new StringBuffer();
                if(contentLength>0){
                    for (int i = 0; i < contentLength; i++) {
                        sb.append((char)bd.read());
                    }
                    System.out.println("POST参数是："+sb.toString());
                }
                if(condition.trim().endsWith(".js"))
                {
                     DownFile fi=new DownFile();
                     int index=condition.lastIndexOf("/");
                     String file=condition.substring(index+1);
                     int typel=condition.lastIndexOf(".");
                     String ext=condition.substring(typel+1);
                     fi.GetContentType(ext);
                      fi.responce(file, socket);
                      socket.close();
                    continue;
                }
                 String json=RPCServer.getInstance().getServer();
                 json= JsonPro.jsonFomat(json);
                 PrintWriter pw=new PrintWriter(socket.getOutputStream());
                 pw.println("HTTP/1.1 200 OK\\r\\n");// 返回应答消息,并结束应答
                 pw.println("Content-Type: text/html; charset=utf-8\\r\\n");
                 pw.println();// 根据 HTTP 协议, 空行将结束头信息
                 pw.write(json);
                 pw.flush();
                 socket.close();
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            }
        
    });
    recRequest.setDaemon(true);
    recRequest.setName("recviceHttp");
    recRequest.start();
}

}
