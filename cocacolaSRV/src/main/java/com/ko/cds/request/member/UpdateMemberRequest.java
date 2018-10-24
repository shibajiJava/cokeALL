package com.ko.cds.request.member;

import java.math.BigInteger;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.pojo.member.MemberSocialDomain;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.utils.CDSConstants;

/**
 * 
 * @author ibm
 *
 */
public class UpdateMemberRequest {
	/** The unique session ID */
	@MaxLength(value=36,profiles={CDSConstants.UPDATE_MEMBER_PROFILE_NAME})
	private String sessionUUID;
	/** memberId of member table **/
	@NotNull(profiles={CDSConstants.UPDATE_MEMBER_PROFILE_NAME})
	private BigInteger memberId;
	/** holds sms number and sms type **/
	@AssertValid(profiles={CDSConstants.UPDATE_MEMBER_PROFILE_NAME})
	private SMSNumber smsObject;
	/** vending id*/
	@AssertValid(profiles={CDSConstants.UPDATE_MEMBER_PROFILE_NAME})
	private TagObject memberIdentifiers;
	
	@AssertValid(profiles={CDSConstants.UPDATE_MEMBER_PROFILE_NAME})
	private MemberSocialDomain socialDomain;
	
	

	public MemberSocialDomain getSocialDomain() {
		return socialDomain;
	}
	public void setSocialDomain(MemberSocialDomain socialDomain) {
		this.socialDomain = socialDomain;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public SMSNumber getSmsObject() {
		return smsObject;
	}
	public void setSmsObject(SMSNumber smsObject) {
		this.smsObject = smsObject;
	}
	
	public TagObject getMemberIdentifiers() {
		return memberIdentifiers;
	}
	public void setMemberIdentifiers(TagObject memberIdentifiers) {
		this.memberIdentifiers = memberIdentifiers;
	}
	public String getSessionUUID() {
		return sessionUUID;
	}
	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}
	
	
	

}
