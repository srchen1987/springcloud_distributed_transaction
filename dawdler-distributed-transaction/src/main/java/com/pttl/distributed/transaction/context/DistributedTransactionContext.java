package com.pttl.distributed.transaction.context;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import com.pttl.distributed.transaction.util.TLS;


/**
 * 
 * @ClassName:  DistributedTransactionContext   
 * @Description:   
 * @author: srchen    
 * @date:   2019年11月01日 下午11:42:14
 * @param <T>
 */
public class DistributedTransactionContext<T> implements Serializable,Cloneable{
	private static final String DISTRIBUTEDTRANSACTIONCONTEXT_KEY = "DISTRIBUTEDTRANSACTIONCONTEXT_KEY";
	public static final String STATUS_COMMITING = "commiting";
	private static final long serialVersionUID = 3067909020545794630L;
	private String globalTxId;
	private String branchTxId;
	private boolean cancel=false;//整个事务取消
	private String status;//状态  commiting cancel confirm
	private int addtime;//添加时间
	private String action;//模块功能的简称 
	private int retryTime=0;//重试次数
	private Map<String,Object> datas;
	private T attachment;//其他对象需要传入的 因为springcloud 只支持一个对象参数
	/**
	 * 是否被干扰 如果被干扰 其他状态不生效 存为commting状态
	 * 在商城实际应用场景中是因为开发人员没有
	 */
	private boolean intervene;
	 
	public boolean isIntervene() {
		return intervene;
	}
	public void setIntervene(boolean intervene) {
		this.intervene = intervene;
	}
	public T getAttachment() {
		return attachment;
	}
	public void setAttachment(T attachment) {
		this.attachment = attachment;
	}
	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}
	public int getRetryTime() {
		return retryTime;
	}
	public void retryTimeIncre() {
		retryTime++;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	public Map<String, Object> getDatas() {
		return datas;
	}
	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public DistributedTransactionContext() {
	}
	public void init() {
		branchTxId = UUID.randomUUID().toString();
		addtime = (int) (System.currentTimeMillis()/1000);
	}
	public DistributedTransactionContext(String globalTxId){
		this();
		this.globalTxId=globalTxId;
	}
	
	public String getGlobalTxId() {
		return globalTxId;
	}
	public void setGlobalTxId(String globalTxId) {
		this.globalTxId = globalTxId;
	}
	public String getBranchTxId() {
		return branchTxId;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public void setBranchTxId(String branchTxId) {
		this.branchTxId = branchTxId;
	}
	
	public static DistributedTransactionContext  getDistributedTransactionContext() {
		return (DistributedTransactionContext) TLS.get(DISTRIBUTEDTRANSACTIONCONTEXT_KEY);
	}
	
	public static void setDistributedTransactionContext(DistributedTransactionContext distributedTransactionContext) {
		if(distributedTransactionContext==null)
			TLS.remove(DISTRIBUTEDTRANSACTIONCONTEXT_KEY);
		else
		  TLS.set(DISTRIBUTEDTRANSACTIONCONTEXT_KEY, distributedTransactionContext);
	}
	
	public boolean isCancel() {
		return cancel;
	}
	
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	
}
