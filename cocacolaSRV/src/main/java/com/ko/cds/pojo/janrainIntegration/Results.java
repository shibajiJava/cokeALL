
package com.ko.cds.pojo.janrainIntegration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "result_count","password","janrain","legalAcceptance" })
public class Results{
	private Map<String, String> other = new HashMap<String, String>();
   	private String aboutMe;
   	private String mobileNumberDateVerified;
   	private String mobileNumber;
   	private String mobileNumberCountryCode;
   	private String accountStatus;
   	private String birthday;
   	private List<Clients> clients;
   	private List<CommunicationOpt> communicationOpts;
   	private String created;
   	private String currentLocation;
   	private String deactivateAccount;
   	private String deleteReason;
   	private String display;
   	private String displayName;
   	private String email;
   	private String emailStatus;
   	private String emailVerified;
   	private List<Emails> emails;
   	private List<ExternalIds> externalIds;
   	private String familyName;
   	private String gender;
   	private String givenName;
   	private Number id;
   	private String language;
   	private String lastLogin;
   	private String lastUpdated;
   	private String lastUpdatedClientId;
   	private List<LegalAcceptance> legalAcceptances;
   	private MailingAddress mailingAddress;
   	private String middleName;
   	private String originatingClient;
   	private String password;
   	private List<PhoneNumberJanrain> phoneNumbers;
   	private List photos;
   	private List post_login_confirmation;
   	private List<Profiles> profiles;
   	private Residency residency;
   	private ShippingAddress shippingAddress;
   	private List statuses;
   	private String uuid;
   	private String deactivatedDate;
   	
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
   	
	public String getDeactivatedDate() {
		return deactivatedDate;
	}
	public void setDeactivatedDate(String deactivatedDate) {
		this.deactivatedDate = deactivatedDate;
	}
	public String getAboutMe(){
		return this.aboutMe;
	}
	public void setAboutMe(String aboutMe){
		this.aboutMe = aboutMe;
	}
 	public String getAccountStatus(){
		return this.accountStatus;
	}
	public void setAccountStatus(String accountStatus){
		this.accountStatus = accountStatus;
	}
 	public String getBirthday(){
		return this.birthday;
	}
	public void setBirthday(String birthday){
		this.birthday = birthday;
	}
 	public List<Clients> getClients(){
		return this.clients;
	}
	public void setClients(List<Clients> clients){
		this.clients = clients;
	}
 	public List<CommunicationOpt> getCommunicationOpts(){
		return this.communicationOpts;
	}
	public void setCommunicationOpts(List<CommunicationOpt> communicationOpts){
		this.communicationOpts = communicationOpts;
	}
 	public String getCreated(){
		return this.created;
	}
	public void setCreated(String created){
		this.created = created;
	}
 	public String getCurrentLocation(){
		return this.currentLocation;
	}
	public void setCurrentLocation(String currentLocation){
		this.currentLocation = currentLocation;
	}
 	public String getDeactivateAccount(){
		return this.deactivateAccount;
	}
	public void setDeactivateAccount(String deactivateAccount){
		this.deactivateAccount = deactivateAccount;
	}
 	public String getDeleteReason(){
		return this.deleteReason;
	}
	public void setDeleteReason(String deleteReason){
		this.deleteReason = deleteReason;
	}
 	public String getDisplay(){
		return this.display;
	}
	public void setDisplay(String display){
		this.display = display;
	}
 	public String getDisplayName(){
		return this.displayName;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
 	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
 	public String getEmailStatus(){
		return this.emailStatus;
	}
	public void setEmailStatus(String emailStatus){
		this.emailStatus = emailStatus;
	}
 	public String getEmailVerified(){
		return this.emailVerified;
	}
	public void setEmailVerified(String emailVerified){
		this.emailVerified = emailVerified;
	}
 	public List<Emails> getEmails(){
		return this.emails;
	}
	public void setEmails(List<Emails> emails){
		this.emails = emails;
	}
 	public List<ExternalIds> getExternalIds(){
		return this.externalIds;
	}
	public void setExternalIds(List<ExternalIds> externalIds){
		this.externalIds = externalIds;
	}
 	public String getFamilyName(){
		return this.familyName;
	}
	public void setFamilyName(String familyName){
		this.familyName = familyName;
	}
 	public String getGender(){
		return this.gender;
	}
	public void setGender(String gender){
		this.gender = gender;
	}
 	public String getGivenName(){
		return this.givenName;
	}
	public void setGivenName(String givenName){
		this.givenName = givenName;
	}
 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public String getLanguage(){
		return this.language;
	}
	public void setLanguage(String language){
		this.language = language;
	}
 	public String getLastLogin(){
		return this.lastLogin;
	}
	public void setLastLogin(String lastLogin){
		this.lastLogin = lastLogin;
	}
 	public String getLastUpdated(){
		return this.lastUpdated;
	}
	public void setLastUpdated(String lastUpdated){
		this.lastUpdated = lastUpdated;
	}
 	public String getLastUpdatedClientId(){
		return this.lastUpdatedClientId;
	}
	public void setLastUpdatedClientId(String lastUpdatedClientId){
		this.lastUpdatedClientId = lastUpdatedClientId;
	}
 	public List<LegalAcceptance> getLegalAcceptances(){
		return this.legalAcceptances;
	}
	public void setLegalAcceptances(List<LegalAcceptance> legalAcceptances){
		this.legalAcceptances = legalAcceptances;
	}
 	public MailingAddress getMailingAddress(){
		return this.mailingAddress;
	}
	public void setMailingAddress(MailingAddress mailingAddress){
		this.mailingAddress = mailingAddress;
	}
 	public String getMiddleName(){
		return this.middleName;
	}
	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}
 	public String getOriginatingClient(){
		return this.originatingClient;
	}
	public void setOriginatingClient(String originatingClient){
		this.originatingClient = originatingClient;
	}
 	public String getPassword(){
		return this.password;
	}
	public void setPassword(String password){
		this.password = password;
	}
 	public List<PhoneNumberJanrain> getPhoneNumbers(){
		return this.phoneNumbers;
	}
	public void setPhoneNumbers(List<PhoneNumberJanrain> phoneNumbers){
		this.phoneNumbers = phoneNumbers;
	}
 	public List getPhotos(){
		return this.photos;
	}
	public void setPhotos(List photos){
		this.photos = photos;
	}
 	public List getPost_login_confirmation(){
		return this.post_login_confirmation;
	}
	public void setPost_login_confirmation(List post_login_confirmation){
		this.post_login_confirmation = post_login_confirmation;
	}
 	public List<Profiles> getProfiles(){
		return this.profiles;
	}
	public void setProfiles(List<Profiles> profiles){
		this.profiles = profiles;
	}
 	public Residency getResidency(){
		return this.residency;
	}
	public void setResidency(Residency residency){
		this.residency = residency;
	}
 	public ShippingAddress getShippingAddress(){
		return this.shippingAddress;
	}
	public void setShippingAddress(ShippingAddress shippingAddress){
		this.shippingAddress = shippingAddress;
	}
 	public List getStatuses(){
		return this.statuses;
	}
	public void setStatuses(List statuses){
		this.statuses = statuses;
	}
 	public String getUuid(){
		return this.uuid;
	}
	public void setUuid(String uuid){
		this.uuid = uuid;
	}
	public String getMobileNumberDateVerified() {
		return mobileNumberDateVerified;
	}
	public void setMobileNumberDateVerified(String mobileNumberDateVerified) {
		this.mobileNumberDateVerified = mobileNumberDateVerified;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getMobileNumberCountryCode() {
		return mobileNumberCountryCode;
	}
	public void setMobileNumberCountryCode(String mobileNumberCountryCode) {
		this.mobileNumberCountryCode = mobileNumberCountryCode;
	}
	
}
