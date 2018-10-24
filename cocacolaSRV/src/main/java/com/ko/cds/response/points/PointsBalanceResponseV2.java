package com.ko.cds.response.points;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ko.cds.pojo.points.AccountBalance;
import com.ko.cds.pojo.points.AccountConfiguration;
import com.ko.cds.pojo.points.StatusLevels;

/**
 * Response object for PointsBalance api Version 2.
 * 
 * @author IBM
 * 
 */
public class PointsBalanceResponseV2 implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	private List<AccountBalance> accountBalaces;
	
	private List<AccountConfiguration> accountConfiguration ;

	private String memberId;
	
	public PointsBalanceResponseV2(){
		accountBalaces = new ArrayList<AccountBalance>();
		accountConfiguration = new ArrayList<AccountConfiguration>();
	}

	public List<AccountConfiguration> getAccountConfiguration() {
		return accountConfiguration;
	}

	public void setAccountConfiguration(
			List<AccountConfiguration> accountConfiguration) {
		this.accountConfiguration = accountConfiguration;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public List<AccountBalance> getAccountBalaces() {
		return accountBalaces;
	}

	public void setAccountBalaces(List<AccountBalance> accountBalaces) {
		this.accountBalaces = accountBalaces;
	}


	
	

}
