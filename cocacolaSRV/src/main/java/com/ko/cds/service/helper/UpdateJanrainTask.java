package com.ko.cds.service.helper;

import java.math.BigInteger;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;

public class UpdateJanrainTask implements Runnable {
	
	private static final String UPDATE_CDSOID="Update_cdsoID";
	private static final String UPDATE_SMS="Update_SMSNumber";
	private static final String UPDATE_EXTERNALID="Update_ExternalID";
	private static final String UPDATE_OPTS="Update_COMM_OPTS";
	private static final Logger logger = LoggerFactory.getLogger(UpdateJanrainTask.class);

	ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	private MemberDAO memberDAO = (MemberDAO) ctx.getBean("memberDAO",MemberDAO.class);
	//private MemberDAO memberDAO;
	
	private Object memberInfo;
	private String callerMethod;
	private BigInteger toMemId;
	
	
	public UpdateJanrainTask()
	{
		
	}
	
	public UpdateJanrainTask(Object memberInfo,String callerMethod,BigInteger toMemId)
	{
		this.memberInfo=memberInfo;
		this.callerMethod=callerMethod;
		this.toMemId=toMemId;
	}
	
	@Override
	public void run() {
		updateJanrain(memberInfo,callerMethod,toMemId);
		
	}
	
	
	public boolean updateJanrain(Object memberInfo, String callerMethod,BigInteger toMemId) 
	{
		@SuppressWarnings("unused")
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	    boolean uuidFound=true;
	    String janrainUUID="";
	    String updateSTR="";
		boolean returnValue=true;
		MemberInfo memberDetails = null;
		SMSNumber mergrSmsNumber=null;
		TagObject memberIdentifier = null;
		CommunicationOptIn commOpts = null;
		try{
    		if(callerMethod.equals(UPDATE_CDSOID))
    		{
    			memberDetails = (MemberInfo)memberInfo;
    			if(null==memberDetails.getUuid())
    			{
    			    uuidFound=false;
    			}
    			else
    			{
    			    janrainUUID=memberDetails.getUuid();
    			    updateSTR=getAttributeValueCDSO(memberDetails);
    			}
    			
    		}
    		
    		if(callerMethod.equals(UPDATE_SMS))
    		{
    		    mergrSmsNumber = (SMSNumber)memberInfo;
    		    janrainUUID=(memberDAO.getMemberUUIDbyMemID(toMemId)).getUuid();
    		    if(null == janrainUUID)
    		    {
    		        uuidFound=false;
    		    }
    		    else
    		    {
    		        updateSTR=getAttributeValueSMS(mergrSmsNumber);
    		    }
    			
    		}else if(callerMethod.equals(UPDATE_EXTERNALID)){
    			memberIdentifier = (TagObject)memberInfo;
    		    janrainUUID=(memberDAO.getMemberUUIDbyMemID(toMemId)).getUuid();
    		    if(null == janrainUUID)
    		    {
    		        uuidFound=false;
    		    }
    		    else
    		    {
    		        updateSTR=getAttributeValueExternalId(memberIdentifier);
    		    }
    			
    		}else if(callerMethod.equals(UPDATE_OPTS)){
    			commOpts = (CommunicationOptIn)memberInfo;
    		    janrainUUID=memberDAO.getMemberUUIDbyMemID(toMemId).getUuid();
    		    if(null == janrainUUID)
    		    {
    		        uuidFound=false;
    		    }
    		    else
    		    {
    		        updateSTR=getAttributeValueLegalAcceptance(commOpts);
    		    }
    			
    		}
    		
    		
    		if(uuidFound)
    		{
        		Map<String,String> janrainReaponse = CDSOUtils.updateCDSOIDtoJanrain(updateSTR,janrainUUID);
        		if(janrainReaponse.get("StatusCode").equals("200") &&  janrainReaponse.get("ResponseSTR").equals("{\"stat\":\"ok\"}"))
        		{
        		    returnValue=true;
        		}
        		else
        		{
        		    logger.info("Status Code :"+ janrainReaponse.get("StatusCode")+" Response "+janrainReaponse.get("ResponseSTR")+" Exception "+janrainReaponse.get("Exception"));
        		    returnValue = false;
        		}
    		}
    		else
    		{
    		    logger.info("UUID not set to janrain UPDATE");
    		}
		}
		catch(Exception exx)
		{
		    logger.error("Exception found in Janrain Update",exx);
		    returnValue=false;
		}
		return returnValue;
	}
	
	
private String getAttributeValueCDSO(MemberInfo memberInfo) {
		
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("{\"externalIds\": [{\"useForLogin\": false,\"value\":\""+memberInfo.getMemberId()+"\",\"type\": \"CDS\",\"lastUpdatedTime\": \""+CDSOUtils.getCurrentUTCTimestamp()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	
	private String getAttributeValueSMS(SMSNumber memberSMSInfo) {
		
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("{\"phoneNumbers\": [{\"countryCode\":\"001\",\"type\":\""+memberSMSInfo.getSmsType()+"\",\"value\":\""+memberSMSInfo.getSmsNumber()+"\",\"dateVerified\": \""+CDSOUtils.getCurrentUTCTimestamp()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	private String getAttributeValueExternalId(TagObject memberIdentifier) {
		
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("{\"externalIds\": [{\"useForLogin\": false,\"value\":\""+memberIdentifier.getValue()+"\",\"type\": \""+memberIdentifier.getName()+"\",\"lastUpdatedTime\": \""+CDSOUtils.getCurrentUTCTimestamp()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	private String getAttributeValueLegalAcceptance(CommunicationOptIn commOpts){
		StringBuilder returnStr = new StringBuilder();
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String environment = configProp.get(CDSConstants.ENVORONMENT_KEY);
		String janrainClientId  = 	configProp.get(CDSConstants.JANRAIN_CLIENT_ID_KEY+"_"+environment);
		returnStr.append("{\"communicationOpts\": [{\"optId\":\""+commOpts.getCommunicationTypeName()+"\",\"clientId\":\""+janrainClientId+"\",\"accepted\": \""+commOpts.getOptedInIndicator()+"\",\"type\": \""+commOpts.getType()+"\",\"dateAccepted\":\""+commOpts.getAcceptedDate()+"\",\"schedulePreference\":\""+commOpts.getSchedulePreference()+"\",\"format\":\""+commOpts.getFormat()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}

}
