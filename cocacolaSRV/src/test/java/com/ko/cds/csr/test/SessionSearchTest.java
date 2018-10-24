package com.ko.cds.csr.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.csr.SessionSearchRequest;
import com.ko.cds.response.csr.SessionSearchResponse;
import com.ko.cds.service.helper.CSRServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CSRConstants;
import com.ko.cds.utils.ErrorCode;

/**
 * Junit test cases for the Search Session API
 * @author ibm
 *
 */
@Component
@Transactional
public class SessionSearchTest extends CDSTest {
	
	@Autowired
	private CSRServiceHelper serviceHelper;
	
	@Autowired
	private ActivityDAO activityDAO;
	
	@Test
	public void searchSessionWithMember() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setMemberId(new BigInteger("1"));
		SessionSearchResponse resp = serviceHelper.searchSession(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getSessions().size() > 0);
	}
	
	@Test
	public void searchSessionWithSiteId() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setSiteId(new BigInteger("12345"));
		SessionSearchResponse resp = serviceHelper.searchSession(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getSessions().size() > 0);
	}
	
	@Test
	public void searchSessionWithIP() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setIpAddress("192.168.0.1");
		SessionSearchResponse resp = serviceHelper.searchSession(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getSessions().size() > 0);
	}
	
	@Test
	public void searchSessionWithSessionUUID() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setSearchSessionUUID("6713cb74-361e-4373-ba7c-04212ddd");
		SessionSearchResponse resp = serviceHelper.searchSession(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getSessions().size() > 0);
	}
	
	@Test
	public void searchSessionWithDates() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setSessionDateSearchStart("2014-03-10T21:01:25.000Z");
		req.setSessionDateSearchEnd("2014-06-10T21:01:25.000Z");
		SessionSearchResponse resp = serviceHelper.searchSession(req);
		Assert.assertNotNull(resp);
		Assert.assertTrue(resp.getSessions().size() > 0);
	}
	
	@Test
	public void searchSessionDateRangeValidation(){
		SessionSearchRequest req = new SessionSearchRequest();
		req.setSessionDateSearchStart("2014-03-10T21:01:25.000Z");
		req.setSessionDateSearchEnd("2013-06-10T21:01:25.000Z");
		try {
			serviceHelper.searchSession(req);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), ErrorCode.DATE_ERROR_MESSAGE);
		}
	}
	
	@Test
	public void searchSessionDateValidation() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setSessionDateSearchStart("2014-03-10T21:01:25.000Z");
		try {
			serviceHelper.searchSession(req);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), CSRConstants.DATE_SELECTION_ERROR);
		}
	}
	
	@Test
	public void searchSessionNoData() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setMemberId(new BigInteger("143"));
		try {
			serviceHelper.searchSession(req);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), ErrorCode.NO_HISTORY_FOUND);
		}
	}
	
	@Test
	public void searchSessionEmptyRequest() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		try {
			serviceHelper.searchSession(req);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), CSRConstants.NO_FIELD_SELECTION_ERROR);
		}
	}
	
	@Test
	public void searchSessionWithSessionUUIDValidation() throws BadRequestException{
		SessionSearchRequest req = new SessionSearchRequest();
		req.setSearchSessionUUID("9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
		req.setSessionUUID("888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888");
		try {
			serviceHelper.searchSession(req);
		} catch (BadRequestException e) {
			Assert.assertEquals(e.getMessage(), "com.ko.cds.request.csr.SessionSearchRequest.sessionUUID cannot be longer than 36 characters com.ko.cds.request.csr.SessionSearchRequest.searchSessionUUID cannot be longer than 36 characters ");
		}
	}
}
