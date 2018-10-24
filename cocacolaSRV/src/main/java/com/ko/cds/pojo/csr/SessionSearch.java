package com.ko.cds.pojo.csr;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Pojo for capturing the Session Search Request
 * @author ibm
 *
 */
public class SessionSearch implements Serializable{

	private static final long serialVersionUID = 17878L;
	
	private BigInteger memberId;
	private String 	ip ;
	private String 	userAgent ;
	private String 	sessionChannel ;
	private BigInteger 	siteId ;
	private String 	siteName ;
	private String 	sessionUUID ;
	private String 	janrainUUID ;
	private String 	firstName ;
	private String 	lastName ;
	
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getSessionChannel() {
		return sessionChannel;
	}
	public void setSessionChannel(String sessionChannel) {
		this.sessionChannel = sessionChannel;
	}
	public BigInteger getSiteId() {
		return siteId;
	}
	public void setSiteId(BigInteger siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	public String getJanrainUUID() {
		return janrainUUID;
	}
	public void setJanrainUUID(String janrainUUID) {
		this.janrainUUID = janrainUUID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return "SearchSessionObject [memberId=" + memberId + ", ip=" + ip
				+ ", userAgent=" + userAgent + ", sessionChannel="
				+ sessionChannel + ", siteId=" + siteId + ", siteName="
				+ siteName + ", sessionUUID=" + sessionUUID + ", janrainUUID="
				+ janrainUUID + ", firstName=" + firstName + ", lastName="
				+ lastName + "]";
	}
	


}
