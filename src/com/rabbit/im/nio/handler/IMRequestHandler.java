 
package com.rabbit.im.nio.handler;

/**
 *  请求处理接口,所有的请求实现必须实现此接口
 *  @author easy
 */
import com.rabbit.im.nio.mutual.ReplyBody;
import com.rabbit.im.nio.mutual.SentBody;
import com.rabbit.im.nio.session.IMSession;
 
public interface IMRequestHandler {

	public abstract ReplyBody process(IMSession session, SentBody message);
}