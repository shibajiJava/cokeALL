package com.ko.cds.service.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.pojo.admin.answer.AdminAnswer;
import com.ko.cds.pojo.admin.answer.UpdateAdminAnswer;
import com.ko.cds.pojo.admin.question.AdminQuestion;
import com.ko.cds.pojo.admin.question.UpdateAdminQuestion;
import com.ko.cds.pojo.admin.reason.AdminReason;
import com.ko.cds.pojo.admin.site.AdminSite;
import com.ko.cds.pojo.admin.site.UpdateAdminSite;
import com.ko.cds.pojo.admin.survey.QuestionIdAdmin;
import com.ko.cds.pojo.admin.survey.SurveyAdmin;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.request.admin.answer.GetAnswerAdminRequest;
import com.ko.cds.request.admin.answer.PostAnswerAdminRequest;
import com.ko.cds.request.admin.answer.UpdateAnswerAdminRequest;
import com.ko.cds.request.admin.question.GetQuestionAdminRequest;
import com.ko.cds.request.admin.question.PostQuestionAdminRequest;
import com.ko.cds.request.admin.question.UpdateQuestionAdminRequest;
import com.ko.cds.request.admin.question.answerIdsList;
import com.ko.cds.request.admin.reason.PostReasonAdminRequest;
import com.ko.cds.request.admin.site.GetSiteAdminRequest;
import com.ko.cds.request.admin.site.PostSiteAdminRequest;
import com.ko.cds.request.admin.site.UpdateSiteAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyAdminRequest;
import com.ko.cds.request.admin.survey.PostSurveyQuestionAdminRequest;
import com.ko.cds.request.admin.survey.QuestionIdS;
import com.ko.cds.response.admin.answer.GetAnswerAdminResponse;
import com.ko.cds.response.admin.answer.GetAnswerAdminResponseList;
import com.ko.cds.response.admin.question.GetQuestionAdminResponse;
import com.ko.cds.response.admin.question.GetQuestionAdminResponseList;
import com.ko.cds.response.admin.site.GetSiteAdminResponse;
import com.ko.cds.response.admin.site.GetSiteAdminResponseList;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

@Component
public class SurveyServiceAdminHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(SurveyServiceAdminHelper.class);

	@Autowired
	private SurveyAdminDAO surveyDAO;
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO;

	/* Admin Survey Helper 1-feb-2016 */
	public BigInteger postSurveyAdmin(
			PostSurveyAdminRequest postSurveyAdminRequest)
			throws CustomAdminException {
		BigInteger surveyId = null;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(postSurveyAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postSurveyAdminRequest == null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			long start = System.nanoTime();
			long end = System.nanoTime();
			long elapsedTime = end - start;
			double seconds = (double) elapsedTime / 1000000000.0;
			/* Generate SurveyID */
			SequenceNumber sequenceNumber = new SequenceNumber(
					CDSConstants.SURVEY_ID_SEQ);
			surveyId = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
			logger.info("===========SURVEY ID ============> "
					+ surveyId.toString());
			if (postSurveyAdminRequest.getSurveyId() == null) {
				postSurveyAdminRequest.setSurveyId(surveyId);
			}
			SurveyAdmin serveyAdmin = populateserveyAdmin(postSurveyAdminRequest);
			logger.info("In hepler class");
			if ((serveyAdmin != null)) {
				start = System.nanoTime();
				surveyDAO.postSurveyAdmin(serveyAdmin);
				end = System.nanoTime();
				elapsedTime = end - start;
				seconds = (double) elapsedTime / 1000000000.0;
				logger.info("===========Time Taken by postSurveyList ============> "
						+ String.valueOf(seconds));
			}
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		} catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}
		return surveyId;
	}

	public SurveyAdmin populateserveyAdmin(
			PostSurveyAdminRequest postSurveyAdminRequest) {
		// TODO Auto-generated method stub
		SurveyAdmin Surveypopulate = new SurveyAdmin();
		Surveypopulate.setCountryCode(postSurveyAdminRequest.getCountryCode());
		Surveypopulate.setExpirationDate(postSurveyAdminRequest
				.getExpirationDate());
		Surveypopulate.setFrequencyCode(postSurveyAdminRequest
				.getFrequencyCode());
		Surveypopulate
				.setLanguageCode(postSurveyAdminRequest.getLanguageCode());
		Surveypopulate.setSurveyDescription(postSurveyAdminRequest
				.getSurveyDescription());
		Surveypopulate.setSurveyId(postSurveyAdminRequest.getSurveyId());
		Surveypopulate.setTypeCode(postSurveyAdminRequest.getTypeCode());
		return Surveypopulate;
	}

	public BigInteger postSurveyQuestionAdmin(
			PostSurveyQuestionAdminRequest postSurveyQuestionAdminRequest)
			throws CustomAdminException {
		BigInteger surveyID = BigInteger.ZERO;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(
					postSurveyQuestionAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postSurveyQuestionAdminRequest == null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			long start = System.nanoTime();
			long end = System.nanoTime();
			long elapsedTime = end - start;
			double seconds = (double) elapsedTime / 1000000000.0;
			// SurveyQuestionAdminId serveyquestionidAdmin=new
			// SurveyQuestionAdminId();
			// Incomming Request need to be saved
			// BeanUtils.copyProperties(serveyquestionidAdmin,
			// postSurveyQuestionAdminRequest);
			surveyID = postSurveyQuestionAdminRequest.getSurveyID();
			List<QuestionIdAdmin> SurveyQuestionslist = populateSurveyQuestionsAdmin(postSurveyQuestionAdminRequest);
			// Check for QuestionID from request if it present in QUESTION table
			// then delete it
			// for (QuestionIdAdmin
			// SurveyQuestions:serveyquestionidAdmin.getQuestions()){
			// if(!((SurveyQuestions.getQuestionId()).equals(null))){
			//
			// }
			// }
			// check for question overlapping with other Survey
			// check if QuestionId already exsist in SURVEY_QUESTION TABLE
			SurveyQuestionCheck(postSurveyQuestionAdminRequest);

			if ((SurveyQuestionslist != null)) {
				start = System.nanoTime();
				// Update the Survey with QuestionID in SURVEY_QUESTIONS table
				surveyDAO.postSurveyQuestionAdmin(SurveyQuestionslist);
				end = System.nanoTime();
				elapsedTime = end - start;
				seconds = (double) elapsedTime / 1000000000.0;
				logger.info("===========Time Taken by SurveyQuestionslist ============> "
						+ String.valueOf(seconds));
			}
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		} catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}
		return surveyID;
	}

	public List<QuestionIdAdmin> populateSurveyQuestionsAdmin(
			PostSurveyQuestionAdminRequest postSurveyQuestionAdminRequest) {
		Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
				.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
		List<QuestionIdAdmin> surveyTransList = new ArrayList<QuestionIdAdmin>();
		QuestionIdAdmin questionIdAdmin = null;
		List<QuestionIdS> questionLists = postSurveyQuestionAdminRequest
				.getQuestionIds();
		for (QuestionIdS question : questionLists) {
			questionIdAdmin = new QuestionIdAdmin();
			questionIdAdmin.setQuestionIds(question.getQuestionIds());
			questionIdAdmin.setSurveyID(postSurveyQuestionAdminRequest
					.getSurveyID());
			questionIdAdmin.setInsertDate(currentTimestamp);
			surveyTransList.add(questionIdAdmin);
		}

		return surveyTransList;

	}

	public void SurveyQuestionCheck(
			PostSurveyQuestionAdminRequest postSurveyQuestionAdminRequest) {
		QuestionIdAdmin quests = new QuestionIdAdmin();
		QuestionIdAdmin IDs = new QuestionIdAdmin();
		// List<QuestionIdAdmin> LisstofIDs = new ArrayList<QuestionIdAdmin>();
		List<QuestionIdS> questionLists = postSurveyQuestionAdminRequest
				.getQuestionIds();
		for (QuestionIdS question : questionLists) {
			quests.setSurveyID(postSurveyQuestionAdminRequest.getSurveyID());
			quests.setQuestionIds(question.getQuestionIds());
			BigInteger QuestionIdifany = surveyDAO.getQuestionIdIfAny(quests);
			// BigInteger
			// SurveyIdifany=surveyDAO.getSurveyIdIfAny(postSurveyQuestionAdminRequest.getSurveyID());

			if (question.equals(QuestionIdifany)) {
				IDs.setSurveyID(postSurveyQuestionAdminRequest.getSurveyID());
				IDs.setQuestionIds(question.getQuestionIds());
				logger.info("QUESTION ID============> " + IDs.getQuestionIds());
				surveyDAO.deleteQuestionIDAdmin(IDs);

			}

		}

	}

	public BigInteger postQuestionAdmin(
			PostQuestionAdminRequest postQuestionAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		BigInteger questionID = BigInteger.ZERO;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(postQuestionAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postQuestionAdminRequest.getAnswerIds() == null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			long start = System.nanoTime();
			long end = System.nanoTime();
			long elapsedTime = end - start;
			double seconds = (double) elapsedTime / 1000000000.0;
			SequenceNumber sequenceNumber = new SequenceNumber(
					CDSConstants.QUES_ID_SEQ);
			/* Generate QuestionId */
			questionID = sequenceNumberDAO
					.getNextSequenceNumber(sequenceNumber);
			List<AdminQuestion> ListofQandA = new ArrayList<AdminQuestion>();
			AdminQuestion Questionobj = new AdminQuestion();
			postQuestionAdminRequest.setQuestionID(questionID);
			Questionobj = populateQuestion(postQuestionAdminRequest);
			/*
			 * Check for questionId and then insert the Question details in
			 * QUESTION table
			 */
			if (postQuestionAdminRequest.getQuestionID() != null
					&& Questionobj.getQuestionID() != null) {
				surveyDAO.postNewQuestion(Questionobj);
			}

			ListofQandA = populateQandA(postQuestionAdminRequest);

			if (!(ListofQandA.equals(null))) {
				start = System.nanoTime();
				// Update the Question and Answer ID in QUESTION_ANSWER_XREF
				// table
				surveyDAO.postQandAmapping(ListofQandA);
				end = System.nanoTime();
				elapsedTime = end - start;
				seconds = (double) elapsedTime / 1000000000.0;
				logger.info("===========Time Taken by postQuestionAdmin ============> "
						+ String.valueOf(seconds));
			}

		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		} catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}
		return questionID;

	}

	private AdminQuestion populateQuestion(
			PostQuestionAdminRequest postQuestionAdminRequest) {
		// TODO Auto-generated method stub
		Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
				.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
		AdminQuestion QuestionAdmin = new AdminQuestion();
		QuestionAdmin.setQuestionID(postQuestionAdminRequest.getQuestionID());
		QuestionAdmin.setQuestionType(postQuestionAdminRequest
				.getQuestionType());
		QuestionAdmin.setQuestionString(postQuestionAdminRequest
				.getQuestionString());
		QuestionAdmin.setInsertDate(currentTimestamp);
		QuestionAdmin.setDisplayType(postQuestionAdminRequest.getDisplayType());
		return QuestionAdmin;

	}

	private List<AdminQuestion> populateQandA(
			PostQuestionAdminRequest postQuestionAdminRequest) {
		// TODO Auto-generated method stub
		Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
				.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
		List<AdminQuestion> QandALists = new ArrayList<AdminQuestion>();
		AdminQuestion qandalist = null;
		List<answerIdsList> answerlistLists = postQuestionAdminRequest
				.getAnswerIds();

		for (answerIdsList answers : answerlistLists) {
			qandalist = new AdminQuestion();
			qandalist.setOptionId(answers.getOptionId());
			qandalist.setQuestionID(postQuestionAdminRequest.getQuestionID());
			qandalist.setInsertDate(currentTimestamp);
			qandalist.setDisplayOrder(postQuestionAdminRequest
					.getDisplayOrder());
			QandALists.add(qandalist);
		}

		return QandALists;

	}

	public BigInteger postAnswerAdmin(
			PostAnswerAdminRequest postAnswerAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		BigInteger answerID = BigInteger.ZERO;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(postAnswerAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postAnswerAdminRequest.getOptionString() == null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			SequenceNumber sequenceNumber = new SequenceNumber(
					CDSConstants.ANS_ID_SEQ);
			/* Generate QuestionId */
			answerID = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
			postAnswerAdminRequest.setAnswerID(answerID);
			AdminAnswer answer = populateAnswerDetails(postAnswerAdminRequest);
			if (!(answer.equals(null))) {
				surveyDAO.postAnswersAdmin(answer);
			}
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		} catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}
		return answerID;
	}

	private AdminAnswer populateAnswerDetails(
			PostAnswerAdminRequest postAnswerAdminRequest) {
		// TODO Auto-generated method stub
		Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
				.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
		AdminAnswer Answerfetch = new AdminAnswer();
		Answerfetch.setAnswerID(postAnswerAdminRequest.getAnswerID());
		Answerfetch.setAnswerOrder(postAnswerAdminRequest.getAnswerOrder());
		Answerfetch.setInsertDate(currentTimestamp);
		Answerfetch.setOptionString(postAnswerAdminRequest.getOptionString());
		return Answerfetch;
	}

	public void postReasonAdmin(PostReasonAdminRequest postReasonAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(postReasonAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postReasonAdminRequest.getReasonCode() == null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
					.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
			AdminReason setter = new AdminReason();
			setter.setInsertDate(currentTimestamp);
			setter.setReasonCode(postReasonAdminRequest.getReasonCode());
			setter.setReasonCodeDescirption(postReasonAdminRequest
					.getReasonCodeDescirption());
			surveyDAO.postReasonAdmin(setter);
			logger.info("===========Time Taken by AdminQuestionslist ============> "
					+ setter.getReasonCode()
					+ ">>"
					+ setter.getReasonCodeDescirption()
					+ ">>"
					+ setter.getInsertDate());
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(),rex);
		}

	}

	public BigInteger postSiteAdmin(PostSiteAdminRequest postSiteAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		Long l = new Long(123456789L);
		BigInteger SiteID = BigInteger.valueOf(l);
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(postSiteAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		if (postSiteAdminRequest.getSiteName() == null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
					.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
			long start = System.nanoTime();
			long end = System.nanoTime();
			long elapsedTime = end - start;
			double seconds = (double) elapsedTime / 1000000000.0;
			SequenceNumber sequenceNumber = new SequenceNumber(
					CDSConstants.SITE_ID_SEQ);
			AdminSite setter = new AdminSite();
			SiteID = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
			setter.setInsertDate(currentTimestamp);
			setter.setSiteId(SiteID);
			setter.setSiteName(postSiteAdminRequest.getSiteName());
			if (!(setter.getSiteName().equals(null))) {
				start = System.nanoTime();
				surveyDAO.postSite(setter);
				end = System.nanoTime();
				elapsedTime = end - start;
				seconds = (double) elapsedTime / 1000000000.0;
				logger.info("===========Time Taken by postSiteAdmin ============> "
						+ String.valueOf(seconds));
			}
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}
		return SiteID;
	}

	public GetAnswerAdminResponseList getAnswerAdmin(
			GetAnswerAdminRequest getAnswerAdminRequest)
			throws CustomAdminException {
		GetAnswerAdminResponseList ansresp = new GetAnswerAdminResponseList();
		List<GetAnswerAdminResponse> listofans = new ArrayList<GetAnswerAdminResponse>();

		try {
			listofans = surveyDAO.getAnswers(getAnswerAdminRequest);
			ansresp.setAnswer(listofans);
			logger.info("===========getAnswerAdmin ============> ");
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(),rex);
		}
		return ansresp;
	}

	public GetQuestionAdminResponseList getQuestionAdmin(
			GetQuestionAdminRequest getQuestionAdminRequest)
			throws CustomAdminException {
		GetQuestionAdminResponseList questresp = new GetQuestionAdminResponseList();
		List<GetQuestionAdminResponse> listofques = new ArrayList<GetQuestionAdminResponse>();

		// TODO Auto-generated method stub
		try {
			logger.info("Before If the value of QuestionID>>>>"
					+ getQuestionAdminRequest.getQuestionID());
			if (getQuestionAdminRequest.getQuestionID() != 0) {
				listofques = surveyDAO.getQuestion(getQuestionAdminRequest);// get
																			// one
																			// question
																			// only
				questresp.setQuestions(listofques);
				logger.info(">>>Inside QUESTION ");
			} else {

				listofques = surveyDAO.getQuestions(getQuestionAdminRequest);// get
																				// all
																				// questions
				questresp.setQuestions(listofques);
				logger.info(">>>Inside QUESTIONS ");
			}

			logger.info("===========Time Taken by AdminAnswerlist ============> ");
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(),rex);
		}
		return questresp;
	}

	public GetSiteAdminResponseList getSiteAdmin(
			GetSiteAdminRequest getSiteAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		GetSiteAdminResponseList sites = new GetSiteAdminResponseList();
		List<GetSiteAdminResponse> listofsites = new ArrayList<GetSiteAdminResponse>();
		try {
			listofsites = surveyDAO.getSites(getSiteAdminRequest);
			sites.setSites(listofsites);

			logger.info("===========Time Taken by AdminAnswerlist ============> ");
		} catch (DataIntegrityViolationException rex) {
			throw new CustomAdminException(rex.getMessage(), rex);
		}catch (DataAccessException rex) {
			throw new CustomAdminException(rex.getMessage(),rex);
		}
		return sites;
	}

	public int updateSiteAdmin(UpdateSiteAdminRequest updateSiteAdminRequest)
			throws CustomAdminException {
		int siteId = 0;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(updateSiteAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
					.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
			UpdateAdminSite setter = new UpdateAdminSite();
			setter.setUpdateDate(currentTimestamp);
			setter.setSiteId(updateSiteAdminRequest.getSiteId());
			setter.setSiteName(updateSiteAdminRequest.getSiteName());
			if (!(setter.equals(null))) {
				surveyDAO.updateSiteAdmin(setter);
			}

			siteId = setter.getSiteId().intValue();
			logger.info("===========Time Taken by AdminQuestionslist ============> "
					+ setter.getSiteId()
					+ ">>"
					+ setter.getSiteName()
					+ ">>"
					+ setter.getInsertDate());
		} catch (DataIntegrityViolationException rex) {
			// logger.info("PostSurvey :", rex.getMessage());
			throw new CustomAdminException(rex.getMessage(),
					ErrorCode.SITE_NOT_FOUND, rex);
		}catch (DataAccessException rex) {
			//logger.info("PostSurvey :", rex.getMessage());
			throw new CustomAdminException(ErrorCode.SITE_NOT_FOUND_DESC,
					ErrorCode.SITE_NOT_FOUND,rex);
		}
		return siteId;
	}

	public BigInteger updateAnswerAdmin(
			UpdateAnswerAdminRequest updateAnswerAdminRequest)
			throws CustomAdminException {
		BigInteger AnswerID = BigInteger.ZERO;
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(updateAnswerAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
					.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
			UpdateAdminAnswer setter = new UpdateAdminAnswer();
			setter.setAnswerID(updateAnswerAdminRequest.getOptionId());
			setter.setAnswerOrder(updateAnswerAdminRequest.getAnswerOrder());
			setter.setUpdateDate(currentTimestamp);
			setter.setOptionString(updateAnswerAdminRequest.getOptionString());
			AnswerID = updateAnswerAdminRequest.getOptionId();
			if (!(setter.getAnswerID().equals(null))) {
				surveyDAO.updateAnswerAdmin(setter);
			}
			logger.info("===========Time Taken by UpdateAdminAnswer ============> ");
		} catch (DataIntegrityViolationException rex) {
			// logger.info("PostSurvey :", rex.getMessage());
			throw new CustomAdminException(rex.getMessage(),
					ErrorCode.ANSWER_NOT_FOUND, rex);
		}catch (DataAccessException rex) {
			//logger.info("PostSurvey :", rex.getMessage());
			throw new CustomAdminException(ErrorCode.ANSWER_NOT_FOUND_DESC,
					ErrorCode.ANSWER_NOT_FOUND,rex);
		}
		return AnswerID;
	}

	@Transactional
	public BigInteger updateQuestionAdmin(
			UpdateQuestionAdminRequest updateQuestionAdminRequest)
			throws CustomAdminException {
		String validationErrors = null;
		BigInteger QuestionId = null;
		try {
			validationErrors = CDSOUtils.validate(updateQuestionAdminRequest,
					CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("postSurvey :", e);
			throw new WebApplicationException(e);
		}

		if (validationErrors != null) {
			throw new CustomAdminException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try {
			Timestamp currentTimestampz = Timestamp.valueOf(CDSOUtils
					.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
			QuestionId = updateQuestionAdminRequest.getQuestionID();
			List<UpdateAdminQuestion> ListofQandA = new ArrayList<UpdateAdminQuestion>();
			ListofQandA = updateQandA(updateQuestionAdminRequest);
			if (!(ListofQandA.equals(null))) {
				UpdateAdminQuestion questionupdate = new UpdateAdminQuestion();
				questionupdate.setQuestionID(updateQuestionAdminRequest
						.getQuestionID());
				questionupdate.setUpdateDate(currentTimestampz);
				questionupdate.setQuestionType(updateQuestionAdminRequest
						.getQuestionType());
				questionupdate.setQuestionString(updateQuestionAdminRequest
						.getQuestionString());
				questionupdate.setDisplayType(updateQuestionAdminRequest
						.getDisplayType());
				questionupdate.setDisplayOrder(updateQuestionAdminRequest.getDisplayOrder());
				surveyDAO.updateQuestiononly(questionupdate);
				surveyDAO.updateDisplayOrder(updateQuestionAdminRequest);
				
			}
			logger.info("===========Time Taken by updateQuestionAdminRequest ============> ");
		} catch (DataIntegrityViolationException rex) {
			// logger.info("PostSurvey :", rex.getMessage());
			throw new CustomAdminException(rex.getMessage(),
					ErrorCode.QUESTION_NOT_FOUND, rex);
		}catch (DataAccessException rex) {
			//logger.info("PostSurvey :", rex.getMessage());
			throw new CustomAdminException(ErrorCode.QUESTION_NOT_FOUND_DESC,
					ErrorCode.QUESTION_NOT_FOUND,rex);
		}
		return QuestionId;
	}

	private List<UpdateAdminQuestion> updateQandA(
			UpdateQuestionAdminRequest updateQuestionAdminRequest) {
		// TODO Auto-generated method stub
		Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils
				.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
		List<UpdateAdminQuestion> QandAlist = new ArrayList<UpdateAdminQuestion>();
		UpdateAdminQuestion question = null;
		List<answerIdsList> Answers = updateQuestionAdminRequest.getAnswerIds();
		for (answerIdsList answers : Answers) {
			question = new UpdateAdminQuestion();
			question.setQuestionID(updateQuestionAdminRequest.getQuestionID());
			question.setOptionId(answers.getOptionId());
			question.setUpdateDate(currentTimestamp);
			surveyDAO.updateQuestionAnswerXref(question);
			QandAlist.add(question);

		}
		return QandAlist;
	}
}
