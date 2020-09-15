package com.pttl.compenstate.service.user;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.annotation.TransactionStatus;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.distributed.transaction.message.DistributedTransactionCustomProcessor;
import com.pttl.mapper.user.UserMapper;
@Component("userPayment")
public class UserPaymentCustomProcessor extends DistributedTransactionCustomProcessor{
	@Autowired
	UserMapper userMapper;

	@Override
	public boolean process(DistributedTransactionContext context, String status) {
		List<Map> list = userMapper.selectUserGoldInfo(context.getBranchTxId());
		if(list.isEmpty())return true;
		Map map = list.get(0);
		BigDecimal gold = (BigDecimal)map.get("gold");
		int result = userMapper.updateUserGoldInfo(context.getBranchTxId(), status);
		if(result>0&&status.equals(TransactionStatus.CANCEL)) {
			userMapper.updatePaymentUser((int)map.get("userid"),gold.doubleValue());
		}
		return true;
	}
	
 
}
