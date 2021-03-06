package com.rabbit.im.nio.filter;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.log.Log;
import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.mutual.SentBody;
/**
 *  服务端接收消息解码，可在此解密消息
 *  @author easy
 *
 */
public class ServerMessageDecoder extends CumulativeProtocolDecoder {

    private final Log log = Log.getLog(getClass());
	private final Charset charset = Charset.forName("UTF-8");
    private IoBuffer buff = IoBuffer.allocate(320).setAutoExpand(true);
	@Override
	public boolean doDecode(IoSession iosession, IoBuffer iobuffer, ProtocolDecoderOutput out) throws Exception {
		boolean complete = false;
		while (iobuffer.hasRemaining()) {
            byte b = iobuffer.get();
            /**
			 * IMConstant.MESSAGE_SEPARATE 为消息界限
			 * 当一次收到多个消息时，以此分隔解析多个消息
			 */
            if (b == IMConstant.MESSAGE_SEPARATE ) {            	
            	complete = true;
                break;
            }else if(b == '\0'){	//flex客户端 安全策略验证时会收到<policy-file- request/>\0的消息，忽略此消息内容
            	complete = true;
                break;
            }
            else {
                buff.put(b);
            }
        }
		if (complete) {
			buff.flip();
	        byte[] bytes = new byte[buff.limit()];
	        buff.get(bytes);
	        
	        String message = new String(bytes, charset);
	        log.debug("ServerMessageDecoder:" + message);
	        buff.clear();
	        try{	        	 
				SentBody body = new SentBody();
		    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		        DocumentBuilder builder = factory.newDocumentBuilder();  
		        Document doc = builder.parse(new ByteArrayInputStream(bytes));
		        body.setKey(doc.getElementsByTagName("key").item(0).getTextContent());
				
				NodeList dataNodeList = doc.getElementsByTagName("data");
				if(dataNodeList!=null && dataNodeList.getLength()>0){
					NodeList items = dataNodeList.item(0).getChildNodes();  
					for (int i = 0; i < items.getLength(); i++) {  
						Node node = items.item(i);  
						body.getData().put(node.getNodeName(), node.getTextContent());
					}
				}
		        
		        out.write(body);
	        }catch(Exception e){
	        	log.warn(e.getMessage());
	        	out.write(message);
	        }
		}
	    return complete;
	}

}
