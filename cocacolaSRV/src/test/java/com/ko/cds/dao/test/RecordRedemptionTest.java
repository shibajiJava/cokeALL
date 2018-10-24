package com.ko.cds.dao.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.activity.RecordRedemptionRequest;
import com.ko.cds.service.helper.ActivityServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.ErrorCode;


@Component
@Transactional
public class RecordRedemptionTest extends CDSTest{
	public static Logger logger=LoggerFactory.getLogger(RecordRedemptionTest.class);

	@Autowired
	private ActivityServiceHelper activityServiceHelper;
	@Autowired
	private SessionIdDAO sessionDAO; 
	
	@Autowired
	private ActivityDAO activityDAO;
	
	@Test
	@Rollback(true)
	public void recordRedemption_HappyPathWithLotID() throws BadRequestException{
		RecordRedemptionRequest redemptionTrans=new RecordRedemptionRequest();	
		redemptionTrans.setSessionUUID("we23sgr1");
		redemptionTrans.setMemberId(new BigInteger("176"));
		redemptionTrans.setProgramId(new BigInteger("121"));
		redemptionTrans.setProductId("Coke");
		redemptionTrans.setLotId(new BigInteger("1"));
		BigInteger resp = activityServiceHelper.InsertRecordRedemptionDetails(redemptionTrans);
		Assert.assertNotNull(resp);
	}
	

	@Test
	@Rollback(true)
	public void recordRedemption_HappyPathWithCampaignID() throws BadRequestException{
		RecordRedemptionRequest redemptionTrans=new RecordRedemptionRequest();	
		redemptionTrans.setSessionUUID("we23sgr1");
		redemptionTrans.setMemberId(new BigInteger("176"));
		redemptionTrans.setProgramId(new BigInteger("121"));
		redemptionTrans.setProductId("Coke");
		redemptionTrans.setCampaignId(new Integer("1"));
		BigInteger resp = activityServiceHelper.InsertRecordRedemptionDetails(redemptionTrans);
		Assert.assertNotNull(resp);
	}
	
	@Test
	@Rollback(true)
	public void recordRedemptionWithoutMemberId(){
		RecordRedemptionRequest redemptionTrans=new RecordRedemptionRequest();	
		redemptionTrans.setSessionUUID("we23sgr1");
		redemptionTrans.setProgramId(new BigInteger("121"));
		redemptionTrans.setProductId("Coke");
		redemptionTrans.setLotId(new BigInteger("1"));
		try {
			activityServiceHelper.InsertRecordRedemptionDetails(redemptionTrans);
		} catch (BadRequestException e) {
			// TODO: handle exception
			Assert.assertEquals(e.getMessage(), "com.ko.cds.request.activity.RecordRedemptionRequest.memberId cannot be null ");
		}
			
	}
	
	@Test
	@Rollback(true)
	public void recordRedemptionWithOUTLotIdandCampaignId(){
		RecordRedemptionRequest redemptionTrans=new RecordRedemptionRequest();	
		redemptionTrans.setSessionUUID("we23sgr1");
		redemptionTrans.setMemberId(new BigInteger("176"));
		redemptionTrans.setProgramId(new BigInteger("121"));
		redemptionTrans.setProductId("Coke");
		try {
			activityServiceHelper.InsertRecordRedemptionDetails(redemptionTrans);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), ErrorCode.REEDEMTION_LOT_CAMPAING_ERROR);
		}
			
		
	}
	
	@Test
	@Rollback(true)
	public void recordRedemptionWithLotIdandCampaignId() throws BadRequestException{
		RecordRedemptionRequest redemptionTrans=new RecordRedemptionRequest();	
		redemptionTrans.setSessionUUID("we23sgr1");
		redemptionTrans.setMemberId(new BigInteger("176"));
		redemptionTrans.setProgramId(new BigInteger("121"));
		redemptionTrans.setLotId(new BigInteger("176"));
		redemptionTrans.setCampaignId(new Integer("34"));
		redemptionTrans.setProductId("Coke");
		try {
			activityServiceHelper.InsertRecordRedemptionDetails(redemptionTrans);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(),  ErrorCode.REEDEMTION_LOT_CAMPAING_ERROR);
		}
			
		
	}
	
	@Test
	@Rollback(true)
	public void recordRedemptionWithoutProgramId() throws BadRequestException{
		RecordRedemptionRequest redemptionTrans=new RecordRedemptionRequest();	
		redemptionTrans.setSessionUUID("we23sgr1");
		redemptionTrans.setMemberId(new BigInteger("176"));
		redemptionTrans.setProductId("Coke");
		redemptionTrans.setLotId(new BigInteger("1"));
		try {
			activityServiceHelper.InsertRecordRedemptionDetails(redemptionTrans);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), "com.ko.cds.request.activity.RecordRedemptionRequest.programId cannot be null ");
		}
		
	}
	

}
