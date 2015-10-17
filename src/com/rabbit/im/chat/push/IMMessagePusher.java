 
package com.rabbit.im.chat.push;

import com.rabbit.im.nio.mutual.Message;

/**
 * 消息发送实接口
 * 
 * @author easy
 */
public interface IMMessagePusher {

 
 
    /**
     * 向用户发送消息
     * @param msg
     */
	public void pushMessageToUser(Message msg);

 
}
