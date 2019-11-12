package com.pttl.entity.product;

public class ProductOperateInfo {
	private String branchTxId;
	private int productid;
	private int repertory;
	private String status;
	private int addtime;

	public String getBranchTxId() {
		return branchTxId;
	}

	public void setBranchTxId(String branchTxId) {
		this.branchTxId = branchTxId;
	}

	 

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public int getRepertory() {
		return repertory;
	}

	public void setRepertory(int repertory) {
		this.repertory = repertory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAddtime() {
		return addtime;
	}

	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
}
