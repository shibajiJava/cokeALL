package com.ko.cds.pojo.csr;

import java.io.Serializable;
import java.math.BigInteger;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CustomUTCDateSerializer;
@JsonIgnoreProperties(value = { "memberId"})
public class Site implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2941063212706898813L;
	
	private BigInteger siteId;
	private String siteName;
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String lastLoginDate;
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String firstLoginDate;
	private BigInteger memberId;
	
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
    
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getFirstLoginDate() {
		return firstLoginDate;
	}
	public void setFirstLoginDate(String firstLoginDate) {
		this.firstLoginDate = firstLoginDate;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	

}
