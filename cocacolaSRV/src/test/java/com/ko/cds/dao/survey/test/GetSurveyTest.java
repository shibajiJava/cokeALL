package com.ko.cds.dao.survey.test;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.pojo.survey.Survey;
import com.ko.cds.request.survey.GetSurveyRequest;
import com.ko.cds.test.CDSTest;

/**
 * 
 * @author IBM
 * 
 */
@Component
public class GetSurveyTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(GetSurveyTest.class);
	@Autowired
	private SurveyDAO surveyDAO;

	@Test
	public void getSurvey_RequestWithMemberID() {

		GetSurveyRequest getSurveyReq = new GetSurveyRequest();
		getSurveyReq.setSurveyId(new BigInteger("1"));
		getSurveyReq.setMemberId(new BigInteger("1"));
		getSurveyReq.setSessionUUID("79");

		Survey response = surveyDAO.getSurveyInfo(getSurveyReq);
		log.info("getSurvey API response : " + response);
		Assert.assertEquals(new BigInteger("1"), response.getSurveyId());
	}

	@Test
	public void getSurvey_RequestWithoutMemberID() {

		GetSurveyRequest getSurveyReq = new GetSurveyRequest();
		getSurveyReq.setSurveyId(new BigInteger("1"));
		getSurveyReq.setSessionUUID("79");

		Survey response = surveyDAO.getSurveyInfo(getSurveyReq);
		log.info("getSurvey API response : " + response);
		Assert.assertEquals(new BigInteger("1"), response.getSurveyId());
	}

	@Test
	public void getSurvey_SurveyNotFOund() {

		GetSurveyRequest getSurveyReq = new GetSurveyRequest();
		getSurveyReq.setSurveyId(new BigInteger("5"));
		getSurveyReq.setMemberId(new BigInteger("1"));
		getSurveyReq.setSessionUUID("79");

		Survey response = surveyDAO.getSurveyInfo(getSurveyReq);
		log.info("getSurvey API response : " + response);
		Assert.assertEquals(null, response);
	}

	@Test
	public void getSurvey_SurveyExpired() {

		GetSurveyRequest getSurveyReq = new GetSurveyRequest();
		getSurveyReq.setSurveyId(new BigInteger("4"));
		getSurveyReq.setMemberId(new BigInteger("1"));
		getSurveyReq.setSessionUUID("79");

		Survey response = surveyDAO.getSurveyInfo(getSurveyReq);
		log.info("getSurvey API response : " + response);
		if (response.getExpirationDate() != null) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			Date expDate = null;
			String date = response.getExpirationDate();
			try {
				today = sdf1.parse(sdf1.format(today));
				expDate = sdf2.parse(date);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Assert.assertEquals(true, (expDate.compareTo(today) < 0));
		}
	}
}
