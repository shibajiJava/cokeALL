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

import com.ko.cds.pojo.csr.CSRTicket;
import com.ko.cds.pojo.csr.Site;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.request.csr.CSRTicketGetRequest;
import com.ko.cds.request.csr.CSRTicketPostRequest;
import com.ko.cds.test.CDSTest;

@Component
@Transactional
public class CSRTicketDAOTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(CSRTicketDAOTest.class);
	 @Test
	 @Rollback(true)
	public void testInsertCSRTicket(){
		 log.info("Entry testInsertCSRTicket");
		CSRTicketPostRequest csrTicketPostRequest = new CSRTicketPostRequest();
		csrTicketPostRequest.setClientTransactionId("456387495485");
		csrTicketPostRequest.setCsrReasonCode("Standard");
		csrTicketPostRequest.setDataType("Points Adjustment");
		csrTicketPostRequest.setKoMemberKoId("1325757");
		csrTicketPostRequest.setKoMemberName("Michel");
		csrTicketPostRequest.setMemberId(new BigInteger("2347879594"));
		//csrTicketPostRequest.setSessionId(sessionId);
		csrTicketPostRequest.setTimestamp("2014-10-16 14:50:21.749");
		csrTicketPostRequest.setSessionUUID("20148001-164e-477e-8499-cs7ecb9f");
		BigInteger sessionId=cacheService.getSessionID(csrTicketPostRequest.getSessionUUID());
		csrTicketPostRequest.setSessionId(sessionId); 
		cSRTicketDAO.insertCSRTicket(csrTicketPostRequest);
		log.info("Exit testInsertCSRTicket");
	}
  
	 @Test
	 @Rollback(true)
	public void testGetCSRTicketByMemberIdAndSessionUUID(){
		 BigInteger memberId=createMember();
		 // record session for member
		 RecordSessionInfoRequest recSessionReq = new RecordSessionInfoRequest();
			recSessionReq.setSessionUUID("2014th74-361e-4373-ba7c-04212csrf92c");
			recSessionReq.setSessionDate("2014-11-03");
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
			Site firstLogin = activityDAO.getMemberSiteRecord(recSessionReq);
			
			
		 log.info("Entry testGetCSRTicket");
		CSRTicketPostRequest csrTicketPostRequest = new CSRTicketPostRequest();
		csrTicketPostRequest.setClientTransactionId("456387495485");
		csrTicketPostRequest.setCsrReasonCode("Standard");
		csrTicketPostRequest.setDataType("Points Adjustment");
		csrTicketPostRequest.setKoMemberKoId("1325757");
		csrTicketPostRequest.setKoMemberName("Michel");
		csrTicketPostRequest.setMemberId(memberId);
		//csrTicketPostRequest.setSessionId(sessionId);
		csrTicketPostRequest.setTimestamp("2014-10-16 14:50:21.749");
		csrTicketPostRequest.setSessionUUID(recSessionReq.getSessionUUID());
		BigInteger sessionId=cacheService.getSessionID(csrTicketPostRequest.getSessionUUID());
		csrTicketPostRequest.setSessionId(sessionId);
		cSRTicketDAO.insertCSRTicket(csrTicketPostRequest);
		CSRTicketGetRequest csrTicketGetRequest = new CSRTicketGetRequest();
		csrTicketGetRequest.setMemberId(memberId);
		csrTicketGetRequest.setSessionId(sessionId);
		List<CSRTicket> csrTickets =cSRTicketDAO.getCSRHistory(csrTicketGetRequest);
		Assert.assertEquals(1, csrTickets.size());
		log.info("Exit testGetCSRTicket");
		
	}
	 
	 @Test
	 @Rollback(true)
	public void testGetCSRTicketByMemberId(){
		 BigInteger memberId=createMember();
		log.info("Entry testGetCSRTicket");
		CSRTicketPostRequest csrTicketPostRequest = new CSRTicketPostRequest();
		csrTicketPostRequest.setClientTransactionId("456387495485");
		csrTicketPostRequest.setCsrReasonCode("Standard");
		csrTicketPostRequest.setDataType("Points Adjustment");
		csrTicketPostRequest.setKoMemberKoId("1325757");
		csrTicketPostRequest.setKoMemberName("Michel");
		csrTicketPostRequest.setMemberId(memberId);
		//csrTicketPostRequest.setSessionId(sessionId);
		csrTicketPostRequest.setTimestamp("2014-10-16 14:50:21.749");
		cSRTicketDAO.insertCSRTicket(csrTicketPostRequest);
		CSRTicketGetRequest csrTicketGetRequest = new CSRTicketGetRequest();
		csrTicketGetRequest.setMemberId(memberId);
		List<CSRTicket> csrTickets =cSRTicketDAO.getCSRHistory(csrTicketGetRequest);
		Assert.assertEquals(1, csrTickets.size());
		log.info("Exit testGetCSRTicket");
		
	} 
}
