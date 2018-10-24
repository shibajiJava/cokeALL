package com.ko.cds.service.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.WebApplicationException;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.janrainIntegration.CommunicationOpt;
import com.ko.cds.pojo.janrainIntegration.Result;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.survey.Answer;
import com.ko.cds.pojo.survey.OptPreference;
import com.ko.cds.pojo.survey.Question;
import com.ko.cds.pojo.survey.Survey;
import com.ko.cds.pojo.survey.SurveyTrans;
import com.ko.cds.request.survey.GetSurveyRequest;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.request.survey.PostSurveyRequest;
import com.ko.cds.response.survey.GetOptsResponse;
import com.ko.cds.utils.AnswerComparable;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;
import com.ko.cds.utils.QuestionComparable;


@Component
public class SurveyServiceHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(SurveyServiceHelper.class);

	@Autowired
	private SurveyDAO surveyDAO;
	@Autowired
	private SessionIdDAO sessionIdDAO;
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO;

	@SuppressWarnings("unchecked")
	public Survey getSurvey(GetSurveyRequest getSurveyRequest)
			throws BadRequestException {
		// Response
		Survey survey = null;

		// Validating request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(getSurveyRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("getSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		// two scenarios - with/out memberID
		try {
			if (getSurveyRequest.getMemberId() != null) {

				SurveyTrans st = new SurveyTrans();
				st.setSurveyId(getSurveyRequest.getSurveyId());
				st.setMemberId(getSurveyRequest.getMemberId());

				List<SurveyTrans> surveyAnswered = surveyDAO.getAnsweredSurvey(st);
				if(surveyAnswered != null && surveyAnswered.size()>0 && surveyAnswered.get(0).getAnswerId() == null){
					survey = surveyDAO.getSurveyInfo(getSurveyRequest);
				}else{
					survey = surveyDAO.getSurveyInfoForMember(getSurveyRequest);
				}
				
				//validate survey Before Processing
				surveyValidation(survey);
				
				fillSurveyInfo(survey);
				// Processing the result to mark the answers which are available
				// already in survey_trans
	            /*if(surveyAnswered != null && surveyAnswered.size()>0 && surveyAnswered.get(0).getAnswerId() != null){
	            	survey = processResults(survey,st);
	            }*/
				if(surveyAnswered != null && surveyAnswered.size()>0){
	            	survey = processResults(survey,st);
	            }
			} else {
				survey = surveyDAO.getSurveyInfo(getSurveyRequest);
				//validate survey Before Processing
				surveyValidation(survey);
				survey = fillSurveyInfo(survey);
			}
		} catch (DataIntegrityViolationException rex) {
			//logger.trace("getSurvey API :", rex);
			throw new BadRequestException(rex.getMessage(),rex);
		} catch (BadSqlGrammarException rex) {
			//logger.trace("getSurvey API :", rex);
			throw new BadRequestException(rex.getMessage(),rex);
		}

		logger.info("Response from getSurvey API : " + survey);
		return survey;
	}
	
	private Survey fillSurveyInfo(Survey survey){

		Set<Question> oortedQset = new HashSet<Question>();
		for(Question question :survey.getQuestions() )
		{
			List anslist = new ArrayList(question.getAnswerOptions());
			Collections.sort(anslist, new AnswerComparable());
			question.setAnswerOptions(anslist);
			oortedQset.add(question);
		}
		
		List quelist = new ArrayList(oortedQset);
		Collections.sort(quelist, new QuestionComparable());
		survey.setQuestions(quelist);
		
		return survey;
	}

	private void surveyValidation(Survey survey) throws BadRequestException{
		// Survey expiration check and Survey Not Found check
				if (survey != null) {
					// Expiration check
					if (survey.getExpirationDate() != null) {
						SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
						Date today = new Date();
						Date expDate = null;
						String date = survey.getExpirationDate();
						try {
							today = sdf1.parse(sdf1.format(today));
							expDate = sdf2.parse(date);

						} catch (ParseException e) {
							throw new WebApplicationException(e);
						}
						logger.info("today : " + today + " expDate : " + expDate
								+ " expDate.compareTo(today) "
								+ expDate.compareTo(today));

						if (expDate.compareTo(today) < 0) {
							logger.info("Survey is Expired");
							throw new BadRequestException(
									ErrorCode.SURVEY_EXPIRED_DESC,
									ErrorCode.SURVEY_EXPIRED);
						}
					}
				} else {
					throw new BadRequestException(ErrorCode.SURVEY_NOT_FOUND_DESC,
							ErrorCode.SURVEY_NOT_FOUND);
				}
	}
	/**
	 * This method is to mark the answer in the survey response, which will be
	 * already available in survey_trans table
	 * 
	 * @param survey
	 *            - survey response
	 * @return survey - survey response
	 */
	private Survey processResults(Survey survey, SurveyTrans sTrans) {
		
		boolean isAllFalse=false;
		
		List<SurveyTrans> surveyAnswered = surveyDAO.getAnsweredSurvey(sTrans);
		
		List<BigInteger> allFalseList =  getAllFalseAns(surveyAnswered);
		
		
		for(SurveyTrans surveyTrans:surveyAnswered)
		{
			if(surveyTrans.getSurveyId().equals(survey.getSurveyId()))
			{
				for(Question question : survey.getQuestions())
				{
					if(question.getQuestionId().equals(surveyTrans.getQuestionId()))
					{
						isAllFalse=false;
						for(Answer answer : question.getAnswerOptions())
						{
							
							
							
							if(answer.getOptionId().equals(surveyTrans.getAnswerId()))
							{
								if(surveyTrans.getTextAnswer()!=null)
								{
									answer.setTextAnswer(surveyTrans.getTextAnswer());
								}
								
								if(!allFalseList.contains(question.getQuestionId()))
								{
									answer.setIsSelectedAnswer("true");
								}
								else
								{
									answer.setIsSelectedAnswer("");
								}
							}
						}
					}
				}
			}
		}
		
		
		/*logger.info("Entered into method : processResults");
		if ((survey != null) && (survey.getQuestions() != null)
				&& (!survey.getQuestions().isEmpty())) {
			for (Question question : survey.getQuestions()) {
				List<Answer> answers = question.getAnswerOptions();
				for (Answer answer : answers) {
					if (answer.getMarkedAnswers().size() > 0) {
						for (SurveyTrans st : surveyAnswered) {
							if ((survey.getSurveyId().compareTo(
									st.getSurveyId()) == 0)
									&& (question.getQuestionId().compareTo(
											st.getQuestionId()) == 0)
									&& (answer.getOptionId().compareTo(
											st.getAnswerId()) == 0)) {
								answer.setIsSelectedAnswer("true");
							}
						}
					}
				}
			}
		}*/
		return survey;
	}

	private List<BigInteger> getAllFalseAns(List<SurveyTrans> surveyAnswered) {
		
		List<BigInteger> returnList =  new ArrayList<BigInteger>();
		for(SurveyTrans surveyTrans: surveyAnswered)
		{
			if(surveyTrans.getAnswerId()==null)
			{
				returnList.add(surveyTrans.getQuestionId());
			}
		}
		
		return returnList;
	}

	/**
	 * Helper method for postSurvey service.
	 * 
	 * @param postSurveyrequest
	 * @throws BadRequestException
	 */
	public void postSurvey(PostSurveyRequest postSurveyRequest)
			throws BadRequestException {
		SurveyTrans surveyTrans = null;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(postSurveyRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postSurveyRequest == null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			long start = System.nanoTime();
				BigInteger sessionUUID = sessionIdDAO
						.getSessionID(postSurveyRequest.getSessionUUID());
				long end = System.nanoTime();
	            long elapsedTime = end - start;
	            double seconds = (double) elapsedTime / 1000000000.0;
	            logger.info("===========Time Taken by getSessionID ============> "+String.valueOf(seconds));
				List<SurveyTrans> serveyTransList = populateServeyTrans(
						postSurveyRequest, sessionUUID);
				logger.info("In hepler class");
				if(serveyTransList!=null && serveyTransList.size()>0)
				{
					start = System.nanoTime();
					surveyDAO.postSurveyList(serveyTransList);
					end = System.nanoTime();
		             elapsedTime = end - start;
		             seconds = (double) elapsedTime / 1000000000.0;
		            logger.info("===========Time Taken by postSurveyList ============> "+String.valueOf(seconds));
				}
		} catch (DuplicateKeyException rex) {
			//logger.info("PostSurvey :", rex.getMessage());
			throw new BadRequestException(rex.getMessage(),
					ErrorCode.GEN_INVALID_ARGUMENT,rex);

		} catch (DataIntegrityViolationException rex) {
			//logger.info("PostSurvey :", rex.getMessage());
			throw new BadRequestException(rex.getMessage(),
					ErrorCode.SURVEY_NOT_FOUND,rex);
		}
	}

	private List<SurveyTrans> populateServeyTrans(
			PostSurveyRequest postSurveyRequest, BigInteger sessionUUID) {
		
		
		SurveyTrans sTrans = new SurveyTrans();
		sTrans.setSurveyId(postSurveyRequest.getSurveyId());
		sTrans.setMemberId(postSurveyRequest.getMemberId());
		List<SurveyTrans> surveyAnswered = surveyDAO.getAnsweredSurvey(sTrans);
		
		/*Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils.getCurrentUTCTimestamp());*/
		Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
		List<SurveyTrans> surveyTransList = new ArrayList<SurveyTrans>();
		List<SurveyTrans> surveyTransListReturn = new ArrayList<SurveyTrans>();
		SurveyTrans surveyTrans = null;
		List<com.ko.cds.pojo.survey.Question> questionLists = postSurveyRequest
				.getQuestions();
		boolean isSurveyAnswered = false;
		
		for (com.ko.cds.pojo.survey.Question question : questionLists) {
			boolean allFalse=false;
			for (Answer answerOptionsObject : question
					.getAnswerOptions()) {
				if ("true".equals(answerOptionsObject.getIsSelectedAnswer())) {
					surveyTrans = new SurveyTrans();
					surveyTrans.setSessionId(sessionUUID);
					surveyTrans.setSurveyId(postSurveyRequest.getSurveyId());
					surveyTrans.setMemberId(postSurveyRequest.getMemberId());
					surveyTrans.setClientTransactionId(postSurveyRequest.getClientTransactionId());
					surveyTrans.setQuestionId(question.getQuestionId());
					surveyTrans.setAnswerId(answerOptionsObject.getOptionId());
					surveyTrans.setTextAnswer(answerOptionsObject.getTextAnswer());
					surveyTrans.setInsertDt(currentTimestamp);
					
					surveyTransList.add(surveyTrans);
					isSurveyAnswered = true;
					allFalse=true;
				}
			}
			if(!allFalse)
			{
				
				surveyTrans = new SurveyTrans();
				surveyTrans.setSessionId(sessionUUID);
				surveyTrans.setSurveyId(postSurveyRequest.getSurveyId());
				surveyTrans.setMemberId(postSurveyRequest.getMemberId());
				surveyTrans.setClientTransactionId(postSurveyRequest.getClientTransactionId());
				surveyTrans.setQuestionId(question.getQuestionId());
				surveyTrans.setAnswerId(null);
				//surveyTrans.setTextAnswer(answerOptionsObject.getTextAnswer());
				surveyTrans.setAllFalse(true);
				surveyTrans.setInsertDt(currentTimestamp);

				surveyTransList.add(surveyTrans);
				
			}
		}
		//if the survey is not answered and submitted blank set the answered id as null and Question Id as the first Questionid
		/*if(!isSurveyAnswered){
			surveyTransList = new ArrayList<SurveyTrans>();
			surveyTrans = new SurveyTrans();
			surveyTrans.setSessionId(sessionUUID);
			surveyTrans.setSurveyId(postSurveyRequest.getSurveyId());
			surveyTrans.setMemberId(postSurveyRequest.getMemberId());
			surveyTrans.setClientTransactionId(postSurveyRequest.getClientTransactionId());
			surveyTrans.setQuestionId(questionLists.get(0).getQuestionId());
			surveyTrans.setAnswerId(null);
			//surveyTrans.setTextAnswer(answerOptionsObject.getTextAnswer());
			surveyTrans.setInsertDt(currentTimestamp);

			surveyTransList.add(surveyTrans);
			//surveyTransListReturn=surveyTransList;
		}*/
		removePastAsnwer(surveyTransList,surveyAnswered);
		return surveyTransList;
	}

	
	/**
	 * This mehod is to chk previous answered question and new answered question 
	 * @param surveyTransList
	 * @param surveyAnswered
	 * @return
	 */
	private void removePastAsnwer(
			List<SurveyTrans> surveyTransList, List<SurveyTrans> surveyAnswered) {
		// TODO Auto-generated method stub
		
		//List<SurveyTrans> surveyTransExtra =  new ArrayList<SurveyTrans>();
		CopyOnWriteArrayList<SurveyTrans> answeredSurvey = new CopyOnWriteArrayList<SurveyTrans>();
		
		
		
		
		for(SurveyTrans SurveyTransNew : surveyTransList)
		{
			
			if(SurveyTransNew.getAnswerId()!=null)
			{
				List<SurveyTrans> surveyAnsweredWithQid =  surveyDAO.getAnsweredSurveyWithQuestionId(SurveyTransNew);
				
			}
		}
		
		
		
		for(SurveyTrans surveyTransAnswred:answeredSurvey)
		{
			for(SurveyTrans SurveyTransNew : surveyTransList)
			{
				if(surveyTransAnswred.getSurveyId().equals(SurveyTransNew.getSurveyId()))
				{
					if(surveyTransAnswred.getQuestionId().equals(SurveyTransNew.getQuestionId()))
					{
						
						if(SurveyTransNew.getAnswerId()!=null && surveyTransAnswred.getAnswerId().equals(SurveyTransNew.getAnswerId()))
						{
							answeredSurvey.remove(surveyTransAnswred);
						}
					}
				}
			}
		}
	}

	/**
	 * Helper method for postOpts service.
	 * 
	 * @param optsRequest
	 * @throws BadRequestException
	 */
	public void postOpts(OptsRequest optsRequest) throws BadRequestException {
		int resp = 0;
		final OptsRequest optsReq = optsRequest;
		
		// Validating the request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(optsReq,
					CDSConstants.POSTOPTS_PROFILE_NAME, false);
		} catch (IOException e) {
			logger.error("postOpts : " + e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			long start = System.nanoTime();
			resp = surveyDAO.postOpts(optsReq);
			long end = System.nanoTime();
            long elapsedTime = end - start;
            double seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by postOpts ============> "+String.valueOf(seconds));
			logger.info("postOpts - No. of records inserted : " + resp);						
		} catch (DuplicateKeyException rex) {
			//logger.error("postOpts :", rex);
			throw new BadRequestException(rex.getMessage(),
					ErrorCode.GEN_INVALID_ARGUMENT,rex);
		} catch (DataIntegrityViolationException rex) {
			//logger.trace("postOpts :", rex);
			throw new BadRequestException(rex.getMessage(),
					ErrorCode.GEN_INVALID_ARGUMENT,rex);
		}				
		
		//Janrain sync
		if (resp > 0) {
			final MemberInfo memberInfo = memberDAO.getMemberUUIDbyMemID(optsReq
					.getMemberId());
					
			
			if ((memberInfo != null) && (memberInfo.getUuid() != null) && !(memberInfo.getUuid().isEmpty())) {
				logger.info("UUID to update janrain : "+memberInfo.getUuid());	
				/*ExecutorService es = Executors.newFixedThreadPool(1);
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Future future = es.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	updateJanrain(optsReq, memberInfo.getUuid());
		                        return null;
		                    }
		                });*/
				CDSOUtils.JanrainPostOptsTaskList.add(new UpdateJanrainPostOptsTask(optsReq, memberInfo.getUuid()));
			} else {
				logger.info("UUID not set to janrain UPDATE, may be its a lite account ");
			}
		}
	}

	/**
	 * Method to update opts into janrain.
	 * @param optsRequest
	 * @param janrainUuid
	 * @throws JSONException 
	 */
	private void updateJanrain(OptsRequest optsRequest, String janrainUuid) throws JSONException {
		// For janrain update - formatting json
		JSONArray array = new JSONArray();
		JSONObject obj = null;
		String janrainJson = "";
		
		OptsRequest optsReqNullAcptDt = new OptsRequest();
		optsReqNullAcptDt.setMemberId(optsRequest.getMemberId());
		optsReqNullAcptDt.setSessionUUID(optsRequest.getSessionUUID());
		List<OptPreference> optsPrefNullAcptdt = new ArrayList<OptPreference>();
		
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String environment = configProp.get(CDSConstants.ENVORONMENT_KEY);
		String janrainClientId  = 	configProp.get(CDSConstants.JANRAIN_CLIENT_ID_KEY+"_"+environment);
		try {
			for (OptPreference opt : optsRequest.getOpts()) {
				if(opt.getAcceptedDate() != null && opt.getAcceptedDate().length() > 0){
				obj = new JSONObject();
				obj.put(CDSConstants.JANRAIN_OPTS_ID, opt.getCommunicationTypeName());
				obj.put(CDSConstants.JANRAIN_OPTS_CLIENT_ID, janrainClientId);
				obj.put(CDSConstants.JANRAIN_OPTS_ACCEPTED, opt.getOptInIndicator());
				obj.put(CDSConstants.JANRAIN_OPTS_TYPE, opt.getType());
				obj.put(CDSConstants.JANRAIN_OPTS_ACCEPT_DATE, opt.getAcceptedDate());
				obj.put(CDSConstants.JANRAIN_OPTS_SCHEDULE_PREF, opt.getSchedule());
				obj.put(CDSConstants.JANRAIN_OPTS_TYPE, opt.getType());
				array.put(obj);
				}else{
					optsPrefNullAcptdt.add(opt);
				}
			}
			optsReqNullAcptDt.setOpts(optsPrefNullAcptdt);
			obj = new JSONObject();
			obj.put(CDSConstants.JANRAIN_OPTS, array);
			janrainJson = obj.toString();
			logger.info("Janrain json for postOpts : " + janrainJson);
		} catch (JSONException e) {
			logger.error("JSONException : " + e.getMessage());
		}
		if(optsReqNullAcptDt.getOpts().size() > 0){
			replaceComOptsInJanrain(optsReqNullAcptDt,janrainClientId);
		}
		Map<String, String> janrainResponse = CDSOUtils.updateCDSOIDtoJanrain(
				janrainJson, janrainUuid);
		
		if (janrainResponse.get("StatusCode").equals("200")
				&& janrainResponse.get("ResponseSTR").equals(
						"{\"stat\":\"ok\"}")) {
			logger.info("janrain insert is successful");
		} else {
			logger.info("Status Code :" + janrainResponse.get("StatusCode")
					+ " Response " + janrainResponse.get("ResponseSTR")
					+ " Exception " + janrainResponse.get("Exception"));
		}

	}
	
	public void replaceComOptsInJanrain(OptsRequest optsReqNullAcptDt, String janrainClientId) throws JSONException{
		String janrainUUID=(memberDAO.getMemberUUIDbyMemID(optsReqNullAcptDt.getMemberId())).getUuid();
		Map<String,String> janrainReaponse = CDSOUtils.findOptsInfo(janrainUUID);
		Result allMemberInfo=null;
		if(janrainReaponse.get("StatusCode").equals("200") &&  janrainReaponse.get("ResponseSTR").contains("\"stat\":\"ok\""))
		{
		    ObjectMapper objectMapper = new ObjectMapper();
			try {
				allMemberInfo = objectMapper.readValue(janrainReaponse.get("ResponseSTR"), Result.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//logger.info("Error while parsing the Json Received from Janrain for after the find of the Communication Opts Info");
				logger.error("Error while parsing the Json Received from Janrain for after the find of the Communication Opts Info",e);
			}
			List<CommunicationOpt> commOpts = allMemberInfo.getResults().get(0).getCommunicationOpts();
			for (OptPreference communicationOpt : optsReqNullAcptDt.getOpts()) {
				for (CommunicationOpt janrainOpts : commOpts) {
					if(janrainOpts.getOptId().equals(communicationOpt.getCommunicationTypeName())){
						JSONObject obj = new JSONObject();
						String janrainJson = "";
						obj.put(CDSConstants.JANRAIN_OPTS_ID, communicationOpt.getCommunicationTypeName());
						obj.put(CDSConstants.JANRAIN_OPTS_CLIENT_ID, janrainClientId);
						obj.put(CDSConstants.JANRAIN_OPTS_ACCEPTED, communicationOpt.getOptInIndicator());
						obj.put(CDSConstants.JANRAIN_OPTS_TYPE, communicationOpt.getType());
						obj.put(CDSConstants.JANRAIN_OPTS_ACCEPT_DATE, communicationOpt.getAcceptedDate());
						obj.put(CDSConstants.JANRAIN_OPTS_SCHEDULE_PREF, communicationOpt.getSchedule());
						obj.put(CDSConstants.JANRAIN_OPTS_TYPE, communicationOpt.getType());
						janrainJson = obj.toString();
						CDSOUtils.replaceCommOptsFromJanrain(janrainUUID,janrainOpts.getId(),janrainJson);
					}
				}
			}
			
		}
		else
		{
		    logger.info("Status Code :"+ janrainReaponse.get("StatusCode")+" Response "+janrainReaponse.get("ResponseSTR")+" Exception "+janrainReaponse.get("Exception"));
		}
		
	}
	/**
	 * Helper method for getOpts service.
	 * @param optsRequest
	 * @return
	 * @throws BadRequestException
	 */
	public GetOptsResponse getOpts(OptsRequest optsRequest)
			throws BadRequestException {
		
		GetOptsResponse response = null;
		
		// Validating request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(optsRequest,
					CDSConstants.GETOPTS_PROFILE_NAME, false);
		} catch (IOException e) {
			logger.error("getOpts :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}

		try {
			long start = System.nanoTime();
			response = surveyDAO.getOpts(optsRequest);
			long end = System.nanoTime();
            long elapsedTime = end - start;
            double seconds = (double) elapsedTime / 1000000000.0;
            logger.info("===========Time Taken by getOpts ============> "+String.valueOf(seconds));
		} catch (DataIntegrityViolationException rex) {
			//logger.error("getOpts API :", rex);
			throw new BadRequestException(rex.getMessage(),rex);
		} catch (BadSqlGrammarException rex) {
			//logger.error("getOpts API :", rex);
			throw new BadRequestException(rex.getMessage(),rex);
		}

//		if (response == null) {
//			throw new BadRequestException(ErrorCode.QUESTION_NOT_FOUND_DESC,
//					ErrorCode.QUESTION_NOT_FOUND);
//		}
		
		return response;
	}
}
