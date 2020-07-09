package com.pttl.distributed.transaction.message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * 
 * @ClassName:  MessageConfig   
 * @Description:   消息配置 将之前jms的实现抽象出来
 * @author: srchen
 * @date:   2020年06月23日 下午08:02:12
 */
@ConfigurationProperties
public class MessageConfig {
	@Value("distributed_transaction_queue_${spring.profiles.active:}")
	private String transactionQueueName;
	
	public String getTransactionQueueName() {
		return transactionQueueName;
	}


	public void setTransactionQueueName(String transactionQueueName) {
		this.transactionQueueName = transactionQueueName;
	}
	
	
	
 

}
