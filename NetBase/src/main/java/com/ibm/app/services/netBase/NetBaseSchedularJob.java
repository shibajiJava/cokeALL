package com.ibm.app.services.netBase;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

import com.ibm.app.services.appcontext.ApplicationContextProvider;

public class NetBaseSchedularJob implements org.quartz.StatefulJob {

	Logger logger = LoggerFactory.getLogger(NetBaseSchedularJob.class);
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		NetBaseFile netBaseFile = (NetBaseFile) ctx.getBean("netBaseFile",NetBaseFile.class);
		
		try {
			logger.info("Start of netBase Process");
			long start = System.nanoTime();
			netBaseFile.processAPI();
			long end = System.nanoTime();
	        long elapsedTime = end - start;
	        double seconds = (double) elapsedTime / 1000000000.0;
	        logger.info("===========Time Taken by CreditSACPointsQuartzJob ============> "+String.valueOf(seconds));
			logger.info("Next Run time: ================= " + context.getNextFireTime()+"=====================");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("UnsupportedEncodingException exception in scheddular process "+e.getMessage());
		}
		catch(Exception exx)
		{
			logger.info("exception in scheddular process "+exx.getMessage());
		}
		
	}
	

}
