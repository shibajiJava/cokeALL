package com.ko.cds.service.helper;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;

public class UpdateJanrainForOptsTask implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(UpdateJanrainForOptsTask.class);
	
	
	ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	private MemberDAO memberDAO = (MemberDAO) ctx.getBean("memberDAO",MemberDAO.class);
	
	//private MemberDAO memberDAO;
	
	private List<CommunicationOptIn> commOpt;
	private BigInteger toMemId;
	
	public UpdateJanrainForOptsTask()
	{
		
	}
	
	public UpdateJanrainForOptsTask(List<CommunicationOptIn> commOpt,BigInteger toMemId)
	{
		this.commOpt=commOpt;
		this.toMemId=toMemId;
	}
	
	
	@Override
	public void run() {

		updateJanrain(commOpt,toMemId);
		
	}

public void updateJanrain(List<CommunicationOptIn> commOpt, BigInteger toMemId) {
		
		String janrainUUID=((memberDAO.getMemberUUIDbyMemID(toMemId)).getUuid()).toString();
	    if(null != janrainUUID)
	    {
	    	// For janrain update - formatting json
			JSONArray array = new JSONArray();
			JSONObject obj = null;
			String janrainJson = "";
			Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
			String environment = configProp.get(CDSConstants.ENVORONMENT_KEY);
			String janrainClientId  = 	configProp.get(CDSConstants.JANRAIN_CLIENT_ID_KEY+"_"+environment);
			try {
				for (CommunicationOptIn opt : commOpt) {
					obj = new JSONObject();
					obj.put(CDSConstants.JANRAIN_OPTS_ID, opt.getCommunicationTypeName());
					obj.put(CDSConstants.JANRAIN_OPTS_CLIENT_ID, janrainClientId);
					obj.put(CDSConstants.JANRAIN_OPTS_ACCEPTED, opt.getOptedInIndicator());
					obj.put(CDSConstants.JANRAIN_OPTS_TYPE, opt.getType());
					obj.put(CDSConstants.JANRAIN_OPTS_SCHEDULE_PREF, opt.getSchedulePreference());
					obj.put(CDSConstants.JANRAIN_OPTS_FORMAT, opt.getFormat());
					Date date = opt.getAcceptedDate();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					String outputDate = format.format(date);
					obj.put(CDSConstants.JANRAIN_OPTS_ACCEPT_DATE, outputDate);
					array.put(obj);
				}

				obj = new JSONObject();
				obj.put(CDSConstants.JANRAIN_OPTS, array);
				janrainJson = obj.toString();
				logger.info("Janrain json for postOpts : " + janrainJson);
			} catch (JSONException e) {
				logger.info("JSONException : " + e.getMessage());
			}

			Map<String, String> janrainResponse = CDSOUtils.updateCDSOIDtoJanrain(
					janrainJson, janrainUUID);
			if (janrainResponse.get("StatusCode").equals("200")
					&& janrainResponse.get("ResponseSTR").equals(
							"{\"stat\":\"ok\"}")) {
				logger.info("janrain insert is successful");
			} else {
				logger.info("Status Code :" + janrainResponse.get("StatusCode")
						+ " Response " + janrainResponse.get("ResponseSTR")
						+ " Exception " + janrainResponse.get("Exception"));
			}
	    }else
		{
		    logger.info("UUID not set to janrain UPDATE");
		}
	}
	
}
