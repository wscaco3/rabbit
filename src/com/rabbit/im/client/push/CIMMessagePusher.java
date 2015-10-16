 
package com.rabbit.im.client.push;

import com.rabbit.im.nio.mutual.Message;

/**
 * 消息发送实接口
 * 
 * @author easy
 */
public interface CIMMessagePusher {

 
 
    /**
     * 向用户发送消息
     * @param msg
     */
	public void pushMessageToUser(Message msg);

 
}
