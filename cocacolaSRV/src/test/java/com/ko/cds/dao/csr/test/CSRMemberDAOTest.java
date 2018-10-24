package com.ko.cds.dao.csr.test;

import java.math.BigInteger;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.pojo.csr.CSRMemberInfo;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.request.csr.CSRAdvanceSearchRequest;
import com.ko.cds.test.CDSTest;

@Component
@Transactional
public class CSRMemberDAOTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(CSRMemberDAOTest.class);
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchBySessionUUID(){
		 CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		 advanceSearchRequest.setSessionUUID("1esd23c");
		 advanceSearchRequest.setMemberId(new BigInteger("2"));
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		log.info("Exit");
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchBySessionUUIDAndMemberId(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		 //Record member Session
		 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
			recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
			recSessionReq.setSessionDate("2014-10-22");
			recSessionReq.setMemberId(memberId);
			recSessionReq.setIpAddress("192.168.0.1");
			recSessionReq
					.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
			recSessionReq.setReferring("http://www.google.com");
			recSessionReq.setSiteId(12345);

			int inserted = activityDAO.recordSessionInfo(recSessionReq);
			
			Assert.assertEquals(1, inserted);
			log.info("No. of records inserted : " + inserted);
			inserted = activityDAO.recordMemberSite(recSessionReq);
			Assert.assertEquals(1, inserted);
			log.info("No. of records inserted : " + inserted);
		 CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		// advanceSearchRequest.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		 advanceSearchRequest.setMemberId(memberId);
		 long startTime = System.currentTimeMillis();
		 List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		 long endTime =  System.currentTimeMillis();
		 endTime= endTime-startTime;
		 log.info("time taken: "+endTime);
		log.info("Exit");
	 }
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByFirstName(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		advanceSearchRequest.setFirstName("UPA%");
		long startTime = System.currentTimeMillis();
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		long endTime =  System.currentTimeMillis();
		endTime= endTime-startTime;
		log.info("time taken: "+endTime);
		Assert.assertTrue(csrMemberInfoList.size() >0);
		log.info("Exit");
		
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByFirstNameAndLastName(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		advanceSearchRequest.setFirstName("UPA%");
		advanceSearchRequest.setLastName("SA%");
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		Assert.assertTrue(csrMemberInfoList.size() >0);
		log.info("Exit");
		
	 }
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByEmail(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		advanceSearchRequest.setEmail("TEST%");
		
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		Assert.assertTrue(csrMemberInfoList.size() >0);
		log.info("Exit");
		
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByBirthDate(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		//Date birthDate=CDSOUtils.parseDateString("yyyy-MM-dd", "1992-01-02");
		advanceSearchRequest.setBirthDate("1992-01-02");
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		Assert.assertTrue(csrMemberInfoList.size() >0);
		log.info("Exit");
		
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByCreateDate(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
	//	Date createDate=CDSOUtils.parseDateString("yyyy-MM-dd HH:mm:ss", "2014-10-22 00:00:00");
		advanceSearchRequest.setCreatedDate("2014-10-22");
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		Assert.assertTrue(csrMemberInfoList.size() >0);
		log.info("Exit");
		
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByMemberIdentifier(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		TagObject memberIdentifier = new TagObject();
		String memberIdentifierName = "Vending-LoyaltyCard";
		memberIdentifierName = memberIdentifierName.toUpperCase();
		memberIdentifier.setName(memberIdentifierName);
		memberIdentifier.setValue("9362919999018377");
		advanceSearchRequest.setMemberIdentifier(memberIdentifier);
		long startTime = System.currentTimeMillis();
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		long endTime =  System.currentTimeMillis();
		endTime= endTime-startTime;
		log.info("time taken: "+endTime);
		Assert.assertTrue(csrMemberInfoList.size() >0);
		log.info("Exit");
		
	 }
	 
	 @Test
	 @Rollback(true)
	 public void testCSRAdvanceSearchByAllCriteria(){
		 //create member
		 BigInteger memberId= createMember();
		 insertSMSNumber(memberId);
		 insertMemberIdentifier(memberId);
		//Record member Session
	 	RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		recSessionReq.setSessionDate("2014-10-22");
		recSessionReq.setMemberId(memberId);
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		CSRAdvanceSearchRequest advanceSearchRequest = new CSRAdvanceSearchRequest();
		TagObject memberIdentifier = new TagObject();
		String domainName = "Vending-LoyaltyCard";
		domainName= domainName.toUpperCase();
		memberIdentifier.setName(domainName);
		memberIdentifier.setValue("9362919999018377");
		advanceSearchRequest.setSessionUUID("6713cb74-361e-4373-ba7c-77892dddf91c");
		//advanceSearchRequest.setMemberId(memberId);
		advanceSearchRequest.setMemberIdentifier(memberIdentifier);
		advanceSearchRequest.setFirstName("UPA%");
		advanceSearchRequest.setLastName("SA%");
		advanceSearchRequest.setEmail("TEST%");
		//Date birthDate=CDSOUtils.parseDateString("yyyy-MM-dd", "1992-01-02");
		advanceSearchRequest.setBirthDate("1992-01-02");
	//	Date createDate=CDSOUtils.parseDateString("yyyy-MM-dd HH:mm:ss", "2014-10-24 00:00:00");
		//advanceSearchRequest.setCreatedDate("2014-10-28");
		String city = "Kolkata";
		city = city.toUpperCase();
		advanceSearchRequest.setCity(city);
		String address = "1774, Nayabad";
		address= address.toUpperCase();
		advanceSearchRequest.setAddress(address);
		advanceSearchRequest.setZip("700099");
		//advanceSearchRequest.setCdsAccountstatus("Active".toUpperCase());
		advanceSearchRequest.setJanrainUUID("ere0-4344-dddd-u4449");
		long startTime = System.currentTimeMillis();
		List<CSRMemberInfo> csrMemberInfoList= memberDAO.csrAdvanceSearch(advanceSearchRequest);
		long endTime =  System.currentTimeMillis();
		endTime= endTime-startTime;
		log.info("time taken: "+endTime);
		Assert.assertTrue(csrMemberInfoList.size() ==1);
		Assert.assertEquals(memberId, csrMemberInfoList.get(0).getMemberId());
		log.info("Exit");
		
	 }

}
