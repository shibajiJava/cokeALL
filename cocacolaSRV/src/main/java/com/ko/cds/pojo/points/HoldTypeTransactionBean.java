package com.ko.cds.pojo.points;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class HoldTypeTransactionBean implements Serializable{
	
	private static final long serialVersionUID = 7936793601941792736L;
	String pointTransId;
	
	private BigInteger relatedTransactionId;
	
	private BigInteger memberId;
	
	private String lastTransactionDate;
	
	private String startTime;
	
	private String endTime;
	
	private Date insertDate;
	
	private int pointsQuanity;
	
	String pointsType;
	
	private String accountType;
	
	private BigInteger balance;
	private Integer holdTime;
	
	private String statusLevel;
	

	public Integer getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Integer holdTime) {
		this.holdTime = holdTime;
	}

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	public HoldTypeTransactionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public HoldTypeTransactionBean(String pointTransId,
			BigInteger relatedTransactionId, BigInteger memberId,
			String lastTransactionDate, Date insertDate, int pointsQuanity,
			String pointsType) {
		super();
		this.pointTransId = pointTransId;
		this.relatedTransactionId = relatedTransactionId;
		this.memberId = memberId;
		this.lastTransactionDate = lastTransactionDate;
		this.insertDate = insertDate;
		this.pointsQuanity = pointsQuanity;
		this.pointsType = pointsType;
	}

	/**
	 * @return the pointTransId
	 */
	public String getPointTransId() {
		return pointTransId;
	}

	/**
	 * @param pointTransId the pointTransId to set
	 */
	public void setPointTransId(String pointTransId) {
		this.pointTransId = pointTransId;
	}

	/**
	 * @return the relatedTransactionId
	 */
	public BigInteger getRelatedTransactionId() {
		return relatedTransactionId;
	}

	/**
	 * @param relatedTransactionId the relatedTransactionId to set
	 */
	public void setRelatedTransactionId(BigInteger relatedTransactionId) {
		this.relatedTransactionId = relatedTransactionId;
	}

	/**
	 * @return the memberId
	 */
	public BigInteger getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the lastTransactionDate
	 */
	public String getLastTransactionDate() {
		return lastTransactionDate;
	}

	/**
	 * @param lastTransactionDate the lastTransactionDate to set
	 */
	public void setLastTransactionDate(String lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	/**
	 * @return the insertDate
	 */
	public Date getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @return the pointsQuanity
	 */
	public int getPointsQuanity() {
		return pointsQuanity;
	}

	/**
	 * @param pointsQuanity the pointsQuanity to set
	 */
	public void setPointsQuanity(int pointsQuanity) {
		this.pointsQuanity = pointsQuanity;
	}

	/**
	 * @return the pointsType
	 */
	public String getPointsType() {
		return pointsType;
	}

	/**
	 * @param pointsType the pointsType to set
	 */
	public void setPointsType(String pointsType) {
		this.pointsType = pointsType;
	}

	
	public String getStatusLevel() {
		return statusLevel;
	}

	public void setStatusLevel(String statusLevel) {
		this.statusLevel = statusLevel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HoldTypeTransactionBean [pointTransId=" + pointTransId
				+ ", relatedTransactionId=" + relatedTransactionId
				+ ", memberId=" + memberId + ", lastTransactionDate="
				+ lastTransactionDate + ", insertDate=" + insertDate
				+ ", pointsQuanity=" + pointsQuanity + ", pointsType="
				+ pointsType + "]";
	}
	

}
