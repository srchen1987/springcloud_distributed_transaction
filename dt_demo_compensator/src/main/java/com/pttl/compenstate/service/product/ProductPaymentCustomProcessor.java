package com.pttl.compenstate.service.product;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.distributed.transaction.jms.DistributedTransactionCustomProcessor;
import com.pttl.mapper.product.ProductMapper;
@Component("productPayment")
public class ProductPaymentCustomProcessor extends DistributedTransactionCustomProcessor{
	
	@Autowired
	private ProductMapper productMapper;

	@Override
	public boolean process(DistributedTransactionContext context, String status) {
		List<Map> list = productMapper.selectProductOperateInfo(context.getBranchTxId());
		if(list.isEmpty())return true;
		Map map = list.get(0);
		int repertory = (int)map.get("repertory");
		int result = productMapper.updateProductOperateInfo(context.getBranchTxId(), status);
		if(result>0&&status.equals("cancel")) {
			productMapper.updaterepertory((int)map.get("productid"), repertory);
		}
		return true;
	}
}
