package com.pttl.mapper.product;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductMapper {
	
	int updaterepertory(@Param("productid")int productid,@Param("repertory")int repertory);

	int updateProductOperateInfo(@Param("branch_tx_id")String branch_tx_id,@Param("status")String status);

	List<Map> selectProductOperateInfo(@Param("branch_tx_id")String branch_tx_id);


}
