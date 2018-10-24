package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.MaxLength;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;






import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.utils.CustomDateDeserializer;

/**
 * @author IBM
 * 
 */
@JsonIgnoreProperties(value = { "deactivatedDate","cdsoProfileInterest","updateDate","aboutMe","deleteReason","originatingClient","emailVerified","legalAcceptances","clients","proflies","authStatus","statusUpdateDTM","janrainEmailAddress","created","accountStatus" })
public class MemberInfo implements Serializable {

	private Map<String, String> other = new HashMap<String, String>();

	public static final String BLNK_STR = null;
	/**
	 * Memmer Info POJO
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*** sessionUUID ***/
	// private String sessionUUID;
	/** memberId of member table **/
	private BigInteger memberId;
	@MaxLength(value = 10, message = "member.create.violation")
	/** Title of the member (Mr, MS, Mrs)*/
	private String title = BLNK_STR;
	@MaxLength(value = 256, message = "member.create.violation")
	/** first name of the member**/
	private String firstName = BLNK_STR;
	@MaxLength(value = 50, message = "member.create.violation")
	/** middle name of member */
	private String middleName = BLNK_STR;
	@MaxLength(value = 256, message = "member.create.violation")
	/** last name of member*/
	private String lastName = BLNK_STR;
	@MaxLength(value = 513, message = "member.create.violation")
	/** displayName of member*/
	private String displayName = "";
	@MaxLength(value = 250, message = "member.create.violation")
	/** alias of member **/
	private String alias = BLNK_STR;
	@MaxLength(value = 10, message = "member.create.violation")
	/** suffix of member **/
	private String suffix = BLNK_STR;
	@MaxLength(value = 1, message = "member.create.violation")
	@MatchPattern(pattern = "[MF]", message = "member.create.violation")
	/** gendenCode of member */
	private String genderCode = BLNK_STR;
	/** birthday of member */
	@JsonDeserialize(using=CustomDateDeserializer.class)
	private String birthday = BLNK_STR;
	@MaxLength(value = 20, message = "member.create.violation")
	/** langugeCode of member*/
	private String languageCode = BLNK_STR;
	@MaxLength(value = 2, message = "member.create.violation")
	/**country of member*/
	private String country = BLNK_STR;
	@MaxLength(value = 10, message = "member.create.violation")
	@MatchPattern(pattern = "Active|InActive|delete", message = "member.create.violation")
	/** memberStatus of member*/
	private String memberStatus = BLNK_STR;
	@AssertValid
	/** A member can contains more than one address**/
	private List<Address> addresses;
	@AssertValid
	/** A member can contains more than one phone numbers**/
	private List<PhoneNumber> phoneNumbers;
	@Email(message = "member.create.violation")
	@MaxLength(value = 256, message = "member.create.violation")
	private String emailAddress = BLNK_STR;
	@AssertValid
	/** Assuming one member can contain more than one member SMS **/
	private List<SMSNumber> smsNumbers;
	

	
	// private SMSNumber smsNumber;
	@AssertValid
	/** Assuming one member can contain more than one member indentifier **/
	private List<MemberIdentifier> memberIdentifiers;
	
	private Date created;
	
	private String accountStatus;
	
	@AssertValid
	private List<MemberSocialDomain> socialDomain; 
	
	private List<TagObject> socialProfiles;



	public List<TagObject> getSocialProfiles() {
		return socialProfiles;
	}

	public void setSocialProfiles(List<TagObject> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}

	public List<MemberSocialDomain> getSocialDomain() {
		return socialDomain;
	}

	public void setSocialDomain(List<MemberSocialDomain> socialDomain) {
		this.socialDomain = socialDomain;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}




	private List<com.ko.cds.pojo.member.Email> janrainEmailAddress;
	// TODO get field details

	

	public List<com.ko.cds.pojo.member.Email> getJanrainEmailAddress() {
		return janrainEmailAddress;
	}

	public void setJanrainEmailAddress(
			List<com.ko.cds.pojo.member.Email> janrainEmailAddress) {
		this.janrainEmailAddress = janrainEmailAddress;
	}




	/** Source_Code of member table */
	// private String source=BLNK_STR;
	/** Authendication_Status_Code of member table */
	// private String authenticationStatusCode;
	@MatchPattern(pattern = "Individual|Organization|SMS|Rewardcard", message = "member.create.violation")
	/** Member_Type_Code for member table */
	private String memberType = BLNK_STR;
	@MaxLength(value = 36, message = "member.create.violation")
	/** janrain UUID in member table*/
	private String uuid = BLNK_STR;
	
	private Timestamp updateDate;
	
	private String aboutMe; 
	
	private String deleteReason; 

	private String originatingClient;

	private Timestamp emailVerified;
	
	private Timestamp deactivatedDate;
	
	public Timestamp getDeactivatedDate() {
		return deactivatedDate;
	}

	public void setDeactivatedDate(Timestamp deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}




	@AssertValid
	/** Assuming one member can contain more than one member indentifier **/
	private List<LegalAcceptances> legalAcceptances;
	@AssertValid
	/** Assuming one member can contain more than one member indentifier **/
	private List<Client> clients;
	@AssertValid
	/** Assuming one member can contain more than one member indentifier **/
	private List<MemberDomainProfile> proflies;
   
	private String authStatus;
	
	

	private Timestamp statusUpdateDTM;
	
	/*private com.ko.cds.pojo.member.Email email;
	
	public com.ko.cds.pojo.member.Email getEmail() {
		return email;
	}

	public void setEmail(com.ko.cds.pojo.member.Email email) {
		this.email = email;
	}*/
	
	
	/** janrain encripted password **/
	// private String password;

	public String getTitle() {
		return title;
	}

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	/*
	 * public String getSource() { return source; } public void setSource(String
	 * source) { this.source = source; }
	 */

	/*
	 * public String getAuthenticationStatusCode() { return
	 * authenticationStatusCode; } public void
	 * setAuthenticationStatusCode(String authenticationStatusCode) {
	 * this.authenticationStatusCode = authenticationStatusCode; }
	 */
	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/*
	 * public String getPassword() { return password; } public void
	 * setPassword(String password) { this.password = password; }
	 */

	public List<Address> getAddresses() {
		return addresses;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<MemberIdentifier> getMemberIdentifiers() {
		return memberIdentifiers;
	}

	public void setMemberIdentifiers(List<MemberIdentifier> memberIdentifiers) {
		this.memberIdentifiers = memberIdentifiers;
	}

	/*
	 * public String getSessionUUID() { return sessionUUID; } public void
	 * setSessionUUID(String sessionUUID) { this.sessionUUID = sessionUUID; }
	 */

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
	
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    
    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getOriginatingClient() {
        return originatingClient;
    }

    public void setOriginatingClient(String originatingClient) {
        this.originatingClient = originatingClient;
    }
    
    public Timestamp getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Timestamp emailVerified) {
        this.emailVerified = emailVerified;
    }

    
	public List<SMSNumber> getSmsNumbers() {
		return smsNumbers;
	}

	public void setSmsNumbers(List<SMSNumber> smsNumbers) {
		this.smsNumbers = smsNumbers;
	}

	
	public List<LegalAcceptances> getLegalAcceptances() {
		return legalAcceptances;
	}

	public void setLegalAcceptances(List<LegalAcceptances> legalAcceptances) {
		this.legalAcceptances = legalAcceptances;
	}
	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public List<MemberDomainProfile> getProflies() {
		return proflies;
	}

	public void setProflies(List<MemberDomainProfile> proflies) {
		this.proflies = proflies;
	}
	
	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public Timestamp getStatusUpdateDTM() {
		return statusUpdateDTM;
	}

	public void setStatusUpdateDTM(Timestamp statusUpdateDTM) {
		this.statusUpdateDTM = statusUpdateDTM;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
	
	@Override
	public String toString() {
				
		return "MemberInfo [other=" + other + ", memberId=" + memberId
				+ ", title=" + title + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName
				+ ", displayName=" + displayName + ", alias=" + alias
				+ ", suffix=" + suffix + ", genderCode=" + genderCode
				+ ", birthday=" + birthday + ", languageCode=" + languageCode
				+ ", country=" + country + ", memberStatus=" + memberStatus
				+ ", addresses=" + addresses + ", phoneNumbers=" + phoneNumbers
				+ ", emailAddress=" + emailAddress + ", smsNumbers="
				+ smsNumbers + ", memberIdentifiers=" + memberIdentifiers
				+ ", memberType=" + memberType + ", uuid=" + uuid
				+ ", updateDate=" + updateDate + ", aboutMe=" + aboutMe
				+ ", deleteReason=" + deleteReason + ", originatingClient="
				+ originatingClient + ", emailVerified=" + emailVerified
				+ ", legalAcceptances=" + legalAcceptances + ", clients=" + clients + ", proflies=" + proflies
				+ "]";
	}

}
