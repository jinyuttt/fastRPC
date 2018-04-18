/**    
 * 文件名：ReadFile.java    
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**    
 *     
 * 项目名称：RPCCore    
 * 类名称：ReadFile    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年3月11日 上午11:55:25    
 * 修改人：jinyu    
 * 修改时间：2018年3月11日 上午11:55:25    
 * 修改备注：    
 * @version     
 *     
 */
public class ReadFile {
    public static String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }
}
