package com.pttl.distributed.transaction.message.amqp;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.message.MessageConsumer;
import com.pttl.distributed.transaction.message.amqp.rabbitmq.AmqpConfig;
import com.rabbitmq.client.Channel;
/**
 * 
 * @ClassName:  AmqpConsumer   
 * @Description:   消费者 将消息分发到不同的处理者上去执行，继承DistributedTransactionCustomProcessor action为bean的id
 * @author: srchen    
 * @date:   2020年6月28日 上午11:10:15
 */
@Component
@ConditionalOnBean(AmqpConfig.class)
public class AmqpConsumer extends MessageConsumer{
	private static Logger log = LoggerFactory.getLogger(AmqpConsumer.class);
	/**
	 * 
	 * @Title: consumer   
	 * @Description: 消费者定义
	 * @param: @param message
	 * @param: @throws JMSException
	 * @return: void    
	 * @throws 
	 * @author: srchen     
	 * @date:   2020年6月28日 上午11:11:10
	 */
	@RabbitHandler
	@RabbitListener( 
			bindings = @QueueBinding(value = 
			@Queue(value="distributed_transaction_queue_${spring.profiles.active:}",durable = "true"),
			exchange = @Exchange(value="${spring.rabbitmq.exchange:}",durable = "true"),key = "distributed_transaction_queue_${spring.profiles.active:}"),admin = "rabbitAdmin")
	public void consumer(Channel channel,Message message){
		consumer(message.getBody());
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		} catch (IOException e) {
			log.error("",e);
		}
	}
	
	@Bean("rabbitAdmin")
	public RabbitAdmin getRabbitAdmin(ConnectionFactory connectionFactory){
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		return rabbitAdmin;
	}
}
