package com.pttl.distributed.transaction.message.jms.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.message.MessageSender;
/**
 * 
 * @ClassName:  JmsSender   
 * @Description:   
 * @author: srchen    
 * @date:   2019年11月02日 上午0:04:22
 */
@Component
@ConditionalOnBean(JmsConfig.class)
public class JmsSender implements MessageSender {
	public JmsSender() {
	}
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private JmsConfig jmsConfig;
	
	public void sent(final String msg) {
	        jmsTemplate.send(jmsConfig.getTransactionQueueName(), new MessageCreator() {
	            public Message createMessage(Session session) throws JMSException {
	                return session.createTextMessage(msg);
	            }
	        });
	}
}
