package com.ko.cds.dao.errorLog;

import com.ko.cds.pojo.errorLog.AppErrorLog;
import org.springframework.stereotype.Component;

@Component
public abstract interface ErrorLogDAO
{
  public abstract void insertAppErrLog(AppErrorLog paramAppErrorLog);
}
