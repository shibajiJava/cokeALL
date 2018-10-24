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
import com.ko.cds.pojo.admin.answer.UpdateAdminAnswer;
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
public class UpdateAnswerAdminRequestDAOtest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(UpdateAnswerAdminRequestDAOtest.class);
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
		UpdateAdminAnswer postAnswerRequest=new UpdateAdminAnswer();
		postAnswerRequest.setAnswerID(Value);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.updateAnswerAdmin(postAnswerRequest);
		
	}

		//Test case : With all parameters null for SUrvey Admin.
		
	@Test
	@Rollback(true)
	public void testWithoutAnswerID(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		UpdateAdminAnswer postAnswerRequest=new UpdateAdminAnswer();
		postAnswerRequest.setAnswerID(BigInteger.ZERO);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.updateAnswerAdmin(postAnswerRequest);
		
	}
    
	@Test
	@Rollback(true)
	public void testWithaoutanswerorder(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminAnswer postAnswerRequest=new UpdateAdminAnswer();
		postAnswerRequest.setAnswerID(Value);
		postAnswerRequest.setAnswerOrder(0);
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.updateAnswerAdmin(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutoptionstring(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminAnswer postAnswerRequest=new UpdateAdminAnswer();
		postAnswerRequest.setAnswerID(Value);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setUpdateDate(mydate);
		postAnswerRequest.setOptionString(null);
		surveyDAO.updateAnswerAdmin(postAnswerRequest);
		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutUpdatedate(){
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		BigInteger Value= BigInteger.valueOf(45454545);
		UpdateAdminAnswer postAnswerRequest=new UpdateAdminAnswer();
		postAnswerRequest.setAnswerID(Value);
		postAnswerRequest.setAnswerOrder(1);
		postAnswerRequest.setUpdateDate(null);
		postAnswerRequest.setOptionString("This is the answer description");
		surveyDAO.updateAnswerAdmin(postAnswerRequest);
		
	}

	
	
}