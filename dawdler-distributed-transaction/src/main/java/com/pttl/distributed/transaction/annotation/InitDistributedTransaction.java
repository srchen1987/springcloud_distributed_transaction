package com.pttl.distributed.transaction.annotation;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.pttl.distributed.transaction.aspetct.AnnotationBeanPostProcessor;
import com.pttl.distributed.transaction.aspetct.DistributedTransactionInterceptor;
import com.pttl.distributed.transaction.jms.JmsConfig;
public class InitDistributedTransaction implements ImportSelector{
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{JmsConfig.class.getName(),AnnotationBeanPostProcessor.class.getName(),DistributedTransactionInterceptor.class.getName(),(String) importingClassMetadata.getAnnotationAttributes(EnableDistributedTransaction.class.getName()).get("transactionRepository")};
	}

}
