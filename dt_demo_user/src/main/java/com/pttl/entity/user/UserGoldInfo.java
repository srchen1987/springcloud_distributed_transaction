package com.pttl.entity.user;

public class UserGoldInfo {
	private String branchTxId;
	private int userid;
	private double gold;
	private String status;
	private int addtime;

	public String getBranchTxId() {
		return branchTxId;
	}

	public void setBranchTxId(String branchTxId) {
		this.branchTxId = branchTxId;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
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
