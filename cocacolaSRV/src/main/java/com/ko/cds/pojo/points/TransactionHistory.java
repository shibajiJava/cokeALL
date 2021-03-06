package com.ko.cds.pojo.points;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;



/***
 * 
 * @author IBM
 *
 */
public class TransactionHistory implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6858955358458674701L;
	/** Transaction_dt*/
	private Date transactionDate;
	/** Transaction_dt*/
	private String transactionTimeStampString;
	/***trans_type_cd  records the "debit" or "credit" values **/
	private String transactionType;
	/** Point Type Code-credit type -�Regular�, �Admin� or �Bonus� points will be placed here**/
	private String pointsType;
	/**The reason code for the transaction**/
	private String reasonCode;
	/** brand_cd , brev_prod_cd -Records the added brand detail */
	private String productID;
	/**Unique Id for the member generated by CDS**/
	private BigInteger memberId;
	/**Unique session id to record user logged in session CDS**/
	private BigInteger sessionId;
	/**The Account type that is being debited and credited**/
	private String accountType;
	/**Unique Id for the transaction **/
	private BigInteger pointTransId;
	/** source_cd - Name of the application because of which the transaction is being called (Example: Bunchball, MixedCodes, Interact)**/
	private String pointsSource;
	/**If multiple clientTransactionIDs are submitted then only the first transaction will be accepted and all other transactions matching the clientTransactionID will fail*/
	private String clientTransId;
	/**PROMOTION_ID Include the promotion variable for the name of the promotion**/
	private String promotionId;
	/**The amount of points that will be debited and credited**/
	private int pointsQuanity;
	/**The related transaction ID that will be included for tracking multiple CDS transactions **/
	private BigInteger relatedTransactionId;
	/**Unique stakeHolderId **/
	private String stakeHolderId;
	/***/
	private Integer itemId;
	/****/
	private Integer holdTime;
	
	private Date insertDt;
	
	private BigInteger 	Balance;
	
	 private String brandId;
	 
	private String errorDetail;
	
	private String key;
	
	private String statusLevel;

	public BigInteger getBalance() {
		return Balance;
	}

	public void setBalance(BigInteger balance) {
		Balance = balance;
	}

	public Date getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Date insertDt) {
		this.insertDt = insertDt;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getPointsType() {
		return pointsType;
	}
	public void setPointsType(String pointsType) {
		this.pointsType = pointsType;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public BigInteger getSessionId() {
		return sessionId;
	}
	public void setSessionId(BigInteger sessionId) {
		this.sessionId = sessionId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigInteger getPointTransId() {
		return pointTransId;
	}
	public void setPointTransId(BigInteger pointTransId) {
		this.pointTransId = pointTransId;
	}
	public String getClientTransId() {
		return clientTransId;
	}
	public void setClientTransId(String clientTransId) {
		this.clientTransId = clientTransId;
	}
	
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	public String getStakeHolderId() {
		return stakeHolderId;
	}
	public void setStakeHolderId(String stakeHolderId) {
		this.stakeHolderId = stakeHolderId;
	}
	public BigInteger getRelatedTransactionId() {
		return relatedTransactionId;
	}
	public void setRelatedTransactionId(BigInteger relatedTransactionId) {
		this.relatedTransactionId = relatedTransactionId;
	}
	public Integer getHoldTime() {
		return holdTime;
	}
	public void setHoldTime(Integer holdTime) {
		this.holdTime = holdTime;
	}
	public String getPointsSource() {
		return pointsSource;
	}
	public void setPointsSource(String pointsSource) {
		this.pointsSource = pointsSource;
	}
	public int getPointsQuanity() {
		return pointsQuanity;
	}
	public void setPointsQuanity(int pointsQuanity) {
		this.pointsQuanity = pointsQuanity;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getTransactionTimeStampString() {
		return transactionTimeStampString;
	}
	public void setTransactionTimeStampString(String transactionTimeStampString) {
		this.transactionTimeStampString = transactionTimeStampString;
	}
	
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	
	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	

	public String getStatusLevel() {
		return statusLevel;
	}

	public void setStatusLevel(String statusLevel) {
		this.statusLevel = statusLevel;
	}

	public String getSACAttributes(){
		return "[transactionTimeStampString=" + transactionTimeStampString
				+ ", pointsType="+ pointsType + ", reasonCode=" + reasonCode + ", productID="
				+ productID + ", memberId=" + memberId  + ", accountType=" + accountType
				+ ", pointsSource="+ pointsSource 
				+ ", promotionId=" + promotionId + ", pointsQuanity="
				+ pointsQuanity + ", stakeHolderId=" + stakeHolderId
				+ ", brandId=" + brandId + ", clientTransId=" + clientTransId+ "]";
	}
	@Override
	public String toString() {
		return "TransactionHistory [transactionDate=" + transactionDate
				+ ", transactionTimeStampString=" + transactionTimeStampString
				+ ", transactionType=" + transactionType + ", pointsType="
				+ pointsType + ", reasonCode=" + reasonCode + ", productID="
				+ productID + ", memberId=" + memberId + ", sessionId="
				+ sessionId + ", accountType=" + accountType
				+ ", pointTransId=" + pointTransId + ", pointsSource="
				+ pointsSource + ", clientTransId=" + clientTransId
				+ ", promotionId=" + promotionId + ", pointsQuanity="
				+ pointsQuanity + ", relatedTransactionId="
				+ relatedTransactionId + ", stakeHolderId=" + stakeHolderId
				+ ", itemId=" + itemId + ", holdTime=" + holdTime
				+ ", insertDt=" + insertDt + ", Balance=" + Balance
				+ ", brandId=" + brandId + ", errorDetail=" + errorDetail + "]";
	}

	
	
	
}
