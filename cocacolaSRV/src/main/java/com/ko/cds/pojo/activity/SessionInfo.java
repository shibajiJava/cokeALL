package com.ko.cds.pojo.activity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * 
 * @author IBM
 * 
 */
public class SessionInfo implements Serializable {

	/**
	 * Session Info POJO
	 */
	private static final long serialVersionUID = 1L;
	@NotNull
	@MaxLength(value=32)
	private String sessionID;
	@NotNull
	@MatchPattern(pattern="MM/dd/yyyy")
	private Date sessionDate;
	@NotNull
	private BigInteger memberId;
	@NotNull
	private BigInteger siteID;
//	private String brandCode;
	@MaxLength(value=32)
	private String ipAddress;
	@MaxLength(value=250)
	private String userAgent;
	@MaxLength(value=250)
	private String referring;
	
	private String sessionChannel;
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionId(String sessionID) {
		this.sessionID = sessionID;
	}

	public Date getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
	}

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	public BigInteger getSiteID() {
		return siteID;
	}

	public void setSiteId(BigInteger siteID) {
		this.siteID = siteID;
	}

//	public String getBrandCode() {
//		return brandCode;
//	}
//
//	public void setBrandCode(String brandCode) {
//		this.brandCode = brandCode;
//	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getReferring() {
		return referring;
	}

	public void setReferring(String referring) {
		this.referring = referring;
	}

	public String getSessionChannel() {
		return sessionChannel;
	}

	public void setSessionChannel(String sessionChannel) {
		this.sessionChannel = sessionChannel;
	}

}
