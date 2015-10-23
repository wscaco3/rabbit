 
package com.rabbit.im.nio.handler;

import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;
 
public interface IMRequestHandler {

	public abstract ReplyBody process(IMSession session, SentBody message);
}