package com.ko.cds.request.points;

import java.io.Serializable;
import java.math.BigInteger;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * Request object for points balance api for Version 2.
 * 
 * @author IBM
 * 
 */
public class PointsBalanceRequestV2 implements Serializable {

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

}
