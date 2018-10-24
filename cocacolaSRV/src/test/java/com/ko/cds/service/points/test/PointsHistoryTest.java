package com.ko.cds.service.points.test;

import java.math.BigInteger;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.points.PointsHistory;
import com.ko.cds.request.points.PointsHistoryRequest;
import com.ko.cds.response.points.PointsHistoryResponse;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CSRConstants;
import com.ko.cds.utils.ErrorCode;

public class PointsHistoryTest extends CDSTest {

	private static Logger log = LoggerFactory
			.getLogger(PointsHistoryTest.class);

	@Autowired
	private PointAccountDAO pointAccDAO;
	
	@Autowired
	private PointAccountServiceHelper serviceHelper;

	//Retrieval for a particular member id
	@Test
	public void getPointsHistory() throws BadRequestException {
		PointsHistoryRequest ptReq = new PointsHistoryRequest();
		ptReq.setMemberId(new BigInteger("173"));
		ptReq.setAccountType("MCR");
		ptReq.setReasonCode("1");
		ptReq.setStartDateTime("2013-03-10T21:01:25.000Z");
		ptReq.setEndDateTime("2014-10-10T21:01:25.000Z");
		PointsHistoryResponse ptResp = serviceHelper.getPointsHistory(ptReq);
		log.info("Points History response :" + ptResp);		
		Assert.assertNotNull(ptResp);
	}
	
	//Retrieval without offset and limit, with start date and end date
	@Test
	public void getPointsHistoryWithMandatoryParams() {
		PointsHistoryRequest ptReq = new PointsHistoryRequest();
		ptReq.setMemberId(new BigInteger("173"));
		ptReq.setAccountType("MCR");
		ptReq.setReasonCode("1");
		ptReq.setStartDateTime("2013-03-10T21:01:25.000Z");
		ptReq.setEndDateTime("2014-10-10T21:01:25.000Z");
		ptReq.setAscending(false);		

		List<PointsHistory> ptResp = pointAccDAO.getPointsHistory(ptReq);
		log.info("Points History response :" + ptResp);		
		Assert.assertNotNull(ptResp);
	}
	
	//Retrieval with offset and limit
	@Test
	public void getPointsHistoryWithAllParams() throws BadRequestException {
		PointsHistoryRequest ptReq = new PointsHistoryRequest();
		ptReq.setMemberId(new BigInteger("173"));
		ptReq.setAccountType("MCR");
		ptReq.setReasonCode("1");
		ptReq.setStartDateTime("2013-03-10T21:01:25.000Z");
		ptReq.setEndDateTime("2014-10-10T21:01:25.000Z");
		ptReq.setAscending(false);
		ptReq.setOffset(4);
		ptReq.setLimit(6);

		PointsHistoryResponse ptResp = serviceHelper.getPointsHistory(ptReq);
		log.info("Points History response :" + ptResp);		
		Assert.assertNotNull(ptResp);
	}
	
	 @Test
		public void getPointsHistoryNoDataFound() {
			PointsHistoryRequest ptReq = new PointsHistoryRequest();
			ptReq.setMemberId(new BigInteger("1"));
			ptReq.setAccountType("MCR");
			ptReq.setReasonCode("1");
			ptReq.setStartDateTime("2013-03-10T21:01:25.000Z");
			ptReq.setEndDateTime("2014-03-10T21:01:25.000Z");
			try {
				PointsHistoryResponse ptResp = serviceHelper.getPointsHistory(ptReq);
			} catch (BadRequestException e) {
				// TODO Auto-generated catch block
				Assert.assertEquals(e.getMessage(), ErrorCode.NO_HISTORY_FOUND);
			}
		}
	 
	 @Test
		public void getPointsHistoryDateError() {
			PointsHistoryRequest ptReq = new PointsHistoryRequest();
			ptReq.setMemberId(new BigInteger("1"));
			ptReq.setAccountType("MCR");
			ptReq.setReasonCode("1");
			ptReq.setStartDateTime("2014-03-10T21:01:25.000Z");
			ptReq.setEndDateTime("2013-03-10T21:01:25.000Z");
			try {
				PointsHistoryResponse ptResp = serviceHelper.getPointsHistory(ptReq);
			} catch (BadRequestException e) {
				// TODO Auto-generated catch block
				Assert.assertEquals(e.getMessage(), ErrorCode.DATE_ERROR_MESSAGE);
			}
		}
	 
	 @Test
		public void getPointsHistoryDurationExceedError() {
			PointsHistoryRequest ptReq = new PointsHistoryRequest();
			ptReq.setMemberId(new BigInteger("1"));
			ptReq.setAccountType("MCR");
			ptReq.setReasonCode("1");
			ptReq.setStartDateTime("2012-03-10T21:01:25.000Z");
			ptReq.setEndDateTime("2014-03-10T21:01:25.000Z");
			try {
				PointsHistoryResponse ptResp = serviceHelper.getPointsHistory(ptReq);
			} catch (BadRequestException e) {
				// TODO Auto-generated catch block
				Assert.assertEquals(e.getMessage(), ErrorCode.NO_HISTORY_FOUND);
			}
		}
	 
	 @Test
		public void getPointsHistoryMandatoryFieldValidation() {
			PointsHistoryRequest ptReq = new PointsHistoryRequest();
			ptReq.setAccountType("MCR");
			ptReq.setReasonCode("1");
			ptReq.setStartDateTime("2012-03-10T21:01:25.000Z");
			ptReq.setEndDateTime("2014-03-10T21:01:25.000Z");
			try {
				PointsHistoryResponse ptResp = serviceHelper.getPointsHistory(ptReq);
			} catch (BadRequestException e) {
				// TODO Auto-generated catch block
				Assert.assertEquals(e.getMessage(), "com.ko.cds.request.points.PointsHistoryRequest.memberId cannot be null ");
			}
		}
}
