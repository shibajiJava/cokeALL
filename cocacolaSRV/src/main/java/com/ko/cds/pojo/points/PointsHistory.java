package com.ko.cds.pojo.points;

import java.io.Serializable;
import java.math.BigInteger;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;

/**
 * Pojo for Points History.
 * 
 * @author IBM
 * 
 */
public class PointsHistory implements Serializable {

	/**
	 * Serial version UUID.
	 */
	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String transactionDate;
	private String transactionType;
	private String pointsType;
	private Integer points;
	private String reasonCode;
	private String reasonDescription;
	private String productId;
	private Integer itemId;
	private String promotion;

	private String pointsSource;
	private BigInteger 	Balance;
	private String 	clientTransactionId;
	private String brandId;
	private String pointLevel;
	

	public String getPointsSource() {
		return pointsSource;
	}

	public void setPointsSource(String pointsSource) {
		this.pointsSource = pointsSource;
	}

	public BigInteger getBalance() {
		return Balance;
	}

	public void setBalance(BigInteger balance) {
		Balance = balance;
	}

	public String getClientTransactionId() {
		return clientTransactionId;
	}

	public void setClientTransactionId(String clientTransactionId) {
		this.clientTransactionId = clientTransactionId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
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

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	
	public String getPointLevel() {
		return pointLevel;
	}

	public void setPointLevel(String pointLevel) {
		this.pointLevel = pointLevel;
	}

	@Override
	public String toString() {
		return "PointsHistory [transactionDate=" + transactionDate
				+ ", transactionType=" + transactionType + ", pointsType="
				+ pointsType + ", points=" + points + ", reasonCode="
				+ reasonCode + ", reasonDescription=" + reasonDescription
				+ ", productId=" + productId + ", itemId=" + itemId
				+ ", promotion=" + promotion + ", pointsSource=" + pointsSource
				+ ", Balance=" + Balance + ", clientTransactionId="
				+ clientTransactionId + ", brandId=" + brandId
				+ ", pointLevel=" + pointLevel + "]";
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	

}
