package com.ko.cds.dao.survey.test;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.pojo.survey.OptPreference;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.test.CDSTest;

/**
 * Junit for PostOpts API - DAO
 * 
 * @author IBM
 * 
 */
@Component
public class PostOptsTest extends CDSTest {
	private static Logger log = LoggerFactory.getLogger(PostOptsTest.class);
	@Autowired
	private SurveyDAO surveyDAO;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
	
	@Test
	@Rollback(true)
	public void postOpts_HappyPath() {

		List<OptPreference> optsList = new ArrayList<OptPreference>();

		OptsRequest postOptReq = new OptsRequest();
		postOptReq.setMemberId(new BigInteger("1"));
		postOptReq.setSessionUUID("ang0-4344-dddd-4444r");
		OptPreference opts = new OptPreference();
		opts.setCommunicationTypeName("TNC");
		opts.setAcceptedDate(new Timestamp(new Date().getTime()).toString());
		opts.setType("");
		opts.setSchedule("");
		opts.setFormat("checkbox");
		opts.setOptInIndicator("true");
		optsList.add(opts);
		postOptReq.setOpts(optsList);
		int response = surveyDAO.postOpts(postOptReq);
		log.info("postOpts API response : " + response);
		Assert.assertEquals(1, response);
	}

	@Test
	@Rollback(true)
	public void postOpts_MultipleOpts() {

		List<OptPreference> optsList = new ArrayList<OptPreference>();

		OptsRequest postOptReq = new OptsRequest();
		postOptReq.setMemberId(new BigInteger("1"));
		postOptReq.setSessionUUID("ang0-4344-dddd-4444r");
		// opts1
		OptPreference opts1 = new OptPreference();
		opts1.setCommunicationTypeName("TNC");
		opts1.setAcceptedDate(new Timestamp(new Date().getTime()).toString());
		opts1.setType("");
		opts1.setSchedule("");
		opts1.setFormat("checkbox");
		opts1.setOptInIndicator("true");
		optsList.add(opts1);
		// opts2
		OptPreference opts2 = new OptPreference();
		opts2.setCommunicationTypeName("SMS");
		opts2.setAcceptedDate(new Timestamp(new Date().getTime()).toString());
		opts2.setType("");
		opts2.setSchedule("");
		opts2.setFormat("checkbox");
		opts2.setOptInIndicator("true");
		optsList.add(opts2);
//		postOptReq.setOpts(optsList);
		// opts3
		OptPreference opts3 = new OptPreference();
		opts3.setCommunicationTypeName("Email");
		opts3.setAcceptedDate(new Timestamp(new Date().getTime()).toString());
		opts3.setType("");
		opts3.setSchedule("");
		opts3.setFormat("checkbox");
		opts3.setOptInIndicator("true");
		optsList.add(opts3);
		postOptReq.setOpts(optsList);
		int response = surveyDAO.postOpts(postOptReq);
		log.info("postOpts API response : " + response);		
		Assert.assertEquals(3, response);
	}

	@Test(expected = DataIntegrityViolationException.class)
	@Rollback(true)
	public void postOpts_UnavailableMemberID() {
		List<OptPreference> optsList = new ArrayList<OptPreference>();

		OptsRequest postOptReq = new OptsRequest();
		postOptReq.setMemberId(new BigInteger("10"));
		postOptReq.setSessionUUID("ang0-4344-dddd-4444r");
		OptPreference opts = new OptPreference();
		opts.setCommunicationTypeName("TNC");
		opts.setAcceptedDate(new Timestamp(new Date().getTime()).toString());
		opts.setType("");
		opts.setSchedule("");
		opts.setFormat("checkbox");
		opts.setOptInIndicator("true");
		optsList.add(opts);
		postOptReq.setOpts(optsList);
		int response = surveyDAO.postOpts(postOptReq);
		log.info("postOpts API response : " + response);
		Assert.assertEquals(0, response);
	}
	
//	@Test(expected = DuplicateKeyException.class)
//	@Rollback(true)
//	public void postOpts_DuplicatekeyException() {
//		List<OptPreference> optsList = new ArrayList<OptPreference>();		
//
//		OptsRequest postOptReq = new OptsRequest();
//		postOptReq.setMemberId(new BigInteger("3"));
//		postOptReq.setSessionUUID("ang0-4344-dddd-4444r");
//		OptPreference opts = new OptPreference();
//		opts.setCommunicationTypeName("TNC");
//		try {
//			opts.setAcceptedDate(new Timestamp(df.parse("2014-06-17 17:37:34:000").getTime()).toString());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		opts.setType("");
//		opts.setSchedule("");
//		opts.setFormat("checkbox");
//		opts.setOptInIndicator("true");
//		optsList.add(opts);
//		postOptReq.setOpts(optsList);
//		int response = surveyDAO.postOpts(postOptReq);
//		log.info("postOpts API response : " + response);
//		Assert.assertEquals(0, response);
//	}
}
