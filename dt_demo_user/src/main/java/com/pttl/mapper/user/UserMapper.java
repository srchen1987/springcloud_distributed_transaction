package com.pttl.mapper.user;

import com.pttl.entity.user.UserGoldInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserMapper {
	int updatePaymentUser(@Param("userid")int userid,@Param("payment")double payment);

	int insertUserGoldInfo(UserGoldInfo userGoldInfo);
}
