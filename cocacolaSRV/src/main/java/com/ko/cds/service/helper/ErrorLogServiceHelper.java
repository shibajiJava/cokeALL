package com.ko.cds.service.helper;


import com.ko.cds.dao.errorLog.ErrorLogDAO;
import com.ko.cds.pojo.errorLog.AppErrorLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ErrorLogServiceHelper
{
  private static final Logger logger = LoggerFactory.getLogger(ErrorLogServiceHelper.class);
  @Autowired
  private ErrorLogDAO errorLogDAO;
  
  @Transactional
  public boolean inserErrorInDB(AppErrorLog appErrorLog)
  {
    boolean returnValue = false;
    try
    {
      if (appErrorLog != null) {
        this.errorLogDAO.insertAppErrLog(appErrorLog);
      }
      returnValue = true;
    }
    catch (Throwable rex)
    {
      logger.error("Error in insert in Error Log table");
    }
    return returnValue;
  }
}
