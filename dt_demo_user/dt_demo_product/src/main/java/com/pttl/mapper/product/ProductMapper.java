package com.pttl.mapper.product;
import com.pttl.entity.product.ProductOperateInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductMapper {
	int updaterepertory(@Param("productid")int productid,@Param("repertory")int repertory);

	int insertProductOperateInfo(ProductOperateInfo productOperateInfo);
}
