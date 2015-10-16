 
package com.rabbit.controller;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.rabbit.im.nio.mutual.Message;
import com.rabbit.im.client.push.DefaultMessagePusher;
import com.rabbit.im.client.push.SystemMessagePusher;
import com.rabbit.im.client.util.Constants;
import com.jfinal.core.Controller;

/** 
 *
 * @author easy
 */
public class MessageCtrl extends Controller{
	
    public void index(){        
    	HashMap<String,Object> datamap = new HashMap<String,Object>();
    	HashMap<String,String> data = new HashMap<String,String>();		
		try{
			Message message = new Message();
			message.setContent(getPara("content"));
			message.setFile(getPara("file"));
			message.setFileType(getPara("fileType"));
			message.setMid(getPara("mid"));
			message.setReceiver(getPara("receiver"));
			message.setSender(getPara("sender"));
			message.setTimestamp(getParaToLong("timestamp",0l));
			message.setTitle(getPara("title"));
	        message.setType(getPara("type"));
	        checkParams(message);
	        
			if(Constants.MessageType.TYPE_2.equals(message.getType())){
				  //向客户端 发送消息
		        new SystemMessagePusher().pushMessageToUser(message);
			}
			else{
				  //向客户端 发送消息
		        new DefaultMessagePusher().pushMessageToUser(message);
			}
	              
	        data.put("id", message.getMid());
	        data.put("createTime", String.valueOf(message.getTimestamp()));
	        datamap.put("data", data);
		}catch(Exception e){			
			datamap.put("code", 500);
			e.printStackTrace();
		}
        renderJson(datamap);
    }
    
  

    
    /**
     * 文件由客户端发往阿里云 OSS 存储
     * @param messageServiceImpl
     */
   /* private void fileHandler(Message mo, HttpServletRequest request) throws IOException 
    {
    	if(request instanceof MultiPartRequestWrapper){
			MultiPartRequestWrapper pr = (MultiPartRequestWrapper) request;
			if(pr.getFiles("file")!=null)
			{
				File file = pr.getFiles("file")[0];
		         
		        String fileType = request.getParameter("fileType");
		        String dir = dirMap.get(fileType);
		        if(StringUtils.isEmpty(dir))
		        {
		          	  throw new IllegalArgumentException("fileType:" +fileType+" 未定义" );
		          	  
		        }
		        	String path = request.getSession().getServletContext().getRealPath(dir);
		        	String uuid=UUID.randomUUID().toString().replaceAll("-", "");
		 		    File des = new File(path+"/"+uuid);
		 		    FileUtil.copyFile(file, des);
		 		    mo.setFile(dir+"/"+uuid);
		 		    mo.setFileType(fileType);
			}
        }
          
    }*/
    
    
    private void checkParams(Message message){
    	  if(StringUtils.isEmpty(message.getReceiver())){
        	  throw new IllegalArgumentException("receiver 不能为空!");        	  
          }
    }
 
}
