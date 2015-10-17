package com.rabbit.im.nio.filter;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.rabbit.im.nio.constant.IMConstant;

/**
 * 服务端发送消息前编码，可在此加密消息
 *  @author easy
 *
 */
public class ServerMessageEncoder extends ProtocolEncoderAdapter {


	@Override
	public void encode(IoSession iosession, Object message, ProtocolEncoderOutput out) throws Exception {

		
		IoBuffer buff = IoBuffer.allocate(320).setAutoExpand(true);
		buff.put(message.toString().getBytes("UTF-8"));
	    buff.put(IMConstant.MESSAGE_SEPARATE);
	    buff.flip();
		out.write(buff);
	}
	
	 

}
