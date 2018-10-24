package com.ko.cds.sch.mailSend;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class SendMailErrorLog
  extends QuartzJobBean
  implements StatefulJob
{
  private static final Logger logger = LoggerFactory.getLogger("creditSACPointsTask");
  private static final Logger logger1 = LoggerFactory.getLogger("creditSACPointsFailures");
  
  protected void executeInternal(JobExecutionContext context)
    throws JobExecutionException
  {
    ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
    SendMailHelper sendMailHelper = (SendMailHelper)ctx.getBean("sendMailHelper", SendMailHelper.class);
    try
    {
      sendMailHelper.execute();
    }
    catch (Exception e)
    {
      logger1.error("Error While processing for this Run, Please check the CDA_CREDIT_PROCESS table for the errorneous record" + e.getMessage());
    }
  }
}
