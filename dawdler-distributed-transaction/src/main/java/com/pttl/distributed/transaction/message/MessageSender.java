package com.pttl.distributed.transaction.message;

/**
 * 
 * @ClassName:  MessageSender   
 * @Description:  消息发送者 将之前jms的实现抽象出来
 * @author: srchen    
 * @date:   2020年06月23日 下午08:04:22
 */
public interface MessageSender {
	public void sent(final String msg);
}
