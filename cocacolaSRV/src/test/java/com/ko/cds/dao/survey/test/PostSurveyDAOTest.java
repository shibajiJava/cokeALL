package com.ko.cds.dao.survey.test;

import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.ErrorCode;




@Component
@Transactional
public class PostSurveyDAOTest extends CDSTest{
	private static Logger logger = LoggerFactory.getLogger(PostSurveyDAOTest.class);
	@Autowired
	private SurveyDAO surveyDAO;
	@Autowired
	private SessionIdDAO sessionIdDAO;
	// postSurveyTest Case
	
	
	//Test case : If MamberId and SurveyId is equal then record will be update.
	//Happy path
	@Test
	@Rollback(true)
	public void testWithSameMemberIdAndSurveyId(){
		
		SurveyTrans postSurveyRequest=new SurveyTrans();
		BigInteger sessionId = sessionIdDAO.getSessionID("1esd23c");
		
		postSurveyRequest.setSessionId(sessionId);		
		postSurveyRequest.setSurveyId(new BigInteger("1"));
		postSurveyRequest.setMemberId(new BigInteger("1"));	
		postSurveyRequest.setQuestionId(new BigInteger("3"));
		postSurveyRequest.setAnswerId(new BigInteger("15"));	
		
		surveyDAO.postSurvey(postSurveyRequest);
		logger.info("Updated One Record for PostSurvey.");
		
	}
	

	
	//Test case : If MamberId and SurveyId is not equal then new record will be inserted.
	//Happy Path
	@Test
	@Rollback(true)
	public void testWithDiffMemberIdAndSurveyId(){
		
		SurveyTrans postSurveyRequest=new SurveyTrans();		
		BigInteger sessionId = sessionIdDAO.getSessionID("1esd23c");
		
		postSurveyRequest.setSessionId(sessionId);
		postSurveyRequest.setSurveyId(new BigInteger("2"));
		postSurveyRequest.setMemberId(new BigInteger("1"));	
		postSurveyRequest.setQuestionId(new BigInteger("5"));
		postSurveyRequest.setAnswerId(new BigInteger("19"));		
		
		
		surveyDAO.postSurvey(postSurveyRequest);
		 logger.info("Inserted One Record for postSurvey.");
		
	}
	
	
	//Test case : If SurveyId is Unavailable in DB - FK Violation 	
		@Test(expected=DataIntegrityViolationException.class)
		public void testInvalidSurveyId(){
						
			SurveyTrans postSurveyRequest=new SurveyTrans();			
			BigInteger sessionId = sessionIdDAO.getSessionID("1esd23c");
			
			postSurveyRequest.setSessionId(sessionId);
			postSurveyRequest.setSurveyId(new BigInteger("6"));
			postSurveyRequest.setMemberId(new BigInteger("2"));	
			postSurveyRequest.setQuestionId(new BigInteger("5"));
			postSurveyRequest.setAnswerId(new BigInteger("19"));
			
			surveyDAO.postSurvey(postSurveyRequest);
			 logger.info("SurveyId is not available in DB ");
			
		}
		
		//Test case : If MemberId is Unavailable in DB - FK Violation
				@Test(expected=DataIntegrityViolationException.class)			
				public void testInvalidMemberId(){
							
					SurveyTrans postSurveyRequest=new SurveyTrans();
					BigInteger sessionId = sessionIdDAO.getSessionID("1esd23c");
					
					postSurveyRequest.setSessionId(sessionId);
					postSurveyRequest.setSurveyId(new BigInteger("2"));
					postSurveyRequest.setMemberId(new BigInteger("8"));	
					postSurveyRequest.setQuestionId(new BigInteger("5"));
					postSurveyRequest.setAnswerId(new BigInteger("19"));		
					
					
					surveyDAO.postSurvey(postSurveyRequest);
					 logger.info("MemberId is not available in DB. ");
				}

				
				//TestCase - Max Session Id - FK Violation 
				@Test(expected=DataIntegrityViolationException.class)
				public void testWithInvalidSessionId(){
					
					SurveyTrans surveyTrans=new SurveyTrans();
					BigInteger sessionUuid = sessionIdDAO.getSessionID("uyutygf7867-766-ere0-4344-dddd-4444rere0-4344-dddd-4444r");
					
					surveyTrans.setSessionId(sessionUuid);		
					surveyTrans.setMemberId(new BigInteger("2"));	
					surveyTrans.setQuestionId(new BigInteger("3"));
					surveyTrans.setAnswerId(new BigInteger("19"));
							
					
					surveyDAO.postSurvey(surveyTrans);
					logger.info("Session UUID is more than 32 Char");
					
					
				}
}
