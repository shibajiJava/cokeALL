package com.ko.cds.request.activity;

import java.math.BigInteger;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.ko.cds.utils.CustomTimestampDeserializer;

import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

/**
 * 
 * @author IBM
 * 
 */
public class RecordSessionInfoRequest {

	@NotNull
	@MaxLength(value = 36)
	private String sessionUUID;
	@NotNull
	@JsonDeserialize(using = CustomTimestampDeserializer.class)
	private String sessionDate;
	@NotNull
	private BigInteger memberId;
	@NotNull
	private Integer siteId;
	// private String brandCode;
	@MaxLength(value = 32)
	private String ipAddress;
	@MaxLength(value = 250)
	private String userAgent;
	@MaxLength(value = 250)
	private String referring;

	private String sessionChannel;
	
	public String getSessionUUID() {
		return sessionUUID;
	}

	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}

	public String getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(String sessionDate) {
		this.sessionDate = sessionDate;
	}

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	// public String getBrandCode() {
	// return brandCode;
	// }
	//
	// public void setBrandCode(String brandCode) {
	// this.brandCode = brandCode;
	// }

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
