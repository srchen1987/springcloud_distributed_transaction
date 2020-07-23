package com.pttl.distributed.transaction.message.amqp.rabbitmq;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import com.pttl.distributed.transaction.message.MessageSender;
/**
 * 
 * @ClassName:  AmqpSender   
 * @Description:   Amqp发送者 目前没支持
 * @author: srchen    
 * @date:    2020年06月23日 下午08:15:11
 */
@Component
@ConditionalOnBean(AmqpConfig.class)
public class AmqpSender implements MessageSender {
	public AmqpSender() {
	}
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private AmqpConfig amqpConfig;
	
	public void sent(final String msg) {
		Message message = MessageBuilder.withBody(msg.getBytes()).setContentEncoding("utf-8").setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
		rabbitTemplate.send(amqpConfig.getExchange(),amqpConfig.getTransactionQueueName(), message);
	}
}
