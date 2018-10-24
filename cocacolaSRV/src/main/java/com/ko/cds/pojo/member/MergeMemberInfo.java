package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.MaxLength;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author IBM
 * 
 */
@JsonIgnoreProperties(value = { "memberId" ,"updateDate","aboutMe","deleteReason","originatingClient","emailVerified" })
public class MergeMemberInfo implements Serializable {

	private Map<String, String> other = new HashMap<String, String>();

	public static final String BLNK_STR = null;
	
	private static final long serialVersionUID = 1L;

	/** memberId of member table **/
	private BigInteger memberId;

	private String uuid = BLNK_STR;
	
	
	private com.ko.cds.pojo.member.Email emailAddress;

	@MaxLength(value = 10, message = "member.create.violation")
	/*** A member can contains one SMS number*/
	
	@AssertValid
	private List<SMSNumber> smsNumbers;
	
	@AssertValid
	/** Assuming one member can contain more than one member indentifier **/
	private List<MemberIdentifier> memberIdentifiers;
	
	private List<CommunicationOptIn> comOpts;
	
	private List<MemberSocialDomain> socialDomains;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}

	public com.ko.cds.pojo.member.Email getEmail() {
		return emailAddress;
	}

	public void setEmail(com.ko.cds.pojo.member.Email emailAddress) {
		this.emailAddress = emailAddress;
	}


	public List<SMSNumber> getSmsNumbers() {
		return smsNumbers;
	}

	public void setSmsNumbers(List<SMSNumber> smsNumbers) {
		this.smsNumbers = smsNumbers;
	}

	public List<MemberIdentifier> getMemberIdentifiers() {
		return memberIdentifiers;
	}

	public void setMemberIdentifiers(List<MemberIdentifier> memberIdentifiers) {
		this.memberIdentifiers = memberIdentifiers;
	}

	@JsonAnyGetter
	public Map<String, String> any() {
		return other;
	}

	@JsonAnySetter
	public void set(String name, String value) {
		other.put(name, value);
	}

	public boolean hasUnknowProperties() {
		return !other.isEmpty();
	}

	public List<CommunicationOptIn> getComOpts() {
		return comOpts;
	}

	public void setComOpts(List<CommunicationOptIn> comOpts) {
		this.comOpts = comOpts;
	}

	public List<MemberSocialDomain> getSocialDomains() {
		return socialDomains;
	}

	public void setSocialDomains(List<MemberSocialDomain> socialDomains) {
		this.socialDomains = socialDomains;
	}
	

}
