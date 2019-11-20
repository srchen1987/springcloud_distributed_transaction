package com.pttl.distributed.transaction.repository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;
/**
 * 
 * @ClassName:  RedisRepository   
 * @Description:   基于SpringRedisTemplate的实现 为了目前商城项目中的配置重用.
 * @author: srchen    
 * @date:   2019年11月20日 上午10:19:32
 * 
 * 注意 项目中有了redisTemplate的bean 所以没在架构里实现，项目中需要序列化成json 以下是代码 序列化被我拿掉了
 * 
@Configuration
public class RedisAutoConfig {
	   @Bean
	    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
	        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(redisConnectionFactory);
	        redisTemplate.afterPropertiesSet();
	        return redisTemplate;
	    }
}
 */
@Configuration
public class SpringRedisTemplateRepository extends TransactionRepository /* implements InitializingBean */ {

	@Autowired
	private RedisTemplate redisTemplate;
	private static final String PREFIX = "gtid_";

	@Override
	public int create(DistributedTransactionContext transaction) throws Exception {
		byte[] datas = serializer.serialize(transaction);
		Map map = new HashMap();
		map.put(transaction.getBranchTxId().getBytes(), datas);
		return (Integer) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.hMSet((PREFIX+transaction.getGlobalTxId()).getBytes(), map);
				return 1;
			}
		});
	}

	@Override
	public int update(DistributedTransactionContext transaction) throws Exception {
		byte[] datas = serializer.serialize(transaction);
		return (Integer) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Map<byte[],byte[]> map = connection.hGetAll(transaction.getGlobalTxId().getBytes());
				if(map!=null) {
					map.put(transaction.getBranchTxId().getBytes(), datas);
					connection.hMSet((PREFIX+transaction.getGlobalTxId()).getBytes(), map);
					return 1;
				}
				return 0;
			}
		});
		
	}

	@Override
	public int deleteByBranchTxId(String globalTxId, String branchTxId) throws Exception {
		return (Integer) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.hDel((PREFIX+globalTxId).getBytes(),branchTxId.getBytes());
				return 1;
			}
		});
		
	}

	@Override
	public int deleteByGlobalTxId(String globalTxId) throws Exception {
		return (Integer) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.del((PREFIX+globalTxId).getBytes());
				return 1;
			}
		});
		
		
	}
	@Override
	public List<DistributedTransactionContext> findAllByGlobalTxId(String globalTxId) throws Exception {
		List list = new ArrayList();
		return (List) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Collection<byte[]> collection = connection.hGetAll((PREFIX+globalTxId).getBytes()).values();
				for(byte [] bs:collection) {
					  try {
							list.add(serializer.deserialize(bs));
						} catch (Exception e) {
							throw new RuntimeException(e.getMessage());
						}
				}
				return list; 
			}
		});
	}
	



	@Override
	public int updateDataByGlobalTxId(String globalTxId, Map<String, Object> data) throws Exception {
		String status = (String) data.get("status");
		Map map = new HashMap();
		return (Integer) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Collection<byte[]> collection = connection.hGetAll((PREFIX+globalTxId).getBytes()).values();
				for(byte [] bs:collection) {
					try {
						DistributedTransactionContext context = (DistributedTransactionContext) serializer.deserialize(bs);
						if(status!=null) {
							context.setStatus(status);
						}
						bs = serializer.serialize(context);
						map.put(context.getBranchTxId().getBytes(),bs);
					} catch (Exception e) {
							throw new RuntimeException(e);
					}
				}
				if(!map.isEmpty()) {
						connection.hMSet((PREFIX+globalTxId).getBytes(),map);
				}
				return 1;
			}
		});
		
	}

	@Override
	public List<DistributedTransactionContext> findALLBySecondsLater(int seconds) throws Exception {
		List list = new ArrayList();
		return (List) redisTemplate.execute(new RedisCallback() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				Set<byte[]> mkeys= connection.keys((PREFIX+"*").getBytes());
				for(byte [] keys:mkeys) {
					Collection<byte[]> collection = connection.hGetAll(keys).values();
					for(byte [] bs:collection) {
						DistributedTransactionContext dc;
						try {
							dc = (DistributedTransactionContext) serializer.deserialize(bs);
							int now = (int) (System.currentTimeMillis()/1000);
							if((now-dc.getAddtime())>seconds) {
								list.add(dc);
							}
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
				return list;
			}
		});
	}
//	@Value("${spring.redis.database}")
//	private int database;
//	
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		JedisConnectionFactory factory = 
//				(JedisConnectionFactory) redisTemplate.getConnectionFactory();
//				factory.setDatabase(database);
//				redisTemplate.setConnectionFactory(factory);
//				
//				
//	}
	 
}
