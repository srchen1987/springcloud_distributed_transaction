package com.pttl.distributed.transaction.annotation;
import java.util.Map;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import com.pttl.distributed.transaction.aspetct.AnnotationBeanPostProcessor;
import com.pttl.distributed.transaction.aspetct.DistributedTransactionInterceptor;
public class InitDistributedTransaction implements ImportSelector{
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Map data = getAnnotationAttributesData(importingClassMetadata);
		return new String[]{AnnotationBeanPostProcessor.class.getName(),
				DistributedTransactionInterceptor.class.getName(),
				((Class)data.get("transactionRepository")).getName()
		};
	}
	private Map<String, Object> getAnnotationAttributesData(AnnotationMetadata importingClassMetadata) {
		 return importingClassMetadata.getAnnotationAttributes(EnableDistributedTransaction.class.getName());
	}

}
