package com.pttl.distributed.transaction.message.jms;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.pttl.distributed.transaction.message.MessageConsumer;
import com.pttl.distributed.transaction.message.jms.activemq.JmsConfig;
/**
 * 
 * @ClassName:  JmsConsumer   
 * @Description:   消费者 将消息分发到不同的处理者上去执行，继承DistributedTransactionCustomProcessor action为bean的id
 * @author: srchen    
 * @date:   2019年11月02日 上午3:04:15
 */
@Component
@ConditionalOnBean(JmsConfig.class)
public class JmsConsumer extends MessageConsumer{
	private static Logger log = LoggerFactory.getLogger(JmsConsumer.class);
 
	/**
	 * 
	 * @Title: consumer   
	 * @Description: 消费者定义
	 * @param: @param message
	 * @param: @throws JMSException
	 * @return: void    
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午3:06:29
	 */
	@JmsListener(destination = "distributed_transaction_queue_${spring.profiles.active:}",
			containerFactory = "transactionJmsListenerContainerFactory")
	public void consumer(ActiveMQMessage message){
		String text;
		try {
			text = ((TextMessage) message).getText();
			consumer(text);
		} catch (JMSException e) {
			log.error("",e);
		}
		
	}
}
