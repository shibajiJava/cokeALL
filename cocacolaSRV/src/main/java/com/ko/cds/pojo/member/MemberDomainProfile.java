package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import net.sf.oval.constraint.MaxLength;


@JsonIgnoreProperties(value = { "domainProfileList","identifier" })
public class MemberDomainProfile implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger profileId;
	@MaxLength(value = 256, message = "member.create.violation")
	private String domainName;
	private BigInteger memberId;
	private Timestamp insertDt;
	private Timestamp updateDt;
	private String identifier;
	
	@MaxLength(value = 256, message = "member.create.violation")
	private String displayName;
	 
	private List<MemberDomainProfile> domainProfileList;
	
	
	
	public List<MemberDomainProfile> getDomainProfileList() {
		return domainProfileList;
	}
	public void setDomainProfileList(List<MemberDomainProfile> domainProfileList) {
		this.domainProfileList = domainProfileList;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public BigInteger getProfileId() {
		return this.profileId;
	}
	public void setProfileId(BigInteger profileId) {
		this.profileId = profileId;
	}
	public String getDomainName() {
		return this.domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public BigInteger getMemberId() {
		return this.memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public Timestamp getInsertDt() {
		return this.insertDt;
	}
	public void setInsertDt(Timestamp insertDt) {
		this.insertDt = insertDt;
	}
	public Timestamp getUpdateDt() {
		return this.updateDt;
	}
	public void setUpdateDt(Timestamp updateDt) {
		this.updateDt = updateDt;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	
	
}
