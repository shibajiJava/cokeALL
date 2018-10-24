package com.ko.cds.dao.question.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.question.QuestionDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.dao.survey.test.PostSurveyDAOTest;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.ErrorCode;


@Component
@Transactional
public class PostQuestionDAOTest extends CDSTest{
	private static Logger logger = LoggerFactory.getLogger(PostSurveyDAOTest.class);
	
	@Autowired
	private QuestionDAO questionDAO;
	@Autowired
	private SessionIdDAO sessionIdDAO;
	
	@Test
	@Rollback(true)
	public void testWithSameMemberIdAndQuestionId(){
		
			SurveyTrans surveyTrans=new SurveyTrans();
			BigInteger sessionUuid = sessionIdDAO.getSessionID("tyt36b-25b");
			
			surveyTrans.setSessionId(sessionUuid);	
			surveyTrans.setSurveyId(new BigInteger("2")); //If surveyID is not with memberId in table in one row then it'll insert
			surveyTrans.setMemberId(new BigInteger("2"));	
			surveyTrans.setQuestionId(new BigInteger("6"));
			surveyTrans.setAnswerId(new BigInteger("21"));
			
			//questionDAO=context.getBean(QuestionDAO.class);		
			questionDAO.postQuestion(surveyTrans);
			logger.info("Updated One Record for PostQuestion.");
	}
	
	@Test
	@Rollback(true)
	public void testWithDiffMemberIdAndQuestionId(){
		
		SurveyTrans surveyTrans=new SurveyTrans();
		BigInteger sessionUuid = sessionIdDAO.getSessionID("we23sgr1");
		
		surveyTrans.setSessionId(sessionUuid);	
		surveyTrans.setSurveyId(new BigInteger("1"));
		surveyTrans.setMemberId(new BigInteger("2"));	
		surveyTrans.setQuestionId(new BigInteger("2"));
		surveyTrans.setAnswerId(new BigInteger("25"));
			
		
		questionDAO.postQuestion(surveyTrans);
		logger.info("Updated One Record for PostQuestion.");
		
		
	}
	
	//TestCase - Unavailable Question Id - FK Violation 
	@Test(expected=DataIntegrityViolationException.class)
	public void testWithInvalidQuestionId(){
	
		
		SurveyTrans surveyTrans=new SurveyTrans();
		
		
		BigInteger sessionUuid = sessionIdDAO.getSessionID("1esd23c");
		
		surveyTrans.setSessionId(sessionUuid);		
		surveyTrans.setMemberId(new BigInteger("2"));	
		surveyTrans.setQuestionId(new BigInteger("10")); // question ID : 10 is not available in DB, so Test with invalid
		surveyTrans.setAnswerId(new BigInteger("19"));
		
		
		questionDAO.postQuestion(surveyTrans);
		logger.info("Updated One Record for PostQuestion.");
		
		
	}
	
	//TestCase - Unavailable Member Id - FK Violation 
	@Test(expected=DataIntegrityViolationException.class)
	public void testWithInvalidMemberId(){
		
		SurveyTrans surveyTrans=new SurveyTrans();
		BigInteger sessionUuid = sessionIdDAO.getSessionID("1esd23c");
		
		surveyTrans.setSessionId(sessionUuid);		
		surveyTrans.setMemberId(new BigInteger("12"));	// member ID : 12 is not available in DB, so Test with invalid
		surveyTrans.setQuestionId(new BigInteger("3"));
		surveyTrans.setAnswerId(new BigInteger("19"));
		
		
		questionDAO.postQuestion(surveyTrans);
		logger.info("Updated One Record for PostQuestion.");
		
		
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
				
		
		questionDAO.postQuestion(surveyTrans);
		logger.info("Updated One Record for PostQuestion.");
		
		
	}
}
