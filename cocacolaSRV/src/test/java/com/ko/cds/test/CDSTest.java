package com.ko.cds.test;

import java.math.BigInteger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.csr.CSRTicketDAO;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Email;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.service.helper.ICacheService;
import com.ko.cds.utils.CDSConstants;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:resources/junit/servlet-context-test.xml"})
@TransactionConfiguration(transactionManager="txManager", defaultRollback=true) 

public class CDSTest {
	private static Logger log = LoggerFactory.getLogger(CDSTest.class);
	
	@Autowired
	protected PointAccountDAO pointAccountDAO;
	@Autowired
	protected MemberDAO memberDAO;
	@Autowired
	protected SequenceNumberDAO sequenceNumberDAO;
	@Autowired
	 protected ActivityDAO activityDAO;
	@Autowired
	protected ICacheService cacheService;
	
	@Autowired
	protected CSRTicketDAO cSRTicketDAO;
	 @BeforeClass
	 public static void setUp() throws Exception {
	  log.info("starting up myBatis tests");
	
	 }
	  
	 @AfterClass
	 public static void tearDown() throws Exception {  
	  log.info("closing down myBatis tests");
	 }
	 protected BigInteger createMember(){
		   MemberInfo memberInfo = new MemberInfo();
			 memberInfo.setTitle("Mr");
			 memberInfo.setFirstName("Upananda");
			 memberInfo.setLastName("Saha");
			 memberInfo.setDisplayName("Upananda Saha");
			 memberInfo.setGenderCode("M");
			 memberInfo.setBirthday("1992-01-02");
			 memberInfo.setLanguageCode("EN");
			 memberInfo.setCountry("IN");
			 memberInfo.setMemberStatus("Active");
			 memberInfo.setUuid("ere0-4344-dddd-u4449");
			
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
			
			 return memberId;
			
	   }
	 protected SMSNumber insertSMSNumber(BigInteger memberId){
		 SMSNumber smsNumber = new SMSNumber();
		 smsNumber.setMemberId(memberId);
		 smsNumber.setSmsNumber(new BigInteger("867890234658"));
		 smsNumber.setSmsType("SMS");
		 smsNumber.setStatusCode(CDSConstants.STATUS_ACTIVE);
		 memberDAO.insertSMSNumber(smsNumber);
		 return smsNumber;
	 }
	 protected MemberIdentifier insertMemberIdentifier(BigInteger memberId){
		 MemberIdentifier memberIdentifier = new MemberIdentifier();
		 memberIdentifier.setMemberId(memberId);
		 memberIdentifier.setDomainName("Vending-LoyaltyCard");
		 memberIdentifier.setUserId("9362919999018377");
		 memberDAO.insertMemberIdentifier(memberIdentifier);
		 return memberIdentifier;
	 }
	 
}