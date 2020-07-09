package com.pttl.service.order.entity;

public class Order {
	private String orderid;
	private int pid;
	private double gold;
	private int quantity;
	private String status;
	private int addtime;
	
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public double getGold() {
		return gold;
	}
	public void setGold(double gold) {
		this.gold = gold;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
