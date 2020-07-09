package com.pttl.distributed.transaction.message;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.distributed.transaction.message.jms.activemq.JmsConfig;
import com.pttl.distributed.transaction.repository.TransactionRepository;
import com.pttl.distributed.transaction.thread.DefaultThreadFactory;
import com.pttl.distributed.transaction.util.JsonUtils;
/**
 * 
 * @ClassName:  JmsConsumer   
 * @Description:   消费者 将消息分发到不同的处理者上去执行，继承DistributedTransactionCustomProcessor action为bean的id
 * @author: srchen    
 * @date:   2019年11月02日 上午3:04:15
 */
public class MessageConsumer implements ApplicationContextAware{
	private static Logger log = LoggerFactory.getLogger(MessageConsumer.class);
	ExecutorService executor;

	@Autowired
	TransactionRepository transactionRepository;

	public MessageConsumer() {
		int cpus = Runtime.getRuntime().availableProcessors() * 2 + 1;
		executor = new ThreadPoolExecutor(cpus, cpus, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024 * 64), new DefaultThreadFactory("transactionCustomExecutor#"));
	}

	
	public void consumer(String message){
		consumer(JsonUtils.jsonToClass(message, Map.class));
	}
	
  public void consumer(byte[] messageByte){
	  consumer(JsonUtils.jsonToClass(messageByte, Map.class));
	}
	/**
	 * 
	 * @Title: consumer   
	 * @Description: 通用处理消息的实现
	 * @param: @param map
	 * @return: void    
	 * @throws 
	 * @author: srchen     
	 * @date:   2020年06月28日 上午11:06:29
	 */
	private void consumer(Map<String, String> map){
		String globalTxId = map.get("globalTxId");
		String status = map.get("status");
		try {
		List<DistributedTransactionContext> list = transactionRepository.findAllByGlobalTxId(globalTxId);
		for(DistributedTransactionContext dt : list) {
			executor.execute(()->{
//				Map<String,Object> datas = dt.getDatas();
				String action = dt.getAction();
				Object obj = applicationContext.getBean(action);
				String branchTxId = dt.getBranchTxId();
				DistributedTransactionCustomProcessor dp = (DistributedTransactionCustomProcessor) obj;
//				boolean result = dp.process(globalTxId, branchTxId, datas, status);
				boolean result = dp.process(dt, status);
				log.debug("compensate_result: globalTxId:{} branchId:{} action:{} status:{} result:{}",dt.getGlobalTxId(),dt.getBranchTxId(),action,status,result);
				if(result) {
					try {
						transactionRepository.deleteByBranchTxId(globalTxId, branchTxId);
//						message.acknowledge();    //ack手动确认
					} catch (Exception e) {
						log.error("",e);
					}
				
				}else {
					dt.retryTimeIncre();
					try {
						transactionRepository.update(dt);
					} catch (Exception e) {
						log.error("",e);
					}
				}
			});
			
		}
		} catch (Exception e) {
			log.error("",e);
		}

	}
	
	
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
