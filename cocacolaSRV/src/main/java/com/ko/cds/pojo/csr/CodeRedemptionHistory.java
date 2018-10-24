package com.ko.cds.pojo.csr;

import java.io.Serializable;
import java.math.BigInteger;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;

/**
 * Pojo for the Code Redemption History SQL results set.
 */
@JsonIgnoreProperties("clientTransactionIdPoints")
public class CodeRedemptionHistory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger memberId;
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String	transactionTimeStamp ;
	private String 	clientTransactionId ;
	private BigInteger	siteId;
	
	private String 	siteName;
	private String 	pointAccountName ;
	private String 	award ;
	private BigInteger	points;
	private String 	promotion;
	
	private String clientTransactionIdPoints ;
	
	public String getClientTransactionIdPoints() {
		return clientTransactionIdPoints;
	}
	public void setClientTransactionIdPoints(String clientTransactionIdPoints) {
		this.clientTransactionIdPoints = clientTransactionIdPoints;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getTransactionTimeStamp() {
		return transactionTimeStamp;
	}
	public void setTransactionTimeStamp(String transactionTimeStamp) {
		this.transactionTimeStamp = transactionTimeStamp;
	}
	public String getClientTransactionId() {
		return clientTransactionId;
	}
	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
	}
	public BigInteger getSiteId() {
		return siteId;
	}
	public void setSiteId(BigInteger siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getPointAccountName() {
		return pointAccountName;
	}
	public void setPointAccountName(String pointAccountName) {
		this.pointAccountName = pointAccountName;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public BigInteger getPoints() {
		return points;
	}
	public void setPoints(BigInteger points) {
		this.points = points;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	@Override
	public String toString() {
		return "CodeRedemptionObject [memberId=" + memberId
				+ ", transactionTimeStamp=" + transactionTimeStamp
				+ ", clientTransactionId=" + clientTransactionId + ", siteId="
				+ siteId + ", siteName=" + siteName + ", pointAccountName="
				+ pointAccountName + ", award=" + award + ", points=" + points
				+ ", promotion=" + promotion + "]";
	}
	

	

}
