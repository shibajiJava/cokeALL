package com.ko.cds.csr.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.activity.SearchActivityTagObject;
import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.request.csr.ActivitySearchRequest;
import com.ko.cds.request.csr.ActivityTagJSON;
import com.ko.cds.response.csr.ActivitySearchResponse;
import com.ko.cds.service.helper.CSRServiceHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.CSRConstants;
import com.ko.cds.utils.ErrorCode;

/**
 * Junit Test cases for the Activity Search APRI
 * @author ibm
 *
 */
@Component
@Transactional
public class ActivitySearchTest extends CDSTest {
		
		@Autowired
		private CSRServiceHelper serviceHelper;
		
		@Autowired
		private ActivityDAO activityDAO;
		
		@Test
		public void searchActivity() throws BadRequestException{
			SearchActivityTagObject tags = new SearchActivityTagObject();
			tags.setName("Activity");
			tags.setValue("Signup");
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("3"));
			req.setTagObject(tags);
			ActivitySearchResponse resp = serviceHelper.searchActivity(req);
			Assert.assertNotNull(resp);
			Assert.assertTrue(resp.getActivityObjects().size() > 0);
		}
		
		@Test
		public void searchActivityMemberValidation() throws BadRequestException{
			SearchActivityTagObject tags = new SearchActivityTagObject();
			tags.setName("Activity");
			tags.setValue("Signup");
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setTagObject(tags);
			try {
				serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), "com.ko.cds.request.csr.ActivitySearchRequest.memberId cannot be null");
			}
		}
		
		@Test
		public void searchActivitTagObjectValidation(){
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("3"));
			try {
				 serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), "com.ko.cds.request.csr.ActivitySearchRequest.tagObject cannot be null");
			}
		}
		
		@Test
		public void searchActivityNoDataFound(){
			SearchActivityTagObject tags = new SearchActivityTagObject();
			tags.setName("Activity");
			tags.setValue("Signup");
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("1"));
			req.setTagObject(tags);
			try {
			 serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), ErrorCode.NO_HISTORY_FOUND);
			}
		}
		
		@Test
		public void searchActivityDateValidation(){
			SearchActivityTagObject tags = new SearchActivityTagObject();
			tags.setName("Activity");
			tags.setValue("Signup");
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("1"));
			req.setTagObject(tags);
			req.setEndDate("2013-03-10T21:01:25.000Z");
			try {
				serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), CSRConstants.DATE_SELECTION_ERROR);
			}
		}
		
		@Test
		public void searchActivityDateRangeValidation(){
			SearchActivityTagObject tags = new SearchActivityTagObject();
			tags.setName("Activity");
			tags.setValue("Signup");
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("1"));
			req.setTagObject(tags);
			req.setEndDate("2013-03-10T21:01:25.000Z");
			req.setStartDate("2014-03-10T21:01:25.000Z");
			try {
				 serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), ErrorCode.DATE_ERROR_MESSAGE);
			}
		}
		
		@Test
		public void searchActivitySessionUUIDValidation(){
			SearchActivityTagObject tags = new SearchActivityTagObject();
			tags.setName("Activity");
			tags.setValue("Signup");
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("1"));
			req.setTagObject(tags);
			req.setSessionUUID("90909000999999999999999999999999999999999999999999999999999999999999999999999999999999999999"
					+ "999999999999999999999999999999");
			try {
				serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), "com.ko.cds.request.csr.ActivitySearchRequest.sessionUUID cannot be longer than 36 characters ");
			}
		}
		
		@Test
		public void searchActivityRequiredFieldValidation(){
			
		}
		
		@Test
		public void searchActivityTagObjectParamValidation(){
			SearchActivityTagObject tags = new SearchActivityTagObject();
			ActivitySearchRequest req = new ActivitySearchRequest();
			req.setMemberId(new BigInteger("1"));
			req.setTagObject(tags);
			try {
				serviceHelper.searchActivity(req);
			} catch (BadRequestException e) {
				// TODO: handle exception
				Assert.assertEquals(e.getMessage(), "com.ko.cds.pojo.activity.TagObject.name cannot be null com.ko.cds.pojo.activity.TagObject.value cannot be null ");
			}
		}
			
}
