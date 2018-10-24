package com.ko.cds.pojo.csr;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.utils.CustomUTCDateSerializer;


public class ActivitySearch implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1098L;
	
	private BigInteger memberId ;
	private BigInteger	siteId ;
	private String siteName;
	private BigInteger	transactionId;
	private List<TagObject>	tags;
	
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String 	date ;
	
	
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public BigInteger getSiteId() {
		return siteId;
	}
	public void setSiteId(BigInteger siteId) {
		this.siteId = siteId;
	}
	
	public BigInteger getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(BigInteger transactionId) {
		this.transactionId = transactionId;
	}

	public List<TagObject> getTags() {
		return tags;
	}
	public void setTags(List<TagObject> tags) {
		this.tags = tags;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "SearchActivityObject [memberId=" + memberId + ", siteId="
				+ siteId + ", transactionId=" + transactionId + ", tags=" + tags
				+ ", date=" + date + "]";
	}
	

	
	
}
