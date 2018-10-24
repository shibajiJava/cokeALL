package com.ko.cds.dao.admin.reason.test;

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
import com.ko.cds.pojo.admin.reason.AdminReason;
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
public class PostReasonAdminRequestDAOtest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(PostReasonAdminRequestDAOtest.class);
	@Autowired
	private SurveyAdminDAO surveyDAO;
	@Autowired
	private SurveyServiceAdminHelper PostSurveyAdmin;
	
	
	//Test case : With all parameters for SUrvey Admin.
	//Happy path
	@Test
	@Rollback(true)
	public void testWithallparametersReason(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		AdminReason postReasonRequest=new AdminReason();
		postReasonRequest.setInsertDate(mydate);
		postReasonRequest.setReasonCode("Promotions for F1");
		postReasonRequest.setReasonCodeDescirption("New F1 has partnered with coke to supply coke drinks");
		surveyDAO.postReasonAdmin(postReasonRequest);		
	}

	@Test
	@Rollback(true)
	public void testWithoutreasoncode(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		AdminReason postReasonRequest=new AdminReason();
		postReasonRequest.setInsertDate(mydate);
		postReasonRequest.setReasonCode(null);
		postReasonRequest.setReasonCodeDescirption("New F1 has partnered with coke to supply coke drinks");
		surveyDAO.postReasonAdmin(postReasonRequest);		
	}
	
	@Test
	@Rollback(true)
	public void testWithoutreasoncodedesc(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		AdminReason postReasonRequest=new AdminReason();
		postReasonRequest.setInsertDate(mydate);
		postReasonRequest.setReasonCode("Promotions for F1");
		postReasonRequest.setReasonCodeDescirption(null);
		surveyDAO.postReasonAdmin(postReasonRequest);		
	}
	
	
}