package com.ko.cds.sch.rollbackpoints;

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
public class DebitedPointsCreditQuartzJob extends QuartzJobBean implements StatefulJob{

	private static final Logger logger = LoggerFactory
			.getLogger("pointsRollbackSchedularTask");
	
	/*@Autowired
	DebitedPointCreditHelper debitedPointCreditHelper;
	
	
	*//**
	 * @return the debitedPointCreditHelper
	 *//*
	public DebitedPointCreditHelper getDebitedPointCreditHelper() {
		return debitedPointCreditHelper;
	}


	*//**
	 * @param debitedPointCreditHelper the debitedPointCreditHelper to set
	 *//*
	public void setDebitedPointCreditHelper(
			DebitedPointCreditHelper debitedPointCreditHelper) {
		this.debitedPointCreditHelper = debitedPointCreditHelper;
	}*/

	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		DebitedPointCreditHelper debitedPointCreditHelper = (DebitedPointCreditHelper) ctx.getBean("debitedPointCreditHelper",DebitedPointCreditHelper.class);
		long start = System.nanoTime();
		debitedPointCreditHelper.execute();
		long end = System.nanoTime();
        long elapsedTime = end - start;
        double seconds = (double) elapsedTime / 1000000000.0;
        logger.info("===========Time Taken by DebitedPointsCreditQuartzJob ============> "+String.valueOf(seconds));
        //System.out.println("===========Time Taken by DebitedPointsCreditQuartzJob ============> "+String.valueOf(seconds));
		logger.info("Next Run time: ================= " + context.getNextFireTime()+"=====================");
	}


	
}
