package com.pttl.controller.product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.service.product.ProductService;
@RestController
public class ProductController {
	@Autowired
	ProductService productService;
	
	@RequestMapping("/repertory")
	public boolean payment(@RequestBody DistributedTransactionContext distributedTransactionContext,@RequestParam("productId") int productid, @RequestParam("repertory") int repertory) {
		System.out.println("#########"+distributedTransactionContext.getAttachment());
		productService.updateRepertory(distributedTransactionContext.getBranchTxId(),productid, repertory);
		return false;
	}
}
