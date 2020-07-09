package com.pttl.distributed.transaction.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.pttl.distributed.transaction.repository.SpringRedisTemplateRepository;
import com.pttl.distributed.transaction.repository.TransactionRepository;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(InitDistributedTransaction.class)
public @interface EnableDistributedTransaction {
	 Class<? extends TransactionRepository> transactionRepository() default SpringRedisTemplateRepository.class; 
}
