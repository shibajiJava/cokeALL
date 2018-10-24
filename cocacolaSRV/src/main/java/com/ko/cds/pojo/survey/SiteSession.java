package com.ko.cds.pojo.survey;

import java.io.Serializable;
import java.math.BigInteger;

import net.sf.oval.constraint.MaxLength;

public class SiteSession implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@MaxLength(value=36)
	private String sessionUuid;
	@MaxLength(value=32)
	private BigInteger sessionId;
	public String getSessionUuid() {
		return sessionUuid;
	}
	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
	}
	public BigInteger getSessionId() {
		return sessionId;
	}
	public void setSessionId(BigInteger sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
