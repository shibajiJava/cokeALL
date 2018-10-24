package com.ko.cds.pojo.csr;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.utils.CustomUTCDateSerializer;
/***
 * This class is used to populate Member details in CSR Advance Search API
 * @author ibm
 *
 */
public class CSRMemberInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -371275098998897198L;
	/** Member ID for CDS **/
	private BigInteger memberId;
	/** Janrain UUID is a unique identifier used for synchronization  **/
	private String janrainUUID;
	/** Title of the member (Mr, MS, Mrs) **/
	private String title;
	/** Member First name **/
	private String firstName;
	/** Member middle name **/
	private String middleName;
	/** Member Last name **/
	private String lastName;
	/** Member alias*/
	private String alias;
	/** Member Suffix */
	private String suffix;
	/** The id of the house hold  */
	private String houseHoldId;
	/** The date the member was created in CDS / registered */
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String dateCreated;
	/** Returns the janrain and is the status at janrain */
	private String janrainAccountStatus;
	/** Date of primary email address verification*/
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String verifiedDate;
	/** Display name from janrain */
	private String displayName;
	/** List of external Ids for lite accounts matches janrain external ids*/
	private List<TagObject> memberIdentifiers;
	/** SMS object */
	private List<SMSNumber> smsNumbers;
	/** Address */
	private List<Address> addresses;
	/** Name value (example: Tiwtter, Facebook for searches  */
	private List<TagObject> socialProfiles;
	/** primary email  */
	private String primaryEmail;
	/** Date of birth  */
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String birthDate;
	/** An array of site objects to display all sites that an individual has seen or logged into*/
	private List<Site> sites;
	/** Date the user was deactivated – CSR tool will need to view for if a member is locked or not */
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String deactivatedDate;
	
	public List<Site> getSites() {
		return sites;
	}
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getJanrainUUID() {
		return janrainUUID;
	}
	public void setJanrainUUID(String janrainUUID) {
		this.janrainUUID = janrainUUID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
		
	public String getHouseHoldId() {
		return houseHoldId;
	}
	public void setHouseHoldId(String houseHoldId) {
		this.houseHoldId = houseHoldId;
	}
	public String getJanrainAccountStatus() {
		return janrainAccountStatus;
	}
	public void setJanrainAccountStatus(String janrainAccountStatus) {
		this.janrainAccountStatus = janrainAccountStatus;
	}
	
	public String getVerifiedDate() {
		return verifiedDate;
	}
	public void setVerifiedDate(String verifiedDate) {
		this.verifiedDate = verifiedDate;
	}
	public String getDeactivatedDate() {
		return deactivatedDate;
	}
	public void setDeactivatedDate(String deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public List<TagObject> getSocialProfiles() {
		return socialProfiles;
	}
	public void setSocialProfiles(List<TagObject> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}
	
	public List<TagObject> getMemberIdentifiers() {
		return memberIdentifiers;
	}
    
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setMemberIdentifiers(List<TagObject> memberIdentifiers) {
		this.memberIdentifiers = memberIdentifiers;
	}
	public List<SMSNumber> getSmsNumbers() {
		return smsNumbers;
	}
	public void setSmsNumbers(List<SMSNumber> smsNumbers) {
		this.smsNumbers = smsNumbers;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public String getPrimaryEmail() {
		return primaryEmail;
	}
	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}
	
	
	
}
