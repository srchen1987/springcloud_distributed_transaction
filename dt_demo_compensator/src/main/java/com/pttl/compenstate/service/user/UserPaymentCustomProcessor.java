package com.pttl.compenstate.service.user;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.jms.DistributedTransactionCustomProcessor;
import com.pttl.mapper.user.UserMapper;
@Component("userPayment")
public class UserPaymentCustomProcessor extends DistributedTransactionCustomProcessor{
	@Autowired
	UserMapper userMapper;
	
	@Override
//	@Transactional
	public boolean process(String globalTxId, String branchTxId, Map<String, Object> datas, String status) {
		List<Map> list = userMapper.selectUserGoldInfo(branchTxId);
		if(list.isEmpty())return true;
		Map map = list.get(0);
		BigDecimal gold = (BigDecimal)map.get("gold");
		int result = userMapper.updateUserGoldInfo(branchTxId, status);
		if(result>0&&status.equals("cancel")) {
			userMapper.updatePaymentUser((int)map.get("userid"),gold.doubleValue());
		}
		return true;
	}

}
