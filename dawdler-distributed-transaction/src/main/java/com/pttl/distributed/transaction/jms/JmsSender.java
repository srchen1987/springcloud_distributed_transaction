package com.pttl.distributed.transaction.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
/**
 * 
 * @ClassName:  JmsSender   
 * @Description:   
 * @author: srchen    
 * @date:   2019年11月02日 上午0:04:22
 */
@Component
public class JmsSender {
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void sent(String qname, final String msg) {
	        jmsTemplate.send(qname, new MessageCreator() {
	            public Message createMessage(Session session) throws JMSException {
	                return session.createTextMessage(msg);
	            }
	        });
	}
}
