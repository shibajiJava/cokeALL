package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import net.sf.oval.constraint.MaxLength;

@JsonIgnoreProperties(value={"profileId","memberId","insertDt","updateDt"})
public class MemberSocialDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigInteger profileId;
	@MaxLength(value = 256, message = "member.create.violation")
	private String domain;
	private BigInteger memberId;
	private Timestamp insertDt;
	private Timestamp updateDt;
	private String identifier;
	
	@MaxLength(value = 256, message = "member.create.violation")
	private String userName;

	public BigInteger getProfileId() {
		return profileId;
	}

	public void setProfileId(BigInteger profileId) {
		this.profileId = profileId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	public Timestamp getInsertDt() {
		return insertDt;
	}

	public void setInsertDt(Timestamp insertDt) {
		this.insertDt = insertDt;
	}

	public Timestamp getUpdateDt() {
		return updateDt;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "MemberSocialDomain [profileId=" + profileId + ", domain="
				+ domain + ", memberId=" + memberId + ", insertDt=" + insertDt
				+ ", updateDt=" + updateDt + ", identifier=" + identifier
				+ ", userName=" + userName + "]";
	}
	 

}
