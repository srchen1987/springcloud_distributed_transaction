package com.pttl.service.user.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pttl.entity.user.UserGoldInfo;
import com.pttl.mapper.user.UserMapper;
import com.pttl.service.user.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserMapper userMapper;
	@Override

	@Transactional(rollbackFor = Exception.class)
	public int updatePaymentUser(String branchTxId,int userid,double payment) {
		UserGoldInfo userGoldInfo = new UserGoldInfo();
		userGoldInfo.setBranchTxId(branchTxId);
		userGoldInfo.setUserid(userid);
		userGoldInfo.setGold(payment);
		userGoldInfo.setStatus("commiting");
		userGoldInfo.setAddtime((int)System.currentTimeMillis());
		int result = userMapper.updatePaymentUser(userid, payment);
		if(result==0)throw new RuntimeException("inadequate！");
		return userMapper.insertUserGoldInfo(userGoldInfo);
	}

}
