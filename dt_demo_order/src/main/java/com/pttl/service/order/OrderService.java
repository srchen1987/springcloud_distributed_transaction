package com.pttl.service.order;
import com.pttl.service.order.entity.Order;
public interface OrderService {
	public int insertOrder(Order order);
	boolean buyProduct(String product, String num);
}
