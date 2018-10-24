package com.ko.cds.pojo.points;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;

public class AccountBalance {

	private int totalBalance;
	private int weeklyRegularBalance;
	private int weeklyRegularLimit;
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String pointsExpirationDate;
	private int weeklyBalanceTotal;

	private String pointAccountType;	
	private String pointAccountTypeName;
	private String statusLevel;
	private String statusPointsToNextLevel;
	private String statusNextLevel;
	
	public int getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(int totalBalance) {
		this.totalBalance = totalBalance;
	}

	public int getWeeklyRegularBalance() {
		return weeklyRegularBalance;
	}

	public void setWeeklyRegularBalance(int weeklyRegularBalance) {
		this.weeklyRegularBalance = weeklyRegularBalance;
	}

	public int getWeeklyRegularLimit() {
		return weeklyRegularLimit;
	}

	public void setWeeklyRegularLimit(int weeklyRegularLimit) {
		this.weeklyRegularLimit = weeklyRegularLimit;
	}

	public String getPointsExpirationDate() {
		return pointsExpirationDate;
	}

	public void setPointsExpirationDate(String pointsExpirationDate) {
		this.pointsExpirationDate = pointsExpirationDate;
	}

	public int getWeeklyBalanceTotal() {
		return weeklyBalanceTotal;
	}

	public void setWeeklyBalanceTotal(int weeklyBalanceTotal) {
		this.weeklyBalanceTotal = weeklyBalanceTotal;
	}

	public String getPointAccountType() {
		return pointAccountType;
	}

	public void setPointAccountType(String pointAccountType) {
		this.pointAccountType = pointAccountType;
	}

	public String getPointAccountTypeName() {
		return pointAccountTypeName;
	}

	public void setPointAccountTypeName(String pointAccountTypeName) {
		this.pointAccountTypeName = pointAccountTypeName;
	}

	public String getStatusLevel() {
		return statusLevel;
	}

	public void setStatusLevel(String statusLevel) {
		this.statusLevel = statusLevel;
	}

	public String getStatusPointsToNextLevel() {
		return statusPointsToNextLevel;
	}

	public void setStatusPointsToNextLevel(String statusPointsToNextLevel) {
		this.statusPointsToNextLevel = statusPointsToNextLevel;
	}

	public String getStatusNextLevel() {
		return statusNextLevel;
	}

	public void setStatusNextLevel(String statusNextLevel) {
		this.statusNextLevel = statusNextLevel;
	}

}
