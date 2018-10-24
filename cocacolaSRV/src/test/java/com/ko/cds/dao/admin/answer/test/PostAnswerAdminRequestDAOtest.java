package com.ko.cds.dao.admin.answer.test;

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
import com.ko.cds.pojo.admin.answer.AdminAnswer;
import com.ko.cds.pojo.admin.survey.SurveyAdmin;
import com.ko.cds.pojo.admin.survey.SurveyTransAdmin;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.request.admin.answer.PostAnswerAdminRequest;
import com.ko.cds.request.admin.question.PostQuestionAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyAdminRequest;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.ErrorCode;




@Component
@Transactional
public class PostAnswerAdminRequestDAOtest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(PostAnswerAdminRequestDAOtest.class);
	@Autowired
	private SurveyAdminDAO surveyDAO;
	@Autowired
	private SurveyServiceAdminHelper PostSurveyAdmin;
	
	
	//Test case : With all parameters for SUrvey Admin.
	//Happy path
	@Test
	@Rollback(true)
	public void testWithallparametersAnswers(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		Long l = new Long(123456789L);
		BigInteger AnsID=BigInteger.valueOf(l);
		AdminAnswer postAnswerRequest=new AdminAnswer();
		postAnswerRequest.setAnswerID(AnsID);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setInsertDate(mydate);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.postAnswersAdmin(postAnswerRequest);
		
	}

		//Test case : With all parameters null for SUrvey Admin.
		
	@Test
	@Rollback(true)
	public void testWithoutAnswerID(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		Long l = new Long(123456789L);
		BigInteger AnsID=BigInteger.valueOf(l);
		AdminAnswer postAnswerRequest=new AdminAnswer();
		postAnswerRequest.setAnswerID(null);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setInsertDate(mydate);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.postAnswersAdmin(postAnswerRequest);
		
	}
    
	@Test
	@Rollback(true)
	public void testWithaoutanswerorder(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		Long l = new Long(123456789L);
		BigInteger AnsID=BigInteger.valueOf(l);
		AdminAnswer postAnswerRequest=new AdminAnswer();
		postAnswerRequest.setAnswerID(AnsID);
		postAnswerRequest.setAnswerOrder(0);
		postAnswerRequest.setInsertDate(mydate);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.postAnswersAdmin(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutoptionstring(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		Long l = new Long(123456789L);
		BigInteger AnsID=BigInteger.valueOf(l);
		AdminAnswer postAnswerRequest=new AdminAnswer();
		postAnswerRequest.setAnswerID(AnsID);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setInsertDate(mydate);
		postAnswerRequest.setOptionString(null);
		surveyDAO.postAnswersAdmin(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutInsetdate(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Long l = new Long(123456789L);
		BigInteger AnsID=BigInteger.valueOf(l);
		AdminAnswer postAnswerRequest=new AdminAnswer();
		postAnswerRequest.setAnswerID(AnsID);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setInsertDate(null);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.postAnswersAdmin(postAnswerRequest);
		
	}

	
	
}