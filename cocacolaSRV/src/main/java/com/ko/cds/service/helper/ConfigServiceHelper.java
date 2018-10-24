package com.ko.cds.service.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.config.ConfigDAO;
import com.ko.cds.pojo.config.ReasonCode;

@Component
public class ConfigServiceHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigServiceHelper.class);

	@Autowired
	private ConfigDAO configDAO;
	
	public List<ReasonCode> getReasonCodes(){
		 List<ReasonCode> resonCodesList = configDAO.getReasonCodes();
		 
		 if(resonCodesList != null && resonCodesList.size()>0){
			 logger.info("There are "+ resonCodesList.size() +" Reason Codes in the CDSO DB");
			 return resonCodesList;
		 }
		 
		 return null;
	}
	

}
