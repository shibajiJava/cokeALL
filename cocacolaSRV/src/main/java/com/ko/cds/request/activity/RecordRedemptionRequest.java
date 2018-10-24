package com.ko.cds.request.activity;

import java.io.Serializable;
import java.math.BigInteger;

import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

public class RecordRedemptionRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sessionUUID;
	@NotNull
	@Length(min=1)
	private BigInteger memberId;
	@NotNull
	@Length(min=1)
	private BigInteger programId;
	
	private BigInteger lotId;
	
	 @MaxLength(value=10)
	private String productId;
	
	private String clientTransactionId;
	
	private Integer campaignId;
	
	/**Brand Id for storing brand level information*/
    @MaxLength(value=10)
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
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
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
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	

}
