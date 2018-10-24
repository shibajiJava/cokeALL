package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.MaxLength;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/***
 * 
 * @author IBM
 *
 *
 */

@JsonIgnoreProperties(value={"addressId","memberId","memberAddressListJanrain"})

public class Address implements Serializable {
    
	public static final String BLNK_STR= null;
	/**
	 * Adress pojo
	 */
	private static final long serialVersionUID = 1L;
	
	/** id of member**/
	private BigInteger memberId;
	@MaxLength(value=10,message="member.create.violation")
	@MatchPattern(pattern="Home|Work|Shipping|Mailing",message="member.create.violation" )
	/** Address Type**/
	private String addressType=BLNK_STR;
	@MaxLength(value=256,message="member.create.violation")
	/** Street_Address_1 of address table **/
	private String streetAddress1=BLNK_STR;
	@MaxLength(value=256,message="member.create.violation")
	/** Street_Address_2 of address table**/
	private String streetAddress2=BLNK_STR;
	@MaxLength(value=256,message="member.create.violation")
	/** city of  address**/
	private String city=BLNK_STR;
	@MaxLength(value=10,message="member.create.violation")
	/** state of address**/
	private String state=BLNK_STR;
	@MaxLength(value=10,message="member.create.violation")
	/** postalCode of address **/
	private String postalCode=BLNK_STR;
	@MaxLength(value=2,message="member.create.violation")
	/** country of address**/
	private String country=BLNK_STR;
	@MaxLength(value=10,message="member.create.violation")
	
	private List<Address> memberAddressListJanrain ;
	
	
	public List<Address> getMemberAddressListJanrain() {
		return memberAddressListJanrain;
	}
	public void setMemberAddressListJanrain(List<Address> memberAddressListJanrain) {
		this.memberAddressListJanrain = memberAddressListJanrain;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getStreetAddress1() {
		return streetAddress1;
	}
	public void setStreetAddress1(String streetAddress1) {
		this.streetAddress1 = streetAddress1;
	}
	
	public String getStreetAddress2() {
		return streetAddress2;
	}
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	
	
}
