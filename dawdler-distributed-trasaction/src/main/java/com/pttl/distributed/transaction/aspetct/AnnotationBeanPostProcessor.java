package com.pttl.distributed.transaction.aspetct;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pttl.distributed.transaction.annotation.DistributedTransaction;
/**
 * 
 * @ClassName:  AnnotationBeanPostProcessor   
 * @Description:   覆盖advisor 实现aop
 * @author: srchen    
 * @date:   2019年11月02日 上午1:19:21
 */
@Component
public class AnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor{
	
	private static final long serialVersionUID = -5299257531723431065L;
	private Class<DistributedTransaction> distributedTransactionClass = DistributedTransaction.class;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		setProxyTargetClass(true);
		this.advisor = new DistributedTransactionAdvisor(AnnotationMatchingPointcut.forMethodAnnotation(getDistributedTransactionClass()),distributedTransactionInterceptor);
	}
	public Class<DistributedTransaction> getDistributedTransactionClass() {
		return distributedTransactionClass;
	}
	public void setDistributedTransactionClass(Class<DistributedTransaction> distributedTransactionClass) {
		this.distributedTransactionClass = distributedTransactionClass;
	}
	@Autowired
  public DistributedTransactionInterceptor distributedTransactionInterceptor;
}
