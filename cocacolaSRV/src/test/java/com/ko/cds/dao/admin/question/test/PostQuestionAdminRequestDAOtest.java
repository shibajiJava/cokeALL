package com.ko.cds.dao.admin.question.test;

import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.pojo.admin.survey.SurveyAdmin;
import com.ko.cds.pojo.admin.survey.SurveyTransAdmin;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.request.admin.question.PostQuestionAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyAdminRequest;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.ErrorCode;




@Component
@Transactional
public class PostQuestionAdminRequestDAOtest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(PostQuestionAdminRequestDAOtest.class);
	@Autowired
	private SurveyAdminDAO surveyDAO;
	@Autowired
	private SurveyServiceAdminHelper PostSurveyAdmin;
	
	
	//Test case : With all parameters for SUrvey Admin.
	//Happy path
	@Test
	@Rollback(true)
	public void testWithallparametersQuestions(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		
		PostQuestionAdminRequest postSurveyRequest=new PostQuestionAdminRequest();
		//BigInteger surveyAdmin=PostSurveyAdmin.postQuestionAdmin(postSurveyRequest);
		//surveyDAO.postSurveyAdmin(surveyAdmin);
		
	}

		//Test case : With all parameters null for SUrvey Admin.
		

		public void testWithallparametersNullAndSurveyId(){
			BigInteger SurveyId= null;
		
			PostSurveyAdminRequest postSurveyRequest=new PostSurveyAdminRequest();
			postSurveyRequest.setSurveyDescription("");
			postSurveyRequest.setTypeCode("");
			postSurveyRequest.setCountryCode("");
			postSurveyRequest.setLanguageCode("");
			postSurveyRequest.setExpirationDate(null);
			postSurveyRequest.setFrequencyCode("");
			try {
				SurveyId=PostSurveyAdmin.postSurveyAdmin(postSurveyRequest);
			} catch (CustomAdminException e) {
				log.info("PointServiceHelperTest :"+e);
			}
			postSurveyRequest.setSurveyId(SurveyId);
			SurveyAdmin surveyAdmin=PostSurveyAdmin.populateserveyAdmin(postSurveyRequest);
			surveyDAO.postSurveyAdmin(surveyAdmin);
		}
			//Test case : Without surveyDescription and typecode null for SUrvey Admin.
       
			
			public void testWithparametSurveyId(){
				BigInteger SurveyId= null;
				@SuppressWarnings("deprecation")
				Date mydate = new Date("2014-12-20T12:29:02.000Z");
				
				PostSurveyAdminRequest postSurveyRequest=new PostSurveyAdminRequest();
				postSurveyRequest.setSurveyDescription("");
				postSurveyRequest.setTypeCode("");
				postSurveyRequest.setCountryCode("IN");
				postSurveyRequest.setLanguageCode("EN");
				postSurveyRequest.setExpirationDate(mydate);
				postSurveyRequest.setFrequencyCode("Freq");
				try {
					SurveyId=PostSurveyAdmin.postSurveyAdmin(postSurveyRequest);
				} catch (CustomAdminException e) {
					log.info("PointServiceHelperTest :"+e);
				}
				postSurveyRequest.setSurveyId(SurveyId);
				SurveyAdmin surveyAdmin=PostSurveyAdmin.populateserveyAdmin(postSurveyRequest);
				surveyDAO.postSurveyAdmin(surveyAdmin);
			}
		
			//Test case : Without countrycode,languagecode,frequency  null for SUrvey Admin.
       
			
			public void testWithpmetersSurveyId(){
				BigInteger SurveyId= null;
				@SuppressWarnings("deprecation")
				Date mydate = new Date("2014-12-20T12:29:02.000Z");
				
				PostSurveyAdminRequest postSurveyRequest=new PostSurveyAdminRequest();
				postSurveyRequest.setSurveyDescription("Formula 1 Promotions");
				postSurveyRequest.setTypeCode("ZZZ");
				postSurveyRequest.setCountryCode("");
				postSurveyRequest.setLanguageCode("");
				postSurveyRequest.setExpirationDate(mydate);
				postSurveyRequest.setFrequencyCode("");
				try {
					SurveyId=PostSurveyAdmin.postSurveyAdmin(postSurveyRequest);
				} catch (CustomAdminException e) {
					log.info("PointServiceHelperTest :"+e);
				}
				postSurveyRequest.setSurveyId(SurveyId);
				SurveyAdmin surveyAdmin=PostSurveyAdmin.populateserveyAdmin(postSurveyRequest);
				surveyDAO.postSurveyAdmin(surveyAdmin);
			}
			
			//Test case : Without Expirationdate and Surveyid null for SUrvey Admin.
       
			
			public void testWithparametersSurveyId(){
				BigInteger SurveyId= null;
		
				PostSurveyAdminRequest postSurveyRequest=new PostSurveyAdminRequest();
				postSurveyRequest.setSurveyDescription("Formula 1 Promotions");
				postSurveyRequest.setTypeCode("ZZZ");
				postSurveyRequest.setCountryCode("IN");
				postSurveyRequest.setLanguageCode("EN");
				postSurveyRequest.setExpirationDate(null);
				postSurveyRequest.setFrequencyCode("Freq");
				postSurveyRequest.setSurveyId(null);
				SurveyAdmin surveyAdmin=PostSurveyAdmin.populateserveyAdmin(postSurveyRequest);
				surveyDAO.postSurveyAdmin(surveyAdmin);
			}
	
	


	
	
}