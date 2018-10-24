package com.ko.cds.service.janrainIntegration;

import java.util.Calendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ko.cds.jmxImpl.JmxConfigurationProperty;
@Component

public class JanrainJobSchedularJob implements org.quartz.StatefulJob {

	private static final Logger logger = LoggerFactory.getLogger("schedularTask");
	


	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		//System.out.println("==================================================================================================");
		//System.out.println("================================================================="+JmxConfigurationProperty.getJanrainSyncActive());
		//System.out.println("==================================================================================================");
		JmxConfigurationProperty.isJanrainSyncActive=false;
		if(JmxConfigurationProperty.getJanrainSyncActive())
		{
			/*logger.info("isJanrainSyncActive value is true ");
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
			
			logger.info("======================================== Starting of Janrain Schedular ===============================");
			logger.info("Start Time Stamp "+currentTimestamp);
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			JanrainService janrainService = (JanrainService) ctx.getBean("janrainService",JanrainService.class);
			janrainService.updateMemberInfoFJanrain();*/
		}
		else
		{
			logger.info("isJanrainSyncActive value is true ");
		}
		
	}

	
	
	
}
