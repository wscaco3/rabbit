/**
 * probject:cim
 * @version 1.1.0
 * 
 * @author easy
 */  
package com.rabbit.im.chat.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.handler.IMRequestHandler;
import com.rabbit.im.nio.mutual.Message;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;
/**
 * 推送离线消息
 */
public class PushOfflineMessageHandler implements IMRequestHandler {

	protected final Logger logger = Logger
			.getLogger(PushOfflineMessageHandler.class);

	public ReplyBody process(IMSession ios, SentBody message) {

		ReplyBody reply = new ReplyBody();
		reply.setCode(IMConstant.ReturnCode.CODE_200);
		try {
			String account = message.get("account");
			//获取到存储的离线消息
			//List<Message> list = messageService.queryOffLineMessages(account);
			List<Message> list = new ArrayList<Message>();
			for (Message m : list) {
				
				ios.write(m);
			}
 
		} catch (Exception e) {
			reply.setCode(IMConstant.ReturnCode.CODE_500);
			e.printStackTrace();
			logger.error("拉取离线消息失败", e);
		}
		return reply;
	}
}