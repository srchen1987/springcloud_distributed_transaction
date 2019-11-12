package com.pttl.compenstate.multidatasource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import com.pttl.compenstate.multidatasource.DataSourceModule.DataSourceName;
/**
 * 
 * @ClassName:  DynamicDataSource   
 * @Description:   动态数据源重写determine来确认当前的数据源
 * @author: srchen    
 * @date:   2019年11月02日 上午2:12:10
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
	
	@Override
	protected Object determineCurrentLookupKey() {
		DataSourceName dataSourceName = DataSourceModule.getDataSourceName();
		return dataSourceName;
}
}
