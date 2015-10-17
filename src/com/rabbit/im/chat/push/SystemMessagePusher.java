 
package com.rabbit.im.chat.push;

import com.rabbit.im.nio.mutual.Message;




/**
 * 
 * @author easy
 */
public class SystemMessagePusher  extends DefaultMessagePusher{


 
	/**
	 * Constructor.
	 */
	public SystemMessagePusher() {
		super();
	}
	
	@Override
	public void pushMessageToUser(Message MessageMO){
		
		MessageMO.setSender("system");
		super.pushMessageToUser(MessageMO);
		
	}
}
