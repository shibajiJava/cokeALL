package com.ko.cds.response.points;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;

/**
 * Response object for PointsBalance api.
 * 
 * @author IBM
 * 
 */
@JsonIgnoreProperties(value = { "memberId"})
public class PointsBalanceResponse implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	private int totalBalance;
	private int weeklyRegularBalance;
	private int weeklyRegularLimit;
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String pointsExpirationDate;
	private int weeklyBalanceTotal;

	private String memberId;
	
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	

}
