package com.ko.cds.service.errorLog;

import com.ko.cds.pojo.errorLog.AppErrorLog;
import com.ko.cds.service.helper.ErrorLogServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogService
  implements IErrorLogService
{
  @Autowired
  private ErrorLogServiceHelper errorLogServiceHelper;
  
  public boolean inserErrorInDB(String[] appErrorLogSTR)
  {
    boolean returnValue = false;
    AppErrorLog appErrorLog = new AppErrorLog();
    try
    {
      appErrorLog.setApiName(appErrorLogSTR[0]);
      appErrorLog.setErrorDesc(appErrorLogSTR[1]);
      appErrorLog.setErrorCode(appErrorLogSTR[2]);
      appErrorLog.setApiArgs(appErrorLogSTR[3]);
      
      returnValue = this.errorLogServiceHelper.inserErrorInDB(appErrorLog);
    }
    catch (Throwable e) {}
    return returnValue;
  }
}
