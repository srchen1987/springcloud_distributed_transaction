package com.pttl.compenstate.multidatasource;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.pttl.compenstate.multidatasource.DataSourceModule.DataSourceName;

/**
 * 
 * @ClassName:  DataSourceAspect   
 * @Description:   aop实现切换多数据源标记
 * @author: srchen    
 * @date:   2019年11月02日 上午02:46:16
 */
@Aspect
@Component
@Order(Integer.MAX_VALUE)
public class DataSourceAspect {
	
	
	
	/**
	 * 
	 * @Title: switchUser   
	 * @Description:  标记为用户模块
	 * @param:       
	 * @return: void      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午02:50:02
	 */
	@Before("execution(* com.pttl.compenstate.service.user.*.*(..))")
	public void switchUser() {
		DataSourceModule.setDataSourceName(DataSourceName.USER);
	}
	
	
	/**
	 * 
	 * @Title: switchProduct   
	 * @Description: 标记为产品模块
	 * @param:       
	 * @return: void      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午02:52:14
	 */
	@Before("execution(* com.pttl.compenstate.service.product.*.*(..))")
	public void switchProduct() {
		DataSourceModule.setDataSourceName(DataSourceName.PRODUCT);
	}
	
	
	
	
/**
 * 
 * @Title: clear   
 * @Description: 清理数据源标记
 * @param:       
 * @return: void      
 * @throws 
 * @author: srchen     
 * @date:   2019年11月02日 上午02:49:23
 */
	@After("execution(* com.pttl.compenstate.service..*.*(..))")
	public void clear() {
		DataSourceModule.clearDataSourceName();
	}
	
}
