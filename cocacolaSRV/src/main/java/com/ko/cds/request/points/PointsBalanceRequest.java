package com.ko.cds.request.points;

import java.io.Serializable;
import java.math.BigInteger;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * Request object for points balance api.
 * 
 * @author IBM
 * 
 */
public class PointsBalanceRequest implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	
	@QueryParam("sessionUUID")
	@MaxLength(value = 36)
	private String sessionUUID;	
	@QueryParam("memberId")
	@NotNull
	private BigInteger memberId;
	@QueryParam("accountType")
	@NotNull
	@MaxLength(value = 10)
	private String accountType;

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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

}
