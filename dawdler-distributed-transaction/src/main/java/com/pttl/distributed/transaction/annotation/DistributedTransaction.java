package com.pttl.distributed.transaction.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName:  DistributedTransaction   
 * @Description:   事务注解
 * @author: srchen    
 * @date:   2019年11月02日 上午00:12:13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface DistributedTransaction {

String action();//订阅动作

boolean sponsor() default false;//是否为调用者 默认为协调者

	
}
