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
import com.ko.cds.pojo.admin.answer.AdminAnswer;
import com.ko.cds.pojo.admin.answer.UpdateAdminAnswer;
import com.ko.cds.pojo.admin.question.UpdateAdminQuestion;
import com.ko.cds.pojo.admin.survey.SurveyAdmin;
import com.ko.cds.pojo.admin.survey.SurveyTransAdmin;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.request.admin.answer.PostAnswerAdminRequest;
import com.ko.cds.request.admin.question.PostQuestionAdminRequest;
import com.ko.cds.request.admin.question.UpdateQuestionAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyAdminRequest;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.test.CDSTest;
import com.ko.cds.utils.ErrorCode;




@Component
@Transactional
public class UpdateQuestionAdminRequestDAOtest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(UpdateQuestionAdminRequestDAOtest.class);
	@Autowired
	private SurveyAdminDAO surveyDAO;
	@Autowired
	private SurveyServiceAdminHelper PostSurveyAdmin;
	
	
	//Test case : With all parameters for SUrvey Admin.
	//Happy path
	@Test
	@Rollback(true)
	public void testWithallparametersAnswers(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(Value);
		postAnswerRequest.setDisplayType("GENERAL");
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setQuestionString("Hey New Questions");
		postAnswerRequest.setQuestionType("Privacy");
		postAnswerRequest.setQuestionID(Value);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}

		//Test case : With all parameters null for SUrvey Admin.
		
	@Test
	@Rollback(true)
	public void testWithoutdipalyorder(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(null);
		postAnswerRequest.setDisplayType("GENERAL");
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setQuestionString("Hey New Questions");
		postAnswerRequest.setQuestionType("Privacy");
		postAnswerRequest.setQuestionID(Value);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutDisplaytype(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(Value);
		postAnswerRequest.setDisplayType(null);
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setQuestionString("Hey New Questions");
		postAnswerRequest.setQuestionType("Privacy");
		postAnswerRequest.setQuestionID(Value);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutupdatedate(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(Value);
		postAnswerRequest.setDisplayType("GENERAL");
		postAnswerRequest.setUpdateDate(null);
		postAnswerRequest.setQuestionString("Hey New Questions");
		postAnswerRequest.setQuestionType("Privacy");
		postAnswerRequest.setQuestionID(Value);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutQuestionstring(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(Value);
		postAnswerRequest.setDisplayType("GENERAL");
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setQuestionString(null);
		postAnswerRequest.setQuestionType("Privacy");
		postAnswerRequest.setQuestionID(Value);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutQuestiontype(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(Value);
		postAnswerRequest.setDisplayType("GENERAL");
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setQuestionString("Hey New Questions");
		postAnswerRequest.setQuestionType(null);
		postAnswerRequest.setQuestionID(Value);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutquestionID(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminQuestion postAnswerRequest=new UpdateAdminQuestion();
		postAnswerRequest.setDisplayOrder(Value);
		postAnswerRequest.setDisplayType("GENERAL");
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setQuestionString("Hey New Questions");
		postAnswerRequest.setQuestionType("Privacy");
		postAnswerRequest.setQuestionID(BigInteger.ZERO);
		
		surveyDAO.updateQuestiononly(postAnswerRequest);
		
	}
	
}