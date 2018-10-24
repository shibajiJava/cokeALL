package com.ko.cds.sch.creditpoints;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.ibm.app.services.appcontext.ApplicationContextProvider;

@Component
public class CreditSACPointsQuartzJob extends QuartzJobBean implements StatefulJob{

	private static final Logger logger = LoggerFactory
			.getLogger("creditSACPointsTask");
	
	private static final Logger logger1 = LoggerFactory
			.getLogger("creditSACPointsFailures");
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		CreditSACPointsHelper creditSACPointsHelper = (CreditSACPointsHelper) ctx.getBean("creditSACPointsHelper",CreditSACPointsHelper.class);
		long start = System.nanoTime();
		try {
			creditSACPointsHelper.execute();
		} catch (Exception e) {
			logger1.error("Error While processing for this Run, Please check the CDA_CREDIT_PROCESS table for the errorneous record" + e.getMessage());
		}
		long end = System.nanoTime();
        long elapsedTime = end - start;
        double seconds = (double) elapsedTime / 1000000000.0;
        logger.info("===========Time Taken by CreditSACPointsQuartzJob ============> "+String.valueOf(seconds));
		logger.info("Next Run time: ================= " + context.getNextFireTime()+"=====================");
		
		
	}

}
