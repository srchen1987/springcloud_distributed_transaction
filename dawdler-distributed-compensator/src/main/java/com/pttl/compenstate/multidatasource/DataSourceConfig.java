package com.pttl.compenstate.multidatasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;//如果为1.5.6 或1.5.x 就用这个包 spring boot开发者真是没意思 所以一直不喜欢用 spring
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.pttl.compenstate.multidatasource.DataSourceModule.DataSourceName;
/**
 * 
 * @ClassName:  DataSourceConfig   
 * @Description:   多数据源配置类
 * @author: srchen    
 * @date:   2019年11月02日 上午02:20:13
 */
@Configuration
//@PropertySource("classpath:application.properties")
@ConfigurationProperties
@MapperScan(basePackages = "com.pttl.mapper.*", sqlSessionFactoryRef = "SqlSessionFactory")
public class DataSourceConfig implements TransactionManagementConfigurer, ApplicationContextAware {
	
	/**
	 * 
	 * @Title: getUserDateSource   
	 * @Description: 配置用户库数据源
	 * @param: @return      
	 * @return: DataSource      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 下午2:22:01
	 */
	@ConfigurationProperties(prefix = "spring.datasource.user")
	@Bean("userDataSource")
	@Primary
	public DataSource getUserDateSource() {
		return DataSourceBuilder.create().build();
	}
	/**
	 * 
	 * @Title: getProductDateSource   
	 * @Description: 配置产品库数据源
	 * @param: @return      
	 * @return: DataSource      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午2:23:11
	 */
	@ConfigurationProperties(prefix = "spring.datasource.product")
	@Bean("productDataSource")
	public DataSource getProductDateSource() {
		return DataSourceBuilder.create().build();
	}
	
	
	Map<DataSourceName, DataSource> dataSources = new HashMap<>();

	/**
	 * 
	 * @Title: dataSouceProxy   
	 * @Description:  初始化动态数据源
	 * @param: @return      
	 * @return: DynamicDataSource      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午2:23:32
	 */
	@Bean(name="dynamicDataSource")
	public DynamicDataSource dataSouceProxy() {
		DynamicDataSource proxy = new DynamicDataSource();
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		for (DataSourceName type : dataSources.keySet()) {
			targetDataSources.put(type, dataSources.get(type));
		}
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}

	/**
	 * 
	 * @Title: sqlSessionFactory   
	 * @Description: 构建SqlSessionFactory
	 * @param: @param dynamicDataSource
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: SqlSessionFactory      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午2:25:18
	 */
	@Bean(name = "SqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dynamicDataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/*.xml"));
		return bean.getObject();
	}

	/**
	 * 
	 * @Title: txManager   
	 * @Description: 
	 * @param: @return
	 * @return: PlatformTransactionManager      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午2:26:51
	 */
	@Bean
	public PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSouceProxy());
	}

	
	/**
	 * 
	 * @Title: annotationDrivenTransactionManager   
	 * @Description: 获取PlatformTransactionManager
	 * @param: @return      
	 * @return: PlatformTransactionManager      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午2:27:30
	 */
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return txManager();
	}

	
	/**
	 * 
	 * @Title: setApplicationContext   
	 * @Description: 设置applicationContext
	 * @param: @return      
	 * @throws 
	 * @author: srchen     
	 * @date:   2019年11月02日 上午2:28:32
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		dataSources.put(DataSourceName.PRODUCT, (DataSource)applicationContext.getBean("productDataSource"));
		dataSources.put(DataSourceName.USER,  (DataSource)applicationContext.getBean("userDataSource"));
		
	}
}