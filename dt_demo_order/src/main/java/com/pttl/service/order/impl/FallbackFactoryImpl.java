package com.pttl.service.order.impl;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.service.order.UserService;
@Component
public class FallbackFactoryImpl implements UserService{
	@Override
	public boolean payment(DistributedTransactionContext distributedTransactionContext, int userId, double payment) {
		System.out.println(distributedTransactionContext.getBranchTxId());
		System.out.println("出错了...默认方法");
		return false;
	}

}
