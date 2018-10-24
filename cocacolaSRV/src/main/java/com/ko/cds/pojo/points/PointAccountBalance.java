package com.ko.cds.pojo.points;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
/****
 * 
 * @author IBM
 *
 */
public class PointAccountBalance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7936793601941792734L;
	private String creditOrDebitType;
	private String pointAccountType;
	private BigInteger memberId;
	private int pointBalance;
	private int weekCreditQty;
	private int weekBonusCreditQty;
	private int dailyCreditQty;
	private int dailyBonusCreditQty;
	private int yearlyCreditQty;
	private Timestamp lastActivityDt;
	private Timestamp insertDt;
	private String statusLevel;
	private String currentPointLevel;
	
	public Timestamp getInsertDt() {
		return insertDt;
	}
	public void setInsertDt(Timestamp insertDt) {
		this.insertDt = insertDt;
	}
	public Timestamp getLastActivityDt() {
		return lastActivityDt;
	}
	public void setLastActivityDt(Timestamp lastActivityDt) {
		this.lastActivityDt = lastActivityDt;
	}
	public String getCreditOrDebitType() {
		return creditOrDebitType;
	}
	public void setCreditOrDebitType(String creditOrDebitType) {
		this.creditOrDebitType = creditOrDebitType;
	}
	public String getPointAccountType() {
		return pointAccountType;
	}
	public void setPointAccountType(String pointAccountType) {
		this.pointAccountType = pointAccountType;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public int getPointBalance() {
		return pointBalance;
	}
	public void setPointBalance(int pointBalance) {
		this.pointBalance = pointBalance;
	}
	public int getWeekCreditQty() {
		return weekCreditQty;
	}
	public void setWeekCreditQty(int weekCreditQty) {
		this.weekCreditQty = weekCreditQty;
	}
	public int getWeekBonusCreditQty() {
		return weekBonusCreditQty;
	}
	public void setWeekBonusCreditQty(int weekBonusCreditQty) {
		this.weekBonusCreditQty = weekBonusCreditQty;
	}
	public int getDailyCreditQty() {
		return dailyCreditQty;
	}
	public void setDailyCreditQty(int dailyCreditQty) {
		this.dailyCreditQty = dailyCreditQty;
	}
	public int getDailyBonusCreditQty() {
		return dailyBonusCreditQty;
	}
	public void setDailyBonusCreditQty(int dailyBonusCreditQty) {
		this.dailyBonusCreditQty = dailyBonusCreditQty;
	}
	public int getYearlyCreditQty() {
		return yearlyCreditQty;
	}
	public void setYearlyCreditQty(int yearlyCreditQty) {
		this.yearlyCreditQty = yearlyCreditQty;
	}
	
	
	public String getStatusLevel() {
		return statusLevel;
	}
	public void setStatusLevel(String statusLevel) {
		this.statusLevel = statusLevel;
	}
	public String getCurrentPointLevel() {
		return currentPointLevel;
	}
	public void setCurrentPointLevel(String currentPointLevel) {
		this.currentPointLevel = currentPointLevel;
	}
	@Override
	public String toString() {
		return "PointAccountBalance [creditOrDebitType=" + creditOrDebitType
				+ ", pointAccountType=" + pointAccountType + ", memberId="
				+ memberId + ", pointBalance=" + pointBalance
				+ ", weekCreditQty=" + weekCreditQty + ", weekBonusCreditQty="
				+ weekBonusCreditQty + ", dailyCreditQty=" + dailyCreditQty
				+ ", dailyBonusCreditQty=" + dailyBonusCreditQty
				+ ", yearlyCreditQty=" + yearlyCreditQty + ", lastActivityDt="
				+ lastActivityDt + ", insertDt=" + insertDt + "]";
	}
}
