package com.ko.cds.sch.mailSend;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SendMailHelper
{
  private static final Logger logger = LoggerFactory.getLogger("SendMailHelper");
  
  public void execute()
  {
    ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
    JavaMailSender mailSender = (JavaMailSender)ctx.getBean("mailSender", MailSender.class);
    try
    {
      String[] mailingList = loadMailingList();
      if ((mailingList != null) && (!"".equals(mailingList)))
      {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mailingList);
        helper.setSubject("Error Log for Date : " + getSystemDate());
        helper.setText("Error File for date : " + getSystemDate());
        FileSystemResource file = new FileSystemResource("/data/mailErrorLog/errorLog.txt");
        if (file.exists())
        {
          helper.addAttachment(file.getFilename(), file);
          mailSender.send(message);
          File fileLog = new File("/data/mailErrorLog/errorLog.txt");
          fileLog.deleteOnExit();
        }
        else
        {
          logger.info("No Error found");
        }
      }
      else
      {
        logger.info("No Mailing list found");
      }
    }
    catch (Exception exx)
    {
      logger.error("Exception at mail send", exx);
    }
  }
  
  private String[] loadMailingList()
  {
    Properties prop = new Properties();
    String mailList = "";
    InputStream input = null;
    try
    {
      input = new FileInputStream("/opt/project/cds/maillingList.properties");
      prop.load(input);
      mailList = prop.getProperty("mailList");
    }
    catch (Exception e)
    {
      logger.error("Exception to get the mailing list", e);
    }
    return mailList.split(";");
  }
  
  private String getSystemDate()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    
    Date date = new Date();
    System.out.println(dateFormat.format(date));
    
    Calendar cal = Calendar.getInstance();
    return dateFormat.format(cal.getTime());
  }
}
