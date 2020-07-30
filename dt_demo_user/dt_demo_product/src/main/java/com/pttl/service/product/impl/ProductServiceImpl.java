package com.pttl.service.product.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pttl.entity.product.ProductOperateInfo;
import com.pttl.mapper.product.ProductMapper;
import com.pttl.service.product.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired
	ProductMapper productMapper;
	 
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateRepertory(String branchTxId,int productid, int repertory) {
		ProductOperateInfo productOperateInfo = new ProductOperateInfo();
		productOperateInfo.setBranchTxId(branchTxId);
		productOperateInfo.setProductid(productid);
		productOperateInfo.setRepertory(repertory);
		productOperateInfo.setStatus("commiting");
		productOperateInfo.setAddtime((int)(System.currentTimeMillis()/1000));
		int result = productMapper.updaterepertory(productid, repertory);
		if(result==0)throw new RuntimeException("inadequate！");
		return productMapper.insertProductOperateInfo(productOperateInfo);
	}

}
