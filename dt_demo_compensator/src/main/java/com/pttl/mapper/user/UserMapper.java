package com.pttl.mapper.user;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserMapper {
	
	int updatePaymentUser(@Param("userid")int userid,@Param("payment")double payment);
	
	int updateUserGoldInfo(@Param("branch_tx_id")String branch_tx_id,@Param("status")String status);
	
	List<Map> selectUserGoldInfo(@Param("branch_tx_id")String branch_tx_id);
}
