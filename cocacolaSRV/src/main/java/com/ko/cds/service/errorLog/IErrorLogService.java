package com.ko.cds.service.errorLog;


import org.springframework.stereotype.Component;

@Component
public abstract interface IErrorLogService
{
  public abstract boolean inserErrorInDB(String[] paramArrayOfString);
}
