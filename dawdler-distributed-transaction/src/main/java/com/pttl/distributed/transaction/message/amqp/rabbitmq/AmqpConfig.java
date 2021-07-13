package com.pttl.distributed.transaction.message.amqp.rabbitmq;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.message.MessageConfig;
/**
 * 
 * @ClassName:  AmqpConfig   
 * @Description:   AMQP配置
 * @author: srchen
 * @date:    2020年06月23日 下午08:05:12
 */
@ConfigurationProperties
@Component
@ConditionalOnExpression("'${dawdler.transaction.mqserver}'.equals('rabbitmq')")
public class AmqpConfig extends MessageConfig {
	@Value("${spring.rabbitmq.host:}")
	private String host;
	
	@Value("${spring.rabbitmq.port:}")
	private Integer port;
	
	@Value("${spring.rabbitmq.username:}")
	private String username;
	
	@Value("${spring.rabbitmq.password:}")
	private String password;
	
	@Value("${spring.rabbitmq.virtualHost:}")
	private String virtualHost;
	
	@Value("${spring.rabbitmq.exchange:}")
	private String exchange;
	
	public String getExchange() {
		return exchange;
	}
//	@Autowired
//	private CachingConnectionFactory connectionFactory;
//	@Bean("transactionAmqpListenerContainerFactory")
//	  public SimpleRabbitListenerContainerFactory jmsListenerContainerFactory() {
//		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//	        factory.setConnectionFactory(connectionFactory);
////	        factory.setPubSubDomain(true);
////	        factory.setConcurrency("3-10");
//	        factory.setRecoveryInterval(1000L);
//	        return factory;
//	    }
//	CachingConnectionFactory factory;
//	@Bean("transactionCachingConnectionFactory")
//	public CachingConnectionFactory cachingConnectionFactory(){
//		  factory = new CachingConnectionFactory();
//	    factory.setHost(host);
//	    factory.setPort(port);
//	    factory.setUsername(username);
//	    factory.setPassword(password);
//	    factory.setVirtualHost(virtualHost);
////	    factory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
////	    factory.setPublisherReturns(rabbitProperties.isPublisherReturns());
////	    factory.addChannelListener(rabbitChannelListener);
////	    factory.addConnectionListener(rabbitConnectionListener);
////	    factory.setRecoveryListener(rabbitRecoveryListener);
//
//	    return factory;
//	}
//  
//
//	  @Bean
//	  public RabbitTemplate createRabbitTemplateTest(){
//	        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//	        rabbitTemplate.setConnectionFactory(factory);
////	        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
////	        rabbitTemplate.setMandatory(true);
////	 
////	        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
////	            @Override
////	            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
////	            }
////	        });
////	 
////	        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
////	            @Override
////	            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
////	            }
////	        });
//	        
//	        return rabbitTemplate;
//	    }
	 
	

}
