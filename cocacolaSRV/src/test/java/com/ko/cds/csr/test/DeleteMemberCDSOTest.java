package com.ko.cds.csr.test;

import java.math.BigInteger;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.codeRedemption.CodeRedemptionDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.csr.DeleteMemberRequest;
import com.ko.cds.service.helper.CSRServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CSRConstants;

/**
 * Junit test cases for the Delete Member API 
 * @author ibm
 *
 */
@Component
@Transactional
public class DeleteMemberCDSOTest extends CDSTest {
	

	@Autowired
	private CSRServiceHelper serviceHelper;
	
	@Autowired
	private CodeRedemptionDAO csrDAO;
	
	
	@Test
	@Rollback(true)
	public void getDeleteMemberFromCDSO() throws BadRequestException{
		DeleteMemberRequest req = new DeleteMemberRequest();
		req.setClientTransactionId("289898");
		req.setDeletionReason("TestJunit");
		req.setMemberId(new BigInteger("2362436232"));
		req.setSessionUUID("upa0-4344-dddd-4443l");
		req.setJanrainDelete("Y");
		Response resp = serviceHelper.deleteMemberfromCdso(req);
		Assert.assertNotNull(resp);
	}
	
	@Test
	@Rollback(true)
	public void getDeleteMemberFromCDSOMemberNotFound(){
		DeleteMemberRequest req = new DeleteMemberRequest();
		req.setClientTransactionId("289898");
		req.setDeletionReason("TestJunit");
		req.setMemberId(new BigInteger("2362436234"));
		req.setJanrainDelete("Y");
		 try {
			serviceHelper.deleteMemberfromCdso(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertEquals(e.getMessage(), CSRConstants.MEMBER_NOT_EXISTS);
		}
		
	}
	
	@Test
	@Rollback(true)
	public void getDeleteMemberFromCDSOMemberScheduleForDelete(){
		DeleteMemberRequest req = new DeleteMemberRequest();
		req.setClientTransactionId("289898");
		req.setDeletionReason("TestJunit");
		req.setMemberId(new BigInteger("34338"));
		req.setSessionUUID("upa0-4344-dddd-4443l");
		req.setJanrainDelete("Y");
		try {
			serviceHelper.deleteMemberfromCdso(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertEquals(e.getMessage(), CSRConstants.MEMBER_SCHEDULE_FOR_DELETE);
		}
	}
	
	@Test
	@Rollback(true)
	public void getDeleteMemberDoesnotExists(){
		DeleteMemberRequest req = new DeleteMemberRequest();
		req.setClientTransactionId("289898");
		req.setDeletionReason("TestJunit");
		req.setMemberId(new BigInteger("111"));
		req.setSessionUUID("upa0-4344-dddd-4443l");
		req.setJanrainDelete("Y");
		try {
			serviceHelper.deleteMemberfromCdso(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertEquals(e.getMessage(), CSRConstants.MEMBER_NOT_EXISTS);
		}
	}
	
	@Test
	@Rollback(true)
	public void getDeleteMemberFromCDSORequiredParamValidation(){
		DeleteMemberRequest req = new DeleteMemberRequest();
		//req.setClientTransactionId("289898");
		//req.setDeletionReason("TestJunit");
		//req.setMemberId(new BigInteger(""));
		req.setSessionUUID("upa0-4344-dddd-4443l");
		 try {
			serviceHelper.deleteMemberfromCdso(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertEquals(e.getMessage(),"com.ko.cds.request.csr.DeleteMemberRequest.memberId cannot be null com.ko.cds.request.csr.DeleteMemberRequest.clientTransactionId cannot be null com.ko.cds.request.csr.DeleteMemberRequest.deletionReason cannot be null com.ko.cds.request.csr.DeleteMemberRequest.janrainDelete cannot be null ");
		}
	}
	
}
