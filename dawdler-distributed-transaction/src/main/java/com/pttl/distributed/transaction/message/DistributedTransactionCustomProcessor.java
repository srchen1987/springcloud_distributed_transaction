package com.pttl.distributed.transaction.message;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;
/**
 * 
 * @ClassName:  DistributedTransactionCustomProcessor   
 * @Description:   
 * @author: srchen    
 * @date:   2019年11月02日 上午1:26:21
 */
public abstract class DistributedTransactionCustomProcessor {
//	public abstract boolean process(String globalTxId,String branchTxId,Map<String,Object> datas,String status); 


	public abstract boolean process(DistributedTransactionContext context,String status); 
}
