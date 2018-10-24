package com.ko.cds.jmxImpl;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.ko.cds.utils.CDSOUtils;


@ManagedResource(objectName = JmxConfigurationProperty.MBEAN_NAME, description = "Allows clients to set the Prop value at runtime")
public class JmxConfigurationProperty {
	
	
	
	public static File[] ovalValidationFile =  new File[10];
	public static String restHostServer ="";
	public static String janRainHostServer ="";
	public static Integer stringMaxLegth;
	public static String stringMaxLegthStr;
	public static String maxDateForHistory="999";
	public static boolean isJanrainSyncActive;
	
	
	/**
	 * value should be true / false 
	 */
	public static String isDevelopment="TRUE";
	

	public static String isProduction;
	public static String isUAT;
	public static Logger logger = LoggerFactory.getLogger(JmxConfigurationProperty.class);
	
	public static Map<String,String> propMap ;

	public static final String MBEAN_NAME = "bean:type=config,name=CDSOJmxConfigurationProperty";
	
	static {
		propMap = CDSOUtils.getConfigurtionPropertyUpdatable();
	}
	
	@ManagedOperation(description = "Returns the RestHostServer")
    @ManagedOperationParameters({ @ManagedOperationParameter(description = "The RestHost Server", name = "restHostServerGet") })
    public String getRestHostServer(String restHostServerGet) {

       // String restHostServer=null;
        return restHostServer;

    }
	
	@ManagedOperation(description = "Set RestHost Server")
    @ManagedOperationParameters({@ManagedOperationParameter(description = "The URL to which the restHostServer must be set", name = "restHostServer") })
    public void setLoggerLevel(String restHostServer) {
		if(restHostServer==null && propMap.get("restHostServer")!=null)
		{
			this.restHostServer=propMap.get("restHostServer");
		}
		else
		{
			this.restHostServer=restHostServer;
			CDSOUtils.updateConfigurtionProperty("restHostServer",restHostServer);
		}
		

    }

	@ManagedOperation(description = "Returns the String Max legth")
    @ManagedOperationParameters({@ManagedOperationParameter(description = "Max legth for all String type object", name = "setStringMaxLegth")})
	public void setStringMaxLegth(String stringMaxLegth) {
		logger.info("Setting the value : "+stringMaxLegth);
		if(stringMaxLegth==null && propMap.get("stringMaxLegth")!=null)
		{
			this.stringMaxLegthStr=propMap.get("stringMaxLegth");
		}
		else
		{
			this.stringMaxLegthStr=stringMaxLegth;
			CDSOUtils.updateConfigurtionProperty("stringMaxLegth",stringMaxLegth);
		}
		if(stringMaxLegthStr!=null && !"".equals(stringMaxLegthStr))
		{
			this.stringMaxLegth = Integer.valueOf(stringMaxLegthStr);
		}
		
	}
	
	public static String getJanRainHostServer() {
		return janRainHostServer;
	}

	@ManagedOperation(description = "Setting Janrain Server Host")
    @ManagedOperationParameters({@ManagedOperationParameter(description = "Setting Janrain Server Host", name = "setJanRainHostServer")})
	public void setJanRainHostServer(String janRainHostServer) {
		
		logger.info("Setting the value : "+stringMaxLegth);
		if(janRainHostServer==null && propMap.get("Janrain_url")!=null)
		{
			this.janRainHostServer=propMap.get("Janrain_url");
		}
		else
		{
			this.janRainHostServer=janRainHostServer;
			CDSOUtils.updateConfigurtionProperty("Janrain_url",janRainHostServer);
		}
		
		
		JmxConfigurationProperty.janRainHostServer = janRainHostServer;
	}
	
	
	public static String getIsDevelopment() {
		return isDevelopment;
	}
	@ManagedOperation(description = "Setting Environment")
    @ManagedOperationParameters({@ManagedOperationParameter(description = "Setting Environment", name = "setIsDevelopment")})
	public static void setIsDevelopment(String isDevelopment) {
		
		logger.info("Setting the value : "+isDevelopment);
		if(janRainHostServer==null && propMap.get("isDevelopment")!=null)
		{
			JmxConfigurationProperty.isDevelopment  =propMap.get("isDevelopment");
		}
		else
		{
			JmxConfigurationProperty.isDevelopment=isDevelopment;
			CDSOUtils.updateConfigurtionProperty("isDevelopment",isDevelopment);
		}
		
		JmxConfigurationProperty.isDevelopment = isDevelopment;
	}
	
	
	public static String getMaxDateForHistory() {
		return maxDateForHistory;
	}

	@ManagedOperation(description = "Setting MAX date for History API")
    @ManagedOperationParameters({@ManagedOperationParameter(description = "Setting MAX date for History API", name = "setMaxDateForHistory")})
	public static void setMaxDateForHistory(String maxDateForHistory) {
		
		logger.info("Setting the value : "+maxDateForHistory);
		if(maxDateForHistory==null && propMap.get("maxDateForHistory")!=null)
		{
			JmxConfigurationProperty.maxDateForHistory  =propMap.get("maxDateForHistory");
		}
		else
		{
			JmxConfigurationProperty.maxDateForHistory=maxDateForHistory;
			CDSOUtils.updateConfigurtionProperty("maxDateForHistory",maxDateForHistory);
		}
		
		
		JmxConfigurationProperty.maxDateForHistory = maxDateForHistory;
	}
	
	
	@ManagedOperation(description = "Setting Janrain Sync Active or not ")
    @ManagedOperationParameters({@ManagedOperationParameter(description = "Setting Janrain Sync Active or not", name = "setJanrainSyncActive")})
	public static void setJanrainSyncActive(String isJanrianSyncActive) {
		
		
		if(isJanrianSyncActive!=null && !"".equals(isJanrianSyncActive))
		{
			if("yes".equalsIgnoreCase(isJanrianSyncActive))
			{
				isJanrainSyncActive=true;
			}
			else
			{
				isJanrainSyncActive=false;
			}
		}
		
		
		if(isJanrianSyncActive==null && propMap.get("isJanrianSyncActive")!=null)
		{
			if(propMap.get("isJanrianSyncActive").equalsIgnoreCase("yes"))
			{
				JmxConfigurationProperty.isJanrainSyncActive=true;
			}
			else
			{
				JmxConfigurationProperty.isJanrainSyncActive=false;
			}
			
		}
		else
		{
			if("yes".equalsIgnoreCase(isJanrianSyncActive))
			{
				JmxConfigurationProperty.isJanrainSyncActive=true;
			}
			else
			{
				JmxConfigurationProperty.isJanrainSyncActive=false;
			}
			
			CDSOUtils.updateConfigurtionProperty("isJanrianSyncActive",isJanrianSyncActive);
		}
		
	}
	
	public static boolean getJanrainSyncActive() {
		propMap = CDSOUtils.getConfigurtionPropertyUpdatable();
		if(propMap.get("isJanrianSyncActive")!=null)
		{
			if(propMap.get("isJanrianSyncActive").equalsIgnoreCase("yes"))
			{
				JmxConfigurationProperty.isJanrainSyncActive=true;
			}
			else
			{
				JmxConfigurationProperty.isJanrainSyncActive=false;
			}
		}
		else
		{
			JmxConfigurationProperty.isJanrainSyncActive=true;
		}
		return JmxConfigurationProperty.isJanrainSyncActive;
	}
		
		
		

}
