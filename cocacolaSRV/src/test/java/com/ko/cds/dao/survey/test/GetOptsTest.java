package com.ko.cds.dao.survey.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.response.survey.GetOptsResponse;
import com.ko.cds.test.CDSTest;

/**
 * Junits for GetOpts api.
 * 
 * @author IBM
 * 
 */
@Component
public class GetOptsTest extends CDSTest {

	private static Logger log = LoggerFactory.getLogger(GetSurveyTest.class);
	@Autowired
	private SurveyDAO surveyDAO;

	@Test
	public void getOpts_SingleOpts() {
		GetOptsResponse response = null;
		OptsRequest optsReq = new OptsRequest();
		optsReq.setMemberId(new BigInteger("3"));
		optsReq.setSessionUUID("ang0-4344-dddd-4444r");

		response = surveyDAO.getOpts(optsReq);

		log.info("GetOpts response : " + response.getOpts().size());
		Assert.assertEquals(1, response.getOpts().size());
	}

	@Test
	public void getOpts_MultipleOpts() {
		GetOptsResponse response = null;
		OptsRequest optsReq = new OptsRequest();
		optsReq.setMemberId(new BigInteger("1"));
		optsReq.setSessionUUID("ang0-4344-dddd-4444r");

		response = surveyDAO.getOpts(optsReq);

		log.info("GetOpts response : " + response.getOpts().size());
		Assert.assertEquals(3, response.getOpts().size());
	}

	@Test
	public void getOpts_QuestionNotFound() {
		GetOptsResponse response = null;
		OptsRequest optsReq = new OptsRequest();
		optsReq.setMemberId(new BigInteger("6"));
		optsReq.setSessionUUID("ere0-4344-dddd-4444r");

		response = surveyDAO.getOpts(optsReq);

		log.info("GetOpts response : " + response);
		Assert.assertEquals(null, response);
	}
}
