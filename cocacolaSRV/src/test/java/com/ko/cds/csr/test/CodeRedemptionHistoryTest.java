package com.ko.cds.csr.test;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ko.cds.dao.codeRedemption.CodeRedemptionDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.csr.CodeRedemptionHistoryRequest;
import com.ko.cds.response.csr.CodeRedemptionHistoryResponse;
import com.ko.cds.service.helper.CSRServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CSRConstants;
/**
 * Junit Test Cases for the Code Redemption History API
 * @author ibm
 *
 */
public class CodeRedemptionHistoryTest extends CDSTest {
	

	@Autowired
	private CSRServiceHelper serviceHelper;
	
	@Autowired
	private CodeRedemptionDAO csrDAO;
	
	
	@Test
	public void getCodeRedemptionHistoryOnClientTransId() throws BadRequestException{
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		req.setSearchClientTransactionId("767");
		System.out.println(req);
		CodeRedemptionHistoryResponse resp = serviceHelper.getCodeRedemptionHistory(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getCodeRedemptions().size()>0);
		
	}
	
	@Test
	public void getCodeRedemptionHistoryOnMemberId()throws BadRequestException{
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		req.setMemberId(new BigInteger("175"));
		CodeRedemptionHistoryResponse resp = serviceHelper.getCodeRedemptionHistory(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getCodeRedemptions().size()>0);
	}
	
	@Test
	public void getgetCodeRedemptionHistoryOnLotId()throws BadRequestException{
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		req.setLotId(new BigInteger("1"));
		CodeRedemptionHistoryResponse resp =  serviceHelper.getCodeRedemptionHistory(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getCodeRedemptions().size()>0);
	}
	
	@Test
	public void getCodeRedemptionHistoryBetweenDates()throws BadRequestException{
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		req.setRedemptionHistoryStartDate("2013-07-09T18:25:43.511Z");
		req.setRedemptionHistoryEndDate("2015-07-09T18:25:43.511Z");
		CodeRedemptionHistoryResponse resp =  serviceHelper.getCodeRedemptionHistory(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getCodeRedemptions().size()>0);
	}
	
	@Test
	public void getCodeRedemptionHistoryDateCheck(){
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		req.setRedemptionHistoryEndDate("2014-08-19T00:00:00.000Z");
		try {
			 serviceHelper.getCodeRedemptionHistory(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			Assert.assertEquals(e.getMessage(), CSRConstants.DATE_SELECTION_ERROR);
			
		}

	}

	@Test
	public void getCodeRedemptionHistoryValidationCheck(){
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		req.setSessionUUID("90909000999999999999999999999999999999999999999999999999999999999999999999999999999999999999"
				+ "999999999999999999999999999999");
		try {
			serviceHelper.getCodeRedemptionHistory(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertEquals(e.getMessage(), "com.ko.cds.request.csr.CodeRedemptionHistoryRequest.sessionUUID cannot be longer than 36 characters ");
		}
		
	}
	
	@Test
	public void getCodeRedemptionHistoryAllNullValues(){
		CodeRedemptionHistoryRequest req = new CodeRedemptionHistoryRequest();
		try {
			serviceHelper.getCodeRedemptionHistory(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			Assert.assertEquals(e.getMessage(), CSRConstants.NO_FIELD_SELECTION_ERROR);
		}
		
	}
}
