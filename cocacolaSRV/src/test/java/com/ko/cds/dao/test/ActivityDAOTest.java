package com.ko.cds.dao.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.test.CDSTest;

/**
 * Junit for Record Session Info api
 * 
 * @author IBM
 * 
 */
@Component
public class ActivityDAOTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(ActivityDAOTest.class);
	
	// Testcase to test happyPath
	@Test
	@Rollback(true)
	public void testRecordSessionInfo() {

		RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-04212dddf91c");
		recSessionReq.setSessionDate("2014-05-29");
		recSessionReq.setMemberId(new BigInteger("1"));
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
	}

	// Testcase - Member id null (Not NUllable in DB)
	@Test(expected = DataIntegrityViolationException.class)
	public void testWithMemberIdNull() {

		RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-04212dddf91c");
		recSessionReq.setSessionDate("2014-05-29");
		// recSessionReq.setMemberId(new BigInteger("1"));
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		Assert.assertEquals(0, inserted);
		log.info("No. of records inserted : " + inserted);
	}

	// TestCase - Unavailable Site ID - FK violation
	@Test(expected = DataIntegrityViolationException.class)
	public void testWithUnavailableSiteID() {

		RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-04212dddf91c");
		recSessionReq.setSessionDate("2014-05-29");
		recSessionReq.setMemberId(new BigInteger("1"));
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		int inserted = activityDAO.recordSessionInfo(recSessionReq);
		Assert.assertEquals(0, inserted);
		log.info("No. of records inserted : " + inserted);
	}
}
