package com.ko.cds.dao.test;

import java.math.BigInteger;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.pojo.activity.ActivityTrans;
import com.ko.cds.test.CDSTest;

@Component
//@Transactional
public class BunchBallActivityTest extends CDSTest{
	public static Logger logger=LoggerFactory.getLogger(BunchBallActivityTest.class);
	@Autowired
	private ActivityDAO activityDAO;
	@Autowired
	private SessionIdDAO sessionIdDAO;
	
	//Happy Path
	@Test
	@Rollback(true)
	public void bunchBallActivityTest(){
		ActivityTrans activityTrans=new ActivityTrans();
		
		BigInteger sessionId=sessionIdDAO.getSessionID("we23sgr1");
		activityTrans.setSessionId(sessionId);
		activityTrans.setMemberId(new BigInteger("4"));
		activityTrans.setName("Target");		
		activityTrans.setValue("Mcrbeta");
		
		activityDAO.bunchBallInfo(activityTrans);
		logger.info("New Record is Inserted");		
	}
	
	@Test
	@Rollback(true)
	public void bunchBallActivityTestWithoutMemberId(){
		ActivityTrans activityTrans=new ActivityTrans();
		
		BigInteger sessionId=sessionIdDAO.getSessionID("we23sgr1");
		activityTrans.setSessionId(sessionId);
		activityTrans.setName("Target");	
		//activityTrans.setMemberId(new BigInteger("null"));
		activityTrans.setValue("Mcrbeta");
		//activityTrans.setBevProdCd("Coke");
		if(activityTrans.getMemberId()!=null
			||activityTrans.getName()!=null||activityTrans.getValue()!=null){
				activityDAO.bunchBallInfo(activityTrans);
			}
		logger.info("New Record is Inserted");		
	}
	
	
	@Test(expected=DataIntegrityViolationException.class)
	public void bunchBallTestWithoutTagName(){
		ActivityTrans bunchBallActivity=new ActivityTrans();
		
		BigInteger sessionId=sessionIdDAO.getSessionID("we23sgr1");
		bunchBallActivity.setSessionId(sessionId);		
		bunchBallActivity.setMemberId(new BigInteger("1"));
		//bunchBallActivity.setActivityTagName("Target");		
		bunchBallActivity.setValue("Mcrbeta");
		
		activityDAO.bunchBallInfo(bunchBallActivity);
	}

}
