package com.ibm.ko.cds.service.janrainIntegration;

import java.util.Calendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ibm.ko.cds.jmxImpl.JmxConfigurationProperty;
import com.ibm.ko.cds.services.appcontext.ApplicationContextProvider;
@Component

public class JanrainJobSchedularJob implements org.quartz.StatefulJob {

	private static final Logger logger = LoggerFactory.getLogger("janrainforETL_schedular_Task");
	


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		if(JmxConfigurationProperty.getJanrainSyncActive())
		{
			logger.info("isJanrainSyncActive value is true ");
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
			
			logger.info("======================================== Starting of Janrain Schedular ===============================");
			logger.info("Start Time Stamp "+currentTimestamp);
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			JanrainService janrainService = (JanrainService) ctx.getBean("janrainService",JanrainService.class);
			long start = System.nanoTime();
			
			janrainService.updateMemberInfoFJanrain();
			
			long end = System.nanoTime();
	        long elapsedTime = end - start;
	        double seconds = (double) elapsedTime / 1000000000.0;
	        logger.info("===========Time Taken by janrain internal call and file write ============> "+String.valueOf(seconds));
		}
		else
		{
			logger.info("isJanrainSyncActive value is true ");
		}
		
		logger.info("Next Run time: ================= " + context.getNextFireTime()+"=====================");
		
	}
	
}
