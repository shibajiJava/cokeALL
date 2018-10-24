package com.ko.cds.dao.admin.quetion.survey.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.pojo.admin.survey.QuestionIdAdmin;
import com.ko.cds.request.admin.survey.PostSurveyQuestionAdminRequest;
import com.ko.cds.request.admin.survey.QuestionIdS;
import com.ko.cds.test.CDSTest;





@Component
@Transactional
public class PostSurveyQuestionAdminDAOTest extends CDSTest{
	private static Logger log = LoggerFactory.getLogger(PostSurveyQuestionAdminDAOTest.class);
	@Autowired
	private SurveyAdminDAO surveyDAO;
	@Autowired
	private SurveyServiceAdminHelper PostSurveyAdmin;
	
	
	//Test case : With all parameters for Question SUrvey Admin.
	//Happy path
	@Test
	@Rollback(true)
	public void testWithallparameters(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		PostSurveyQuestionAdminRequest postSurveyRequest=new PostSurveyQuestionAdminRequest();
		BigInteger Value= BigInteger.valueOf(45454545);
		List<QuestionIdAdmin> Listofquestions = new ArrayList<QuestionIdAdmin>();
		List<QuestionIdS> questionLists = postSurveyRequest.getQuestionIds();
		postSurveyRequest.setSurveyID(Value);
		for(QuestionIdS questions:questionLists){
			QuestionIdAdmin question = new QuestionIdAdmin();
			question.setQuestionIds(questions.getQuestionIds());
			Listofquestions.add(question);
			
		}
		
		surveyDAO.postSurveyQuestionAdmin(Listofquestions);
		
		
		
	}

	//Test case : Without survey parameter for Question SUrvey Admin.
	//
	@Test
	@Rollback(true)
	public void testWithoutSurveyIDparameter(){
		BigInteger SurveyId= null;
		@SuppressWarnings("deprecation")
		Date mydate = new Date("2014-12-20T12:29:02.000Z");
		PostSurveyQuestionAdminRequest postSurveyRequest=new PostSurveyQuestionAdminRequest();
		BigInteger Value= BigInteger.valueOf(45454545);
		List<QuestionIdAdmin> Listofquestions = new ArrayList<QuestionIdAdmin>();
		List<QuestionIdS> questionLists = postSurveyRequest.getQuestionIds();
		postSurveyRequest.setSurveyID(null);
		for(QuestionIdS questions:questionLists){
			QuestionIdAdmin question = new QuestionIdAdmin();
			question.setQuestionIds(questions.getQuestionIds());
			Listofquestions.add(question);
			
		}
		surveyDAO.postSurveyQuestionAdmin(Listofquestions);
		
		
		
	}
	
	//Test case : Without survey parameter for Question SUrvey Admin.
		//
		@Test
		@Rollback(true)
		public void testWithoutQuestionIDparameter(){
			BigInteger SurveyId= null;
			@SuppressWarnings("deprecation")
			Date mydate = new Date("2014-12-20T12:29:02.000Z");
			PostSurveyQuestionAdminRequest postSurveyRequest=new PostSurveyQuestionAdminRequest();
			BigInteger Value= BigInteger.valueOf(45454545);
			List<QuestionIdAdmin> Listofquestions = new ArrayList<QuestionIdAdmin>();
			postSurveyRequest.setQuestionIds(null);
			List<QuestionIdS> questionLists = postSurveyRequest.getQuestionIds();
			postSurveyRequest.setSurveyID(null);
			for(QuestionIdS questions:questionLists){
				QuestionIdAdmin question = new QuestionIdAdmin();
				question.setQuestionIds(questions.getQuestionIds());
				Listofquestions.add(question);
				
			}
			surveyDAO.postSurveyQuestionAdmin(Listofquestions);
			
			
			
		}

	
	
}