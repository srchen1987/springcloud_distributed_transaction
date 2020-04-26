package com.pttl.distributed.transaction.aspetct;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pttl.distributed.transaction.annotation.DistributedTransaction;
import com.pttl.distributed.transaction.annotation.TransactionStatus;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.distributed.transaction.jms.JmsConfig;
import com.pttl.distributed.transaction.jms.JmsSender;
import com.pttl.distributed.transaction.repository.TransactionRepository;
import com.pttl.distributed.transaction.util.JsonUtils;

/**
 * 
 * @ClassName: DistributedTransactionInterceptor
 * @Description: aop拦截器 根据事务注解来做事务处理
 * @author: srchen
 * @date: 2019年11月02日 上午00:16:24
 */
public class DistributedTransactionInterceptor implements MethodInterceptor {
	private static Logger log = LoggerFactory.getLogger(DistributedTransactionInterceptor.class);

	@Autowired(required = false)
	private TransactionInterceptInvoker invoker;

	@Autowired(required = false)
	private JmsConfig jmsConfig;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		DistributedTransaction dt = method.getAnnotation(DistributedTransaction.class);
		if (dt == null) {
			Object obj = invocation.proceed();
			return obj;
		}
		DistributedTransactionContext dc = DistributedTransactionContext.getDistributedTransactionContext();
		if (dt.sponsor()) {
			if (dc != null)
				throw new IllegalStateException(
						"This transaction have the sponsor # globalTxId:\t" + dc.getGlobalTxId());
			String globalTxId = UUID.randomUUID().toString();
			dc = new DistributedTransactionContext(globalTxId);
			dc.init();
			DistributedTransactionContext.setDistributedTransactionContext(dc);
			String action = dt.action(); 
			dc.setAction(action);
			Object obj = null;
			boolean intervene = false;
			try {
				if(log.isDebugEnabled())
					log.debug("transaction proceed sponsor:{} action:{}",dc.getGlobalTxId(),action);
				obj = invocation.proceed();
			} catch (Throwable e) {
				if(!dc.isIntervene())
					cancel(action, globalTxId);
				if(log.isDebugEnabled())
					log.debug("transaction proceed exception sponsor:{} action:{} ",dc.getGlobalTxId(),action);
				throw e; 
			} finally {
				DistributedTransactionContext.setDistributedTransactionContext(null);
			}
			if(!dc.isIntervene())
				if (dc.isCancel()) {
					try {
						if(log.isDebugEnabled())
							log.debug("transaction proceed cancel sponsor:{} action:{} ",dc.getGlobalTxId(),action);
						cancel(action, globalTxId);
					} catch (Exception e) {
						log.error("distributed_transaction_cancel", e);
					}
				} else {
					try {
						if(log.isDebugEnabled())
							log.debug("transaction proceed confirm sponsor:{} action:{} ",dc.getGlobalTxId(),action);
						confirm(action, globalTxId);
					} catch (Exception e) {
						log.error("distributed_transaction_confirm", e);
					}
				}
			return obj;
		} else {
			Object args[] = invocation.getArguments();
			if (dc != null && !dc.isCancel()) {
				String globalTxId = dc.getGlobalTxId();
				int position = -1;
				Parameter[] parameters = method.getParameters();
				Map<String, Object> datas = new HashMap();
				for (int i = 0; i < parameters.length; i++) {
					String key = parameters[i].getName();
					parameters[i].getType();
					if (parameters[i].getType().equals(DistributedTransactionContext.class)) {
						position = i;
					} else {
						datas.put(key, args[i]);
					}
				}

				if (position == -1)
					throw new IllegalStateException(
							"This transaction method's args must be contain DistributedTransactionContext :\t"
									+ invocation.getThis() + "\t" + method.getName());
				DistributedTransactionContext branch_dc = new DistributedTransactionContext(globalTxId);
				branch_dc.setAttachment(dc.getAttachment());
				branch_dc.init();
				args[position] = branch_dc;
				branch_dc.setAction(dt.action());
				branch_dc.setStatus(TransactionStatus.COMMITING);
				DistributedTransactionContext serializableData = (DistributedTransactionContext) branch_dc.clone();
				serializableData.setDatas(datas);
				Object obj = null;
				Exception error = null;
				boolean success = true;
				try {
					if(log.isDebugEnabled())
						log.debug("transaction proceed  globalTxid:{} branchTxId:{} action:{} create to redis",branch_dc.getGlobalTxId(),branch_dc.getBranchTxId(),branch_dc.getAction());
					transactionRepository.create(serializableData);
					if (invoker != null)
						obj = invoker.invoke(invocation, dc);
					else
						obj = invocation.proceed();
				} catch (Exception e) {
					error = e;
					success = false;
				}
				dc.setAttachment(null);
				if (!success) {
					if(log.isDebugEnabled())
						log.debug("transaction proceed failed globalTxid:{} branchTxId:{} action:{} create to redis",branch_dc.getGlobalTxId(),branch_dc.getBranchTxId(),branch_dc.getAction());
					dc.setCancel(true);
					if (error != null)
						throw error;
				}
				return obj;
			} else {
				Object obj = null;
				try {
					obj = invocation.proceed();
				} catch (Exception e) {
					throw e;
				}
				return obj;
			}
		}

	}

	@Autowired
	private JmsSender jmsSender;

	@Autowired
	private TransactionRepository transactionRepository;

	ServiceLoader<TransactionRepository> services = ServiceLoader.load(TransactionRepository.class);
//	public static final String QNAME = "distributed_transaction_queue";

	@Pointcut("@annotation(com.pttl.distributed.transaction.annotation.DistributedTransaction)")
	public void compensableService() {
	}

	@Around("compensableService()")
	public Object interceptCompensableMethod(ProceedingJoinPoint pjp) throws Throwable {
		return null;
	}

	public static int getTransactionContextParamPosition(Class<?>[] parameterTypes) {
		int position = -1;
		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i].equals(DistributedTransactionContext.class)) {
				position = i;
				break;
			}
		}
		return position;
	}

	public static DistributedTransactionContext getTransactionContextFromArgs(Object[] args) {
		DistributedTransactionContext distributedTransactionContext = null;
		for (Object arg : args) {
			if (arg != null && DistributedTransactionContext.class.isAssignableFrom(arg.getClass())) {

				distributedTransactionContext = (DistributedTransactionContext) arg;
			}
		}
		return distributedTransactionContext;
	}

	public void confirm(String action, String globalTxId) throws Exception {
		Map data = new HashMap();
		data.put("status", TransactionStatus.CONFIRM);
		data.put("action", action);
		data.put("globalTxId", globalTxId);
		transactionRepository.updateDataByGlobalTxId(globalTxId, data);
		String msg = JsonUtils.objectToJson(data);
		jmsSender.sent(jmsConfig.getTransactionQueueName(), msg);
	}

	public void cancel(String action, String globalTxId) throws Exception {
		Map<String, Object> data = new HashMap();
		data.put("status", TransactionStatus.CANCEL);
		data.put("action", action);
		data.put("globalTxId", globalTxId); 
		transactionRepository.updateDataByGlobalTxId(globalTxId, data);
		String msg = JsonUtils.objectToJson(data);
		jmsSender.sent(jmsConfig.getTransactionQueueName(), msg);
	}
}
