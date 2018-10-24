package com.ko.cds.service.member;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.app.services.domain.UserData;


@Component
@Path("/testrest")
public class TestingRest {

	@GET
	@Path("/resttest")
	public String savePayment() {
 
		Logger log = LoggerFactory.getLogger(TestingRest.class);
		
		log.info("==========================================================");
		//System.out.println("in service call");
		return "success";
 
	}
	
	@GET
	@Path("/resttest1")
	public UserData savePaymentJson() {
 
		UserData user =new UserData();
		Logger log = LoggerFactory.getLogger(TestingRest.class);
		
		log.info("==========================================================");
		//System.out.println("in service call");
		return user;
 
	}
	
}
