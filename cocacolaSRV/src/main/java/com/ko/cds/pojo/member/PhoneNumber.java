package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.MaxLength;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 
 * @author IBM
 *
 */
@JsonIgnoreProperties(value={"phoneNumberId","memberId","statusCode","verified","phoneNumberList"})
public class PhoneNumber implements Serializable {

	public static final String BLNK_STR=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** member id*/
	private BigInteger memberId;
	/** phone number id**/
	
	private BigInteger phoneNumberId;
	@MaxLength(value=10,message="member.create.violation")
	@MatchPattern(pattern="Home|Work|Mobile",message="member.create.violation" )
	/** phoneType of phoneNumbers **/
	private String phoneType=BLNK_STR;
	@MaxLength(value=10,message="member.create.violation")
	/** phoneNumber of phoneNumbers**/
	private String phoneNumber=BLNK_STR;
	@MatchPattern(pattern="Y|N",message="member.create.violation" )
	@MaxLength(value=10,message="member.create.violation")
	/***/
	private String primaryIndicator=BLNK_STR;
	@MaxLength(value=10,message="member.create.violation")
	/** countryCode of phoneNumber **/
	private String countryCode=BLNK_STR;
	@MatchPattern(pattern="Active|InActive",message="member.create.violation" )
	/** phone status code*/
	private String statusCode="Active";
	
	private Timestamp verified;
	
	private List<PhoneNumber> phoneNumberList;
	
	
	public List<PhoneNumber> getPhoneNumberList() {
		return phoneNumberList;
	}
	public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}
	public Timestamp getVerified() {
		return verified;
	}
	public void setVerified(Timestamp verified) {
		this.verified = verified;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public BigInteger getPhoneNumberId() {
		return phoneNumberId;
	}
	public void setPhoneNumberId(BigInteger phoneNumberId) {
		this.phoneNumberId = phoneNumberId;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPrimaryIndicator() {
		return primaryIndicator;
	}
	public void setPrimaryIndicator(String primaryIndicator) {
		this.primaryIndicator = primaryIndicator;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
