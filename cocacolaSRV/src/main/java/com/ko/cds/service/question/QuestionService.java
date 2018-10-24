package com.ko.cds.service.question;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.question.QuestionDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.question.AnswerOptionsObject;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.request.question.PostQuestionRequest;
import com.ko.cds.service.helper.ICacheService;
import com.ko.cds.service.survey.SurveyService;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

@Component
public class QuestionService implements IQuestionService{
	private static final Logger logger = LoggerFactory.getLogger(SurveyService.class);
	
	@Autowired
    private QuestionDAO questionDAO;
	@Autowired
	private ICacheService cacheService;
	
	public QuestionDAO getQuestionDAO() {
		return questionDAO;
	}


	public void setQuestionDAO(QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}


	public ICacheService getCacheService() {
		return cacheService;
	}


	public void setCacheService(ICacheService cacheService) {
		this.cacheService = cacheService;
	}


	//@Autowired
	//private SessionIdDAO sessionIdDAO;
	public static ApplicationContext context= new ClassPathXmlApplicationContext("classpath:resources/junit/servlet-context-test.xml");
	

	@Transactional
	@AopServiceAnnotation
	@Override
	public Response postQuestion(PostQuestionRequest postQuestionRequest)
			throws BadRequestException {

		SurveyTrans surveyTrans = null;
		String validationErrors=CDSOUtils.validate(postQuestionRequest);
	    if(validationErrors != null){
	     return	CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, validationErrors);
	    }
		if(postQuestionRequest == null || postQuestionRequest.getSurveyId()==null || postQuestionRequest.getMemberId()==null || postQuestionRequest.getQuestionId()==null){		
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, validationErrors);
		}		
		try{
			
			//sessionIdDAO=context.getBean(SessionIdDAO.class);
			BigInteger sessionId = null;
			if(postQuestionRequest.getSessionUuid() != null){
				sessionId=cacheService.getSessionID(postQuestionRequest.getSessionUuid());
			}
			questionDAO=context.getBean(QuestionDAO.class);
			List<SurveyTrans> serveyTransList = populateServeyTrans(postQuestionRequest,sessionId);
			for(int i=0;i<serveyTransList.size();i++){
				surveyTrans=serveyTransList.get(i);
				questionDAO.postQuestion(surveyTrans);
			}
			CDSOUtils.createOKResponse(null);
			logger.info("Okey ");
			
		}catch(DuplicateKeyException rex){
			logger.trace("PostSurvey :", rex);		
			throw new BadRequestException(rex.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT);
		
		}catch(DataIntegrityViolationException rex){
			logger.trace("PostSurvey :", rex);
			throw new BadRequestException(rex.getMessage(),ErrorCode.QUESTION_NOT_FOUND);		   
		}
		
		return CDSOUtils.createOKResponse(null);
	}


	private List<SurveyTrans> populateServeyTrans(PostQuestionRequest postQuestionRequest,BigInteger sessionUuid){
		List<SurveyTrans> surveyTransList = new ArrayList<SurveyTrans>();
		SurveyTrans surveyTrans = null;		
		List<com.ko.cds.pojo.question.Question> questionLists=postQuestionRequest.getQuestions();
		for (com.ko.cds.pojo.question.Question question:questionLists) {
				for (AnswerOptionsObject answerOptionsObject : question.getAnswerOptions()) {
					if("true".equals(answerOptionsObject.getIsSelectedAnswer())){
					Date dt=new Date();
					surveyTrans = new SurveyTrans();
					surveyTrans.setSessionId(sessionUuid);
					surveyTrans.setSurveyId(postQuestionRequest.getSurveyId());
					surveyTrans.setMemberId(postQuestionRequest.getMemberId());
					surveyTrans.setQuestionId(question.getQuestionId());
					surveyTrans.setAnswerId(answerOptionsObject.getOptionId());
					java.sql.Timestamp sqlTimeStamp = new java.sql.Timestamp(dt.getTime());
					surveyTrans.setSurveyDt(sqlTimeStamp);
					surveyTransList.add(surveyTrans);
					}
				}
		}
		return surveyTransList;
	}

}
