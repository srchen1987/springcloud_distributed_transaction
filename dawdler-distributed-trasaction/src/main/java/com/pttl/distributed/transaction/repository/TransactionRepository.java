package com.pttl.distributed.transaction.repository;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.distributed.transaction.serializer.Serializer;
/**
 * 
 * @ClassName:  TransactionRepository   
 * @Description:   抽象的事务存储方式，写的很急，未来会采用linked方式做链处理
 * @author: srchen    
 * @date:   2019年11月01日 下午10:16:21
 */
public abstract class TransactionRepository {
	
	@Autowired
	protected Serializer serializer;
	
	public abstract int create(DistributedTransactionContext transaction) throws Exception;
 
	public abstract int update(DistributedTransactionContext transaction)throws Exception;
 
	public abstract int updateDataByGlobalTxId(String globalTxId,Map<String,Object> data)throws Exception;
	
	public abstract int deleteByBranchTxId(String globalTxId,String branchTxId)throws Exception;

	public abstract  int deleteByGlobalTxId(String globalTxId)throws Exception;
 
	public abstract List<DistributedTransactionContext> findAllByGlobalTxId(String globalTxId)throws Exception;
    
	public abstract List<DistributedTransactionContext> findALLBySecondsLater(int seconds) throws Exception;
    
}
