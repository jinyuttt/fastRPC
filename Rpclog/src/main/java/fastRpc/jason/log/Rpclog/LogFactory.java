/**    
 * 文件名：LogFactory.java    
 *    
 * 版本信息：    
 * 日期：2018年5月6日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package fastRpc.jason.log.Rpclog;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**    
 *     
 * 项目名称：Rpclog    
 * 类名称：LogFactory    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年5月6日 下午2:28:26    
 * 修改人：jinyu    
 * 修改时间：2018年5月6日 下午2:28:26    
 * 修改备注：    
 * @version     
 *     
 */
public class LogFactory {
    private static LogFactory instance=new LogFactory();
    private final static Logger logger = LoggerFactory.getLogger(LogFactory.class);
    private static HashMap<String,Logger> map=new HashMap<String,Logger>();
public static LogFactory getInstance()
{
     return instance;
}

/**
 * 
 * @param msg
 */
public void debug(Object msg)
{
    logger.debug(msg.toString());
}

/**
 * 
 * @param msg
 */
public void info(Object msg)
{
    logger.info(msg.toString());
}
public void error(Object msg)
{
    logger.error(msg.toString());
}
public void warn(Object msg)
{
    logger.warn(msg.toString());
}
public void debug(String name,Object msg)
{
    Logger log=map.getOrDefault(name, null);
    if(log==null)
    {
        log=LoggerFactory.getLogger(name);
        map.put(name, log);
    }
    log.debug(msg.toString());
}
public void info(String name,Object msg)
{
    Logger log=map.getOrDefault(name, null);
    if(log==null)
    {
        log=LoggerFactory.getLogger(name);
        map.put(name, log);
    }
    log.info(msg.toString());
}
public void error(String name,Object msg)
{
    Logger log=map.getOrDefault(name, null);
    if(log==null)
    {
        log=LoggerFactory.getLogger(name);
        map.put(name, log);
    }
    log.error(msg.toString());
}
public void warn(String name,Object msg)
{
    Logger log=map.getOrDefault(name, null);
    if(log==null)
    {
        log=LoggerFactory.getLogger(name);
        map.put(name, log);
    }
    log.warn(msg.toString());
}
}
