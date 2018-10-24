package com.ko.cds.test.dao;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Email;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.request.member.MemberSearchRequest;
import com.ko.cds.request.member.UpdateMemberRequest;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CDSConstants;



 @Component
 @Transactional
public class MemberDAOTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(MemberDAOTest.class);
	@Autowired 
	private MemberDAO memberDAO;
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO;
	
	 @Test
	 public void testSearchMemberByMemberID(){
		 MemberSearchRequest searchRequest = new MemberSearchRequest();
		 searchRequest.setMemberStatus("Active");
		// searchRequest.setSearchParameterName("UUID");
		 //searchRequest.setSearchParameterValue("ere0-4344-dddd-4444r");
		 searchRequest.setSearchParameterName("memberId");
		 searchRequest.setSearchParameterValue("1");
		 
		 
		 MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		 Assert.assertEquals(new BigInteger("1"), member.getMemberId());
		  System.out.println("test");
	 }
	 
	 @Test
	 public void testSearchMemberByUUID(){
		 MemberSearchRequest searchRequest = new MemberSearchRequest();
		 searchRequest.setMemberStatus("Active");
		 searchRequest.setSearchParameterName("janrainUUID");
		 searchRequest.setSearchParameterValue("12gw-absd");
		 //searchRequest.setSearchParameterName("CDSMemberID");
		 //searchRequest.setSearchParameterValue("1");
		 
		 
		 MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		 Assert.assertEquals("12gw-absd", member.getUuid());
		  System.out.println("test");
	 }
	 
	 @Test
	 public void testSearchMemberByIDwithOutStatus(){
		 MemberSearchRequest searchRequest = new MemberSearchRequest();
		 //searchRequest.setMemberStatus("active");
		 searchRequest.setSearchParameterName("memberId");
		 searchRequest.setSearchParameterValue("1");
		 //searchRequest.setSearchParameterName("CDSMemberID");
		 //searchRequest.setSearchParameterValue("1");
		 
		 
		 MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		 Assert.assertEquals(new BigInteger("1"), member.getMemberId());
		  System.out.println("test");
	 }
	 @Test
	 public void testSearchMemberByEmail(){
		 MemberSearchRequest searchRequest = new MemberSearchRequest();
		 searchRequest.setMemberStatus("Active");
		 searchRequest.setSearchParameterName("email");
		 searchRequest.setSearchParameterValue("joe10@in.ibm.com");
		 //searchRequest.setSearchParameterName("CDSMemberID");
		 //searchRequest.setSearchParameterValue("1");
		 
		
		 MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		 Assert.assertEquals("joe10@in.ibm.com", member.getEmailAddress());
		  System.out.println("test");
	 }
	 
	 @Test
	 public void testSearchSMSNumber(){
		 MemberSearchRequest searchRequest = new MemberSearchRequest();
		 searchRequest.setMemberStatus("Active");
		 searchRequest.setSearchParameterName("smsNumber");
		 searchRequest.setSearchParameterValue("5555");
		 //searchRequest.setSearchParameterName("CDSMemberID");
		 //searchRequest.setSearchParameterValue("1");
		 
		 
		 MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		 Assert.assertEquals("5555", member.getSmsNumbers().get(0).getSmsNumber().toString());
		 Assert.assertEquals("SMS", member.getSmsNumbers().get(0).getSmsType());
		  System.out.println("test");
	 }
	 @Test
	 public void testSearchByVendingMachine(){
		 MemberSearchRequest searchRequest = new MemberSearchRequest();
		 searchRequest.setMemberStatus("Active");
		 searchRequest.setSearchParameterName("Twitter");
		 searchRequest.setSearchParameterValue("TESTID");
		 //searchRequest.setSearchParameterName("CDSMemberID");
		 //searchRequest.setSearchParameterValue("1");
		 
		
		 MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		 Assert.assertEquals("TESTID", ((MemberIdentifier)member.getMemberIdentifiers().get(0)).getUserId());
		  System.out.println("test");
	 }
	 @Test
	 @Rollback(true)
	 public void testInsertMemberDetails(){
		 MemberInfo memberInfo = new MemberInfo();
		 memberInfo.setTitle("Mr");
		 memberInfo.setFirstName("Upananda");
		 memberInfo.setLastName("Saha");
		 memberInfo.setDisplayName("Upananda Saha");
		 memberInfo.setGenderCode("M");
		 memberInfo.setBirthday("01/01/1992");
		 memberInfo.setLanguageCode("EN");
		 memberInfo.setCountry("IN");
		 memberInfo.setMemberStatus("Active");
		 memberInfo.setUuid("ere0-4344-dddd-4444t");
		
		 BigInteger memberId=sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber(CDSConstants.MEMBER_ID_SEQ));
		 memberInfo.setMemberId(memberId);
		 memberDAO.insertMemberInfo(memberInfo);
		//Insert Email
		 Email email = new Email();
		 email.setMemberId(memberId);
		 email.setEmail("test@gmail.com");
		 memberDAO.insertEmail(email);
		 //Insert Address
		 Address address= new Address();
		 address.setMemberId(memberId);
		 address.setAddressType(CDSConstants.ADDRESS_TYPE_HOME);
		 address.setCity("Kolkata");
		 address.setCountry("IN");
		 address.setPostalCode("700099");
		 address.setState("WB");
		 address.setStreetAddress1("1774, Nayabad");
		 memberDAO.insertAddress(address);
		 //Insert Phone Number
		 PhoneNumber phoneNumber = new PhoneNumber();
		 phoneNumber.setMemberId(memberId);
		 phoneNumber.setCountryCode("+91");
		 phoneNumber.setPhoneNumber("9874878554");
		 phoneNumber.setPhoneType(CDSConstants.PHONE_TYPE_MOBILE);
		 phoneNumber.setPrimaryIndicator("Y");
		 memberDAO.insertPhoneNumber(phoneNumber);
		 //Insert SMS Number
		 SMSNumber smsNumber = new SMSNumber();
		 smsNumber.setMemberId(memberId);
		 smsNumber.setSmsNumber(new BigInteger("1111111111"));
		 smsNumber.setSmsType("SMS");;
		 memberDAO.insertSMSNumber(smsNumber);
		 //Insert into Member Identifier table
		 MemberIdentifier memberIdentifier = new MemberIdentifier();
		 memberIdentifier.setMemberId(memberId);
		 memberIdentifier.setDomainName("Twitter");
		 memberIdentifier.setUserId("TESTID");
		 memberDAO.insertMemberIdentifier(memberIdentifier);
		 //assert insertion
		MemberSearchRequest searchRequest = new MemberSearchRequest();
		searchRequest.setMemberStatus("Active");
		searchRequest.setSearchParameterName("memberId");
		searchRequest.setSearchParameterValue(memberId.toString());
		MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		Assert.assertEquals(memberInfo.getTitle(), member.getTitle());
		Assert.assertEquals(memberInfo.getFirstName(), member.getFirstName());
		Assert.assertEquals("test@gmail.com", member.getEmailAddress());
		Assert.assertEquals(memberId, member.getAddresses().get(0).getMemberId());
		Assert.assertEquals(memberId, member.getPhoneNumbers().get(0).getMemberId());
		Assert.assertEquals(smsNumber.getSmsNumber().toString(), member.getSmsNumbers().get(0).getSmsNumber().toString());
		Assert.assertEquals(smsNumber.getSmsType().toString(), member.getSmsNumbers().get(0).getSmsType());
		Assert.assertEquals(memberIdentifier.getMemberId(), member.getMemberIdentifiers().get(0).getMemberId());
		Assert.assertEquals(memberIdentifier.getDomainName(), member.getMemberIdentifiers().get(0).getDomainName());
		
		 log.info("testInsertIntoMemberTable");
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testUpdateMemberSMSNumber(){
		 MemberInfo memberInfo = new MemberInfo();
		 memberInfo.setTitle("Mr");
		 memberInfo.setFirstName("Upananda");
		 memberInfo.setLastName("Saha");
		 memberInfo.setDisplayName("Upananda Saha");
		 memberInfo.setGenderCode("M");
		 memberInfo.setBirthday("01/01/1992");
		 memberInfo.setLanguageCode("EN");
		 memberInfo.setCountry("IN");
		 memberInfo.setMemberStatus("Active");
		 memberInfo.setUuid("ere0-4344-dddd-4444t");
		
		 BigInteger memberId=sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber(CDSConstants.MEMBER_ID_SEQ));
		 memberInfo.setMemberId(memberId);
		 memberDAO.insertMemberInfo(memberInfo);
		//Insert Email
		 Email email = new Email();
		 email.setMemberId(memberId);
		 email.setEmail("test@gmail.com");
		 memberDAO.insertEmail(email);
		 //Insert Address
		 Address address= new Address();
		 address.setMemberId(memberId);
		 address.setAddressType(CDSConstants.ADDRESS_TYPE_HOME);
		 address.setCity("Kolkata");
		 address.setCountry("IN");
		 address.setPostalCode("700099");
		 address.setState("WB");
		 address.setStreetAddress1("1774, Nayabad");
		 memberDAO.insertAddress(address);
		 //Insert Phone Number
		 PhoneNumber phoneNumber = new PhoneNumber();
		 phoneNumber.setMemberId(memberId);
		 phoneNumber.setCountryCode("+91");
		 phoneNumber.setPhoneNumber("9874878554");
		 phoneNumber.setPhoneType(CDSConstants.PHONE_TYPE_MOBILE);
		 phoneNumber.setPrimaryIndicator("Y");
		 memberDAO.insertPhoneNumber(phoneNumber);
		 //Insert SMS Number
		 SMSNumber smsNumber = new SMSNumber();
		 smsNumber.setMemberId(memberId);
		 smsNumber.setSmsNumber(new BigInteger("1111111111"));
		 smsNumber.setSmsType("SMS");
		 memberDAO.insertSMSNumber(smsNumber);
		 //Insert into Member Identifier table
		 MemberIdentifier memberIdentifier = new MemberIdentifier();
		 memberIdentifier.setMemberId(memberId);
		 memberIdentifier.setDomainName("Twitter");
		 memberIdentifier.setUserId("TESTID");
		 memberDAO.insertMemberIdentifier(memberIdentifier);
		 //assert insertion
		MemberSearchRequest searchRequest = new MemberSearchRequest();
		searchRequest.setMemberStatus("Active");
		searchRequest.setSearchParameterName("memberId");
		searchRequest.setSearchParameterValue(memberId.toString());
		MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		Assert.assertEquals(memberInfo.getTitle(), member.getTitle());
		Assert.assertEquals(memberInfo.getFirstName(), member.getFirstName());
		Assert.assertEquals("test@gmail.com", member.getEmailAddress());
		Assert.assertEquals(memberId, member.getAddresses().get(0).getMemberId());
		Assert.assertEquals(memberId, member.getPhoneNumbers().get(0).getMemberId());
		Assert.assertEquals(smsNumber.getSmsNumber().toString(), member.getSmsNumbers().get(0).getSmsNumber().toString());
		Assert.assertEquals(smsNumber.getSmsType().toString(), member.getSmsNumbers().get(0).getSmsType());
		Assert.assertEquals(memberIdentifier.getMemberId(), member.getMemberIdentifiers().get(0).getMemberId());
		Assert.assertEquals(memberIdentifier.getDomainName(), member.getMemberIdentifiers().get(0).getDomainName());
		
		 log.info("testInsertIntoMemberTable");
		 
		 // update SMS NUMBER
		 UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest();
		 updateMemberRequest.setMemberId(memberId);
		 SMSNumber smsNumber2 = new SMSNumber();
		 smsNumber2.setSmsNumber(new BigInteger("12345678"));
		 smsNumber2.setSmsType("SMS");
		 updateMemberRequest.setSmsObject(smsNumber2);
		 memberDAO.updateSMSNumberForMember(updateMemberRequest);
		 log.info("fetchSMSNumberBySMSTypeAndMember");
		 smsNumber2.setMemberId(memberId);
		 SMSNumber smsNumber3=memberDAO.fetchSMSNumberBySMSTypeAndMember(smsNumber2);
		 Assert.assertEquals(smsNumber2.getSmsNumber(), smsNumber3.getSmsNumber());
		 
	 }
	 @Test
	 @Rollback(true)
	 public void testUpdateMemberExternalIDs(){
		 MemberInfo memberInfo = new MemberInfo();
		 memberInfo.setTitle("Mr");
		 memberInfo.setFirstName("Upananda");
		 memberInfo.setLastName("Saha");
		 memberInfo.setDisplayName("Upananda Saha");
		 memberInfo.setGenderCode("M");
		 memberInfo.setBirthday("01/01/1992");
		 memberInfo.setLanguageCode("EN");
		 memberInfo.setCountry("IN");
		 memberInfo.setMemberStatus("Active");
		 memberInfo.setUuid("ere0-4344-dddd-4444t");
		
		 BigInteger memberId=sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber(CDSConstants.MEMBER_ID_SEQ));
		 memberInfo.setMemberId(memberId);
		 memberDAO.insertMemberInfo(memberInfo);
		//Insert Email
		 Email email = new Email();
		 email.setMemberId(memberId);
		 email.setEmail("test@gmail.com");
		 memberDAO.insertEmail(email);
		 //Insert Address
		 Address address= new Address();
		 address.setMemberId(memberId);
		 address.setAddressType(CDSConstants.ADDRESS_TYPE_HOME);
		 address.setCity("Kolkata");
		 address.setCountry("IN");
		 address.setPostalCode("700099");
		 address.setState("WB");
		 address.setStreetAddress1("1774, Nayabad");
		 memberDAO.insertAddress(address);
		 //Insert Phone Number
		 PhoneNumber phoneNumber = new PhoneNumber();
		 phoneNumber.setMemberId(memberId);
		 phoneNumber.setCountryCode("+91");
		 phoneNumber.setPhoneNumber("9874878554");
		 phoneNumber.setPhoneType(CDSConstants.PHONE_TYPE_MOBILE);
		 phoneNumber.setPrimaryIndicator("Y");
		 memberDAO.insertPhoneNumber(phoneNumber);
		 //Insert SMS Number
		 SMSNumber smsNumber = new SMSNumber();
		 smsNumber.setMemberId(memberId);
		 smsNumber.setSmsNumber(new BigInteger("1111111111"));
		 smsNumber.setSmsType("SMS");
		 memberDAO.insertSMSNumber(smsNumber);
		 //Insert into Member Identifier table
		 MemberIdentifier memberIdentifier = new MemberIdentifier();
		 memberIdentifier.setMemberId(memberId);
		 memberIdentifier.setDomainName("Vending");
		 memberIdentifier.setUserId("TESTID");
		 memberDAO.insertMemberIdentifier(memberIdentifier);
		 //assert insertion
		MemberSearchRequest searchRequest = new MemberSearchRequest();
		searchRequest.setMemberStatus("Active");
		searchRequest.setSearchParameterName("memberId");
		searchRequest.setSearchParameterValue(memberId.toString());
		MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		Assert.assertEquals(memberInfo.getTitle(), member.getTitle());
		Assert.assertEquals(memberInfo.getFirstName(), member.getFirstName());
		Assert.assertEquals("test@gmail.com", member.getEmailAddress());
		Assert.assertEquals(memberId, member.getAddresses().get(0).getMemberId());
		Assert.assertEquals(memberId, member.getPhoneNumbers().get(0).getMemberId());
		Assert.assertEquals(smsNumber.getSmsNumber().toString(), member.getSmsNumbers().get(0).getSmsNumber().toString());
		Assert.assertEquals(smsNumber.getSmsType().toString(), member.getSmsNumbers().get(0).getSmsType());
		Assert.assertEquals(memberIdentifier.getMemberId(), member.getMemberIdentifiers().get(0).getMemberId());
		Assert.assertEquals(memberIdentifier.getDomainName(), member.getMemberIdentifiers().get(0).getDomainName());
		
		 log.info("testInsertIntoMemberTable");
		 
		 // update MemberIdentifier
		 
		 
		 MemberIdentifier memberIdentifier2= new MemberIdentifier();
		 memberIdentifier2.setDomainName("Vending");
		 memberIdentifier2.setUserId("TESTID");
		 memberIdentifier2.setMemberId(memberId);
	
		 
		 memberDAO.updateExternalIDForMember(memberIdentifier2);
		 log.info("fetchSMSNumberBySMSTypeAndMember");
		 
		 
		 MemberIdentifier memberIdentifier3=memberDAO.fetchIdentifierByDomainAndMember(memberIdentifier2);
		 Assert.assertEquals(memberIdentifier2.getUserId(), memberIdentifier3.getUserId());
		 Assert.assertEquals(memberIdentifier2.getDomainName(), memberIdentifier3.getDomainName());
		 
	 }
	 @Test
	 @Rollback(true)
	 public void testInsertSMSNumber(){
		 MemberInfo memberInfo = new MemberInfo();
		 memberInfo.setTitle("Mr");
		 memberInfo.setFirstName("Upananda");
		 memberInfo.setLastName("Saha");
		 memberInfo.setDisplayName("Upananda Saha");
		 memberInfo.setGenderCode("M");
		 memberInfo.setBirthday("01/01/1992");
		 memberInfo.setLanguageCode("EN");
		 memberInfo.setCountry("IN");
		 memberInfo.setMemberStatus("Active");
		 memberInfo.setUuid("ere0-4344-dddd-4444t");
		
		 BigInteger memberId=sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber(CDSConstants.MEMBER_ID_SEQ));
		 memberInfo.setMemberId(memberId);
		 memberDAO.insertMemberInfo(memberInfo);
		 //Insert SMS Number
		 SMSNumber smsNumber = new SMSNumber();
		 smsNumber.setMemberId(memberId);
		 smsNumber.setSmsNumber(new BigInteger("1111111111"));
		// smsNumber.setSmsType("SMS");;
		 memberDAO.insertSMSNumber(smsNumber);
		
		 //assert insertion
		MemberSearchRequest searchRequest = new MemberSearchRequest();
		searchRequest.setMemberStatus("Active");
		searchRequest.setSearchParameterName("memberId");
		searchRequest.setSearchParameterValue(memberId.toString());
		MemberInfo member = memberDAO.searchMemberInfo(searchRequest);
		Assert.assertEquals(memberInfo.getTitle(), member.getTitle());
		Assert.assertEquals(memberInfo.getFirstName(), member.getFirstName());
		Assert.assertEquals(smsNumber.getSmsNumber().toString(), member.getSmsNumbers().get(0).getSmsNumber().toString());
		Assert.assertEquals("SMS", member.getSmsNumbers().get(0).getSmsType());
		
		 log.info("testInsertIntoMemberTable");
	 }
	
}
