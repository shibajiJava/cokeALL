package com.ko.cds.pojo.activity;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.NotNull;

public class RedemptionTrans implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private BigInteger sessionId;
	@NotNull
	@Length(min=1)
	private BigInteger memberId;
	@NotNull
	@Length(min=1)
	private BigInteger programId;
	@NotNull
	@Length(min=1)
	private BigInteger lotId;
	private String productId;
	private Timestamp insertDt;
	private BigInteger transactionId;
	
	private String clientTransactionId;
	
	private Integer campaignId;
	
	/**Brand Id for storing brand level information*/
    private String brandId;

	
	public String getClientTransactionId() {
		return clientTransactionId;
	}
	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
	}
	public Integer getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}
	
	public BigInteger getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(BigInteger transactionId) {
		this.transactionId = transactionId;
	}
	public BigInteger getSessionId() {
		return sessionId;
	}
	public void setSessionId(BigInteger sessionId) {
		this.sessionId = sessionId;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	
	public BigInteger getProgramId() {
		return programId;
	}
	public void setProgramId(BigInteger programId) {
		this.programId = programId;
	}
	public BigInteger getLotId() {
		return lotId;
	}
	public void setLotId(BigInteger lotId) {
		this.lotId = lotId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Timestamp getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Timestamp insertDt) {
		this.insertDt = insertDt;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
}
