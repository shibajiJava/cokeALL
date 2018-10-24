package com.ko.cds.dao.config.text;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ko.cds.dao.config.ConfigDAO;
import com.ko.cds.pojo.config.ReasonCode;
import com.ko.cds.test.CDSTest;

public class ReasonCodeTest extends CDSTest{

	private static Logger log = LoggerFactory.getLogger(ReasonCodeTest.class);

	@Autowired
	private ConfigDAO configDAO;
	
	@Test
	public void getReasonCode(){
		List<ReasonCode> reasonCodesList = configDAO.getReasonCodes();
		log.info("Reason Codes "+ reasonCodesList);
		Assert.assertEquals(true, (reasonCodesList != null));
	}
}
