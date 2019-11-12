package com.pttl.distributed.transaction.jms;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
/**
 * 
 * @ClassName:  JmsConfig   
 * @Description:   jms配置
 * @author: srchen
 * @date:   2019年11月02日 上午0:02:41
 */
@Configuration
@PropertySource("classpath:application.properties")
public class JmsConfig {
//	public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory) {
//		JmsTemplate jmsTemplate = new JmsTemplate();
//		// 关闭事物
//		jmsTemplate.setSessionTransacted(false);
////	        jmsTemplate.setSessionAcknowledgeMode(ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
//		jmsTemplate.setConnectionFactory(factory);
//		return jmsTemplate;
//	}

	@Value("${activemq.broker-url}")
	private String brokerUrl;
	@Value("${activemq.username}")
	private String username;
	@Value("${activemq.password}")
	private String password;
	@Autowired
	private ActiveMQConnectionFactory activeMQConnectionFactory;

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(brokerUrl);
		factory.setAlwaysSyncSend(true);
		return factory;
	}

	
	@Bean
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
		return new JmsTemplate(activeMQConnectionFactory());
	}

}
