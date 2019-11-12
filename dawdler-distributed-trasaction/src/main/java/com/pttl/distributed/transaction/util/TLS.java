package com.pttl.distributed.transaction.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @Title:  TLS.java
 * @Description:    TLS工具类   
 * @author: jackson.song    
 * @date:   2007年10月09日    
 * @version V1.0 
 * @email: suxuan696@gmail.com
 */
public class TLS {
	private static ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<Map<Object, Object>>();
	public static void set(Object key, Object value) {
		Map<Object, Object> map = createIfNotExist();
		map.put(key, value);
	}
	 
	public static Object get(Object key) {
		Map<Object, Object> map = createIfNotExist();
		return map.get(key);
	}
	public static Object remove(Object key) {
		Map<Object, Object> map = threadLocal.get();
		if (map != null) {
			return map.remove(key);
		}
		return null;
	}
	
	private static Map<Object, Object> createIfNotExist() {
		Map<Object, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<Object,Object>();
			threadLocal.set(map);
		}
		return map;
}
}
