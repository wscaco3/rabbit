 
package com.rabbit.im.nio.handler;

import org.apache.log4j.Logger;

import com.rabbit.im.nio.constant.IMConstant;
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;

/**
 *客户端心跳实现
 * 
 * @author
 */
public class HeartbeatHandler implements IMRequestHandler {

	protected final Logger logger = Logger.getLogger(HeartbeatHandler.class);

	public ReplyBody process(IMSession session, SentBody message) {

		logger.debug("heartbeat... from "+session.getRemoteAddress().toString());
		ReplyBody reply = new ReplyBody();
		reply.setKey(IMConstant.RequestKey.CLIENT_HEARTBEAT);
		reply.setCode(IMConstant.ReturnCode.CODE_200);
		session.setHeartbeat(System.currentTimeMillis());
		return reply;
	}
	
 
	
}