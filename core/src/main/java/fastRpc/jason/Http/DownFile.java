/**    
 * 文件名：DownFile.java    
 *    
 * 版本信息：    
 * 日期：2018年3月11日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.Http;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.Socket;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：DownFile    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月11日 下午1:16:57    
 * 修改人：jinyu    
 * 修改时间：2018年3月11日 下午1:16:57    
 * 修改备注：    
 * @version     
 *     
 */
public class DownFile {
    public enum FContentType
    {
        css,
        gif,
        ico,
        jpe,
        jpg,
        jpeg,
        bmp,
        js,
        stm,
        htm,
        html
    }
    private static final String CodeStatus = "200";
    private String ContentType;

    /// <summary>
    /// 根据请求文件的后缀名，确定响应体的类型
    /// </summary>
    /// <param name="ext"></param>
    void GetContentType(String ext)
    {
        FContentType ctype=FContentType.valueOf(FContentType.class, ext.trim().toLowerCase());
        switch (ctype)
        {
            case css:
                this.ContentType = "text/css";
                break;
            case gif:
                this.ContentType = "image/gif";
                break;
            case ico:
                this.ContentType = "image/x-icon";
                break;
            case jpe:
            case jpeg:
            case jpg:
                this.ContentType = "image/jpeg";
                break;
            case bmp:
                this.ContentType = "image/bmp";
                break;
            case js:
                this.ContentType = "application/x-javascript";
                break;
            case stm:
            case htm:
            case html:
                this.ContentType = "text/html";
                break;
          
        }
    }

//    /// <summary>
//    /// 拼接响应报文
//    /// </summary>
//    public byte[] GetResponse()
//    {
//        // 拼接响应报文头
//        StringBuffer sb=new StringBuffer();
//        sb.append("HTTP/1.0 "+this.CodeStatus+" "+"OK"+"\r\n");
//        sb.append("Content-Type: "+this.ContentType+"\r\n");
//       // sb.Append("Content-Length: "+this.ContentLength+"\r\n");
//        sb.append("Server: ghhSever/1.0\r\n");
//        sb.append("X-Powered-By: MannyGuo\r\n");// 大家可以模拟下面的响应报文进行添加，注意格式必须要一致(末尾换行)
//        sb.append("\r\n");
//      
//        bList.AddRange(header);
//        bList.AddRange(content);
//
//        return bList.ToArray();
//    }
    void responce(String fileName, Socket socket)
    {
        try {  
            PrintStream out = new PrintStream(socket.getOutputStream(), true);  
            File fileToSend = new File(fileName);  
            if (fileToSend.exists() && !fileToSend.isDirectory()) {  
                out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答  
                out.println("Content-Type:"+this.ContentType);  
               // out.println("Content-Length: " + fileToSend.length());// 返回内容字节数  
                out.println();// 根据 HTTP 协议, 空行将结束头信息  
                FileInputStream fis = new FileInputStream(fileToSend);  
                byte data[] = new byte[fis.available()];  
                fis.read(data);  
                out.write(data);
                out.close();  
                fis.close();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    /** 
     * 读取一个图像文件的内容并返回给浏览器端. 
     * @param fileName 文件名 
     * @param socket 客户端 socket. 
     */  
     void imgDownload(String fileName, Socket socket) {  
   
         try {  
             PrintStream out = new PrintStream(socket.getOutputStream(), true);  
             File fileToSend = new File(fileName);  
             if (fileToSend.exists() && !fileToSend.isDirectory()) {  
                 out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答  
                 out.println("Content-Type: application/octet-stream");  
                 out.println("Content-Length: " + fileToSend.length());// 返回内容字节数  
                 out.println();// 根据 HTTP 协议, 空行将结束头信息  
   
                 FileInputStream fis = new FileInputStream(fileToSend);  
                 byte data[] = new byte[fis.available()];  
                 fis.read(data);  
                 out.write(data);  
                 //文件下载完后关闭socket流，但socket还没有关闭  
                 out.close();  
                 fis.close();  
             }  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
     }  
   
     /** 
     * 读取一个文件的内容并返回给浏览器端. 
     * @param fileName 文件名 
     * @param socket 客户端 socket. 
     */  
     void fileDownload(String fileName, Socket socket) {  
         try {  
             PrintStream out = new PrintStream(socket.getOutputStream(), true);  
             File fileToSend = new File(fileName);  
             if (fileToSend.exists() && !fileToSend.isDirectory()) {  
                 out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答  
                 out.println("Content-Type: application/octet-stream;charset=utf-8" );  
                 /* Content-Disposition不是标准参数，查看一下HTTP/1.1的规范文档，对于这个参数的解释大意如下： 
                  * Content-Disposition参数本来是为了在客户端另存文件时提供一个建议的文件名，但是考虑到安全的原因， 
                  * 就从规范中去掉了这个参数。但是由于很多浏览器已经能够支持这个参数，所以只是在规范文档中列出，但是要 
                  * 注意这个不是HTTP/1.1的标准参数。其值为“attachment”，那么无论这个文件是何类型，浏览器都会提示我 
                  * 们下载此文件，因为此时它认为后面的消息体是一个“附件”，不需要由浏览器来处理了。 
                  */  
                 out.println("Content-Disposition: attachment;filename=测试下载文件.txt");  
                 //              out.println("Accept-Ranges: bytes");  
                 out.println("Content-Length: " + fileToSend.length());// 返回内容字节数  
                 out.println();// 根据 HTTP 协议, 空行将结束头信息  
   
                 FileInputStream fis = new FileInputStream(fileToSend);  
                 byte[] tmpByteArr = new byte[10];//这里为了测试看下载进度条，所以设置小点  
                 while (fis.available() > 0) {  
                     int readCount = fis.read(tmpByteArr);  
                     out.write(tmpByteArr, 0, readCount);  
                 }  
   
                 //文件下载完后关闭socket流  
                 out.close();  
                 fis.close();  
             }  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
     }  
}
