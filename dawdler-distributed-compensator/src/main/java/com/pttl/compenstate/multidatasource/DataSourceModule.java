package com.pttl.compenstate.multidatasource;

/**
 * 
 * @ClassName:  DataSourceModule   
 * @Description:   数据源标记定义类
 * @author: srchen    
 * @date:   2019年11月02日 上午02:15:13
 */
public class DataSourceModule {
	public enum DataSourceName {
		PRODUCT, USER
	}

	private static final ThreadLocal<DataSourceName> NAME = new ThreadLocal<DataSourceName>();

	public static void setDataSourceName(DataSourceName dataSourceName) {
		if (dataSourceName == null) {
			throw new NullPointerException();
		}
		NAME.set(dataSourceName);
	}

	public static DataSourceName getDataSourceName() {
		return NAME.get();
	}

	public static void clearDataSourceName() {
		NAME.remove();
}
}
