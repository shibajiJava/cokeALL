package com.ko.cds.dao.csr.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.pojo.csr.Site;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.test.CDSTest;

@Component
@Transactional
public class MemberSiteRecordingTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(MemberSiteRecordingTest.class);
	@Test
	@Rollback(true)
	public void testRecordMemberSiteTest(){
		RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-04212dddf92c");
		recSessionReq.setSessionDate("2014-10-21");
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
		Site firstLogin = activityDAO.getMemberSiteRecord(recSessionReq);
		/*** record session again*/
		
		recSessionReq = new RecordSessionInfoRequest();
		recSessionReq.setSessionUUID("6713cb74-361e-4373-ba7c-04212dddf92d");
		recSessionReq.setSessionDate("2014-10-21");
		recSessionReq.setMemberId(new BigInteger("1"));
		recSessionReq.setIpAddress("192.168.0.1");
		recSessionReq
				.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		recSessionReq.setReferring("http://www.google.com");
		recSessionReq.setSiteId(12345);

		inserted = activityDAO.recordSessionInfo(recSessionReq);
		
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		inserted = activityDAO.recordMemberSite(recSessionReq);
		Assert.assertEquals(1, inserted);
		log.info("No. of records inserted : " + inserted);
		Site secondLogin = activityDAO.getMemberSiteRecord(recSessionReq);
		Assert.assertNotSame(firstLogin.getLastLoginDate(), secondLogin.getLastLoginDate());
	}

}
