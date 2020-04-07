package com.pttl.distributed.transaction.aspetct;
import org.aopalliance.intercept.MethodInvocation;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
/**
 * 
 * @ClassName:  TransactionInterceptInvoker   
 * @Description:   事务执行拦截者，主要是针对不同的响应结果做拦截，使用方式 继承接口并注入到spring的bean下
 * @author: srchen    
 * @date:   2019年11月02日 上午3:09:35
 */
public interface TransactionInterceptInvoker {
	
	Object invoke(MethodInvocation invocation,DistributedTransactionContext tc) throws Throwable;

}
