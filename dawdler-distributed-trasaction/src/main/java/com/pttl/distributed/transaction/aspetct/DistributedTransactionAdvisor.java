package com.pttl.distributed.transaction.aspetct;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 
 * @ClassName:  DistributedTransactionAdvisor   
 * @Description:   实现 aop的拦截
 * @author: srchen    
 * @date:   2019年11月02日 上午1:14:37
 */
public class DistributedTransactionAdvisor extends AbstractPointcutAdvisor{
	private static final long serialVersionUID = 8360797008163718139L;


	public DistributedTransactionAdvisor(AnnotationMatchingPointcut pointcut,DistributedTransactionInterceptor advice) {
		this.advice = advice;
		this.pointcut = pointcut;
	}
	private AnnotationMatchingPointcut pointcut;
	
	private DistributedTransactionInterceptor advice;
	

	public AnnotationMatchingPointcut getPointcut() {
		return pointcut;
	}

	public void setPointcut(AnnotationMatchingPointcut pointcut) {
		this.pointcut = pointcut;
	}

	public DistributedTransactionInterceptor getAdvice() {
		return advice;
	}

	public void setAdvice(DistributedTransactionInterceptor advice) {
		this.advice = advice;
	}
}
