package com.pttl.distributed.transaction.message.jms.activemq;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.message.MessageConfig;
/**
 * 
 * @ClassName:  JmsConfig   
 * @Description:   jms配置
 * @author: srchen
 * @date:   2019年11月02日 上午0:02:41
 */
@Component
@ConfigurationProperties(prefix="spring.activemq")
@ConditionalOnExpression("'${dawdler.transaction.mqserver}'.equals('activemq')")
public class JmsConfig extends MessageConfig {
	public JmsConfig() {
		// TODO Auto-generated constructor stub
	}
//	public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory) {
//		JmsTemplate jmsTemplate = new JmsTemplate();
//		// 关闭事物
//		jmsTemplate.setSessionTransacted(false);
////	        jmsTemplate.setSessionAcknowledgeMode(ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
//		jmsTemplate.setConnectionFactory(factory);
//		return jmsTemplate;
//	}
	@Value("${spring.activemq.broker-url:}")
	private String brokerUrl;
	
	@Value("${spring.activemq.username:}")
	private String username;
	
	@Value("${spring.activemq.password:}")
	private String password;
	
	 
	 
	
	@Autowired
	private ConnectionFactory activeMQConnectionFactory;

	@Bean
	public ConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(brokerUrl);
		factory.setAlwaysSyncSend(true);
		factory.setUserName(username);
		factory.setPassword(password);
		return factory;
	}

	
	@Bean("transactionJmsListenerContainerFactory")
  public JmsListenerContainerFactory<?> jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory);
//        factory.setPubSubDomain(true);
//        factory.setConcurrency("3-10");
        factory.setRecoveryInterval(1000L);
        return factory;
    }
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
//		 jmsTemplate.setSessionAcknowledgeMode(4);
		return jmsTemplate;
	}

}
