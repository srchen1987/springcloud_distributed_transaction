package com.pttl.service.order.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pttl.distributed.transaction.annotation.DistributedTransaction;
import com.pttl.distributed.transaction.annotation.TransactionStatus;
import com.pttl.distributed.transaction.context.DistributedTransactionContext;
import com.pttl.mapper.order.OrderMapper;
import com.pttl.service.order.OrderService;
import com.pttl.service.order.ProductService;
import com.pttl.service.order.UserService;
import com.pttl.service.order.entity.Order;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	UserService userService;

	@Autowired
	ProductService productService;

	@Autowired
	OrderMapper orderMapper;


	@Override
	public int insertOrder(Order order) {
		
		return 0;
	}

	@Override
	@Transactional
	@DistributedTransaction(action = "createOrder",sponsor = true)
	public boolean buyProduct(String product, String num) {
		int userId = 1;
		int productId = Integer.parseInt(product);
		int repertory = Integer.parseInt(num);
		double payment = Double.valueOf(10*repertory);
		String orderId = UUID.randomUUID().toString().replace("-", "");
		Order order = new Order();
		order.setOrderid(orderId);
		order.setPid(productId);
		order.setQuantity(repertory);
		order.setStatus(TransactionStatus.COMMITING);
		order.setAddtime((int)System.currentTimeMillis());
		try {
			List list = new ArrayList();
			list.add(1);
			DistributedTransactionContext.getDistributedTransactionContext().setAttachment(list);
			userService.payment(null,userId,payment);
			List list2 = new ArrayList();
			list2.add(2);
			DistributedTransactionContext.getDistributedTransactionContext().setAttachment(list2);
			productService.payment(null,productId,repertory);
			orderMapper.insertOrder(order);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
