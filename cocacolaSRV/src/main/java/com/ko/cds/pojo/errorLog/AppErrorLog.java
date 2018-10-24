package com.ko.cds.pojo.errorLog;

import java.io.Serializable;
import java.util.Date;

public class AppErrorLog
  implements Serializable
{
  private static final long serialVersionUID = 190909090L;
  private Date entryDate;
  private String errorCode;
  private String apiName;
  private String apiArgs;
  private String errorDesc;
  
  public Date getEntryDate()
  {
    return this.entryDate;
  }
  
  public void setEntryDate(Date entryDate)
  {
    this.entryDate = entryDate;
  }
  
  public String getErrorCode()
  {
    return this.errorCode;
  }
  
  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }
  
  public String getApiName()
  {
    return this.apiName;
  }
  
  public void setApiName(String apiName)
  {
    this.apiName = apiName;
  }
  
  public String getApiArgs()
  {
    return this.apiArgs;
  }
  
  public void setApiArgs(String apiArgs)
  {
    this.apiArgs = apiArgs;
  }
  
  public String getErrorDesc()
  {
    return this.errorDesc;
  }
  
  public void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  
  public String getData()
  {
    StringBuffer dataOut = new StringBuffer();
    dataOut.append(this.apiName + "|" + this.apiArgs + "|" + "|" + this.errorCode + "|" + this.errorDesc);
    return dataOut.toString();
  }
}
