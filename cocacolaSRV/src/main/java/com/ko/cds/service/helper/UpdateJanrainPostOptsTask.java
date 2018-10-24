package com.ko.cds.service.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.pojo.janrainIntegration.CommunicationOpt;
import com.ko.cds.pojo.janrainIntegration.Result;
import com.ko.cds.pojo.survey.OptPreference;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;

public class UpdateJanrainPostOptsTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(UpdateJanrainForOptsTask.class);
	
	ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	private MemberDAO memberDAO = (MemberDAO) ctx.getBean("memberDAO",MemberDAO.class);
	
	private OptsRequest optsRequest;
	private String janrainUuid;
	
	public UpdateJanrainPostOptsTask()
	{
		
	}
	
	
	public UpdateJanrainPostOptsTask(OptsRequest optsRequest,String janrainUuid)
	{
		this.optsRequest=optsRequest;
		this.janrainUuid=janrainUuid;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			updateJanrain(optsRequest,janrainUuid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Method to update opts into janrain.
	 * @param optsRequest
	 * @param janrainUuid
	 */
	private void updateJanrain(OptsRequest optsRequest, String janrainUuid) throws JSONException {
		// For janrain update - formatting json
		JSONArray array = new JSONArray();
		JSONObject obj = null;
		String janrainJson = "";
		
		OptsRequest optsReqNullAcptDt = new OptsRequest();
		optsReqNullAcptDt.setMemberId(optsRequest.getMemberId());
		optsReqNullAcptDt.setSessionUUID(optsRequest.getSessionUUID());
		List<OptPreference> optsPrefNullAcptdt = new ArrayList<OptPreference>();
		
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String environment = configProp.get(CDSConstants.ENVORONMENT_KEY);
		String janrainClientId  = 	configProp.get(CDSConstants.JANRAIN_CLIENT_ID_KEY+"_"+environment);
		try {
			for (OptPreference opt : optsRequest.getOpts()) {
				if(opt.getAcceptedDate() != null && opt.getAcceptedDate().length() > 0){
				obj = new JSONObject();
				obj.put(CDSConstants.JANRAIN_OPTS_ID, opt.getCommunicationTypeName());
				obj.put(CDSConstants.JANRAIN_OPTS_CLIENT_ID, janrainClientId);
				obj.put(CDSConstants.JANRAIN_OPTS_ACCEPTED, opt.getOptInIndicator());
				obj.put(CDSConstants.JANRAIN_OPTS_TYPE, opt.getType());
				obj.put(CDSConstants.JANRAIN_OPTS_ACCEPT_DATE, opt.getAcceptedDate());
				obj.put(CDSConstants.JANRAIN_OPTS_SCHEDULE_PREF, opt.getSchedule());
				obj.put(CDSConstants.JANRAIN_OPTS_FORMAT, opt.getFormat());
				array.put(obj);
				}else{
					optsPrefNullAcptdt.add(opt);
				}
			}
			optsReqNullAcptDt.setOpts(optsPrefNullAcptdt);
			obj = new JSONObject();
			obj.put(CDSConstants.JANRAIN_OPTS, array);
			janrainJson = obj.toString();
			logger.info("Janrain json for postOpts : " + janrainJson);
		} catch (JSONException e) {
			logger.error("JSONException : " + e.getMessage());
		}
		if(optsReqNullAcptDt.getOpts().size() > 0){
			replaceComOptsInJanrain(optsReqNullAcptDt,janrainClientId);
		}
		Map<String, String> janrainResponse = CDSOUtils.updateCDSOIDtoJanrain(
				janrainJson, janrainUuid);
		
		if (janrainResponse.get("StatusCode").equals("200")
				&& janrainResponse.get("ResponseSTR").equals(
						"{\"stat\":\"ok\"}")) {
			logger.info("janrain insert is successful");
		} else {
			logger.info("Status Code :" + janrainResponse.get("StatusCode")
					+ " Response " + janrainResponse.get("ResponseSTR")
					+ " Exception " + janrainResponse.get("Exception"));
		}

	}
	
	public void replaceComOptsInJanrain(OptsRequest optsReqNullAcptDt, String janrainClientId) throws JSONException{
		String janrainUUID=(memberDAO.getMemberUUIDbyMemID(optsReqNullAcptDt.getMemberId())).getUuid();
		Map<String,String> janrainReaponse = CDSOUtils.findOptsInfo(janrainUUID);
		Result allMemberInfo=null;
		if(janrainReaponse.get("StatusCode").equals("200") &&  janrainReaponse.get("ResponseSTR").contains("\"stat\":\"ok\""))
		{
		    ObjectMapper objectMapper = new ObjectMapper();
			try {
				allMemberInfo = objectMapper.readValue(janrainReaponse.get("ResponseSTR"), Result.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//logger.info("Error while parsing the Json Received from Janrain for after the find of the Communication Opts Info");
				logger.error("Error while parsing the Json Received from Janrain for after the find of the Communication Opts Info",e);
			}
			List<CommunicationOpt> commOpts = allMemberInfo.getResults().get(0).getCommunicationOpts();
			for (OptPreference communicationOpt : optsReqNullAcptDt.getOpts()) {
				for (CommunicationOpt janrainOpts : commOpts) {
					if(janrainOpts.getOptId().equals(communicationOpt.getCommunicationTypeName())){
						JSONObject obj = new JSONObject();
						String janrainJson = "";
						obj.put(CDSConstants.JANRAIN_OPTS_ID, communicationOpt.getCommunicationTypeName());
						obj.put(CDSConstants.JANRAIN_OPTS_CLIENT_ID, janrainClientId);
						obj.put(CDSConstants.JANRAIN_OPTS_ACCEPTED, communicationOpt.getOptInIndicator());
						obj.put(CDSConstants.JANRAIN_OPTS_TYPE, communicationOpt.getType());
						obj.put(CDSConstants.JANRAIN_OPTS_ACCEPT_DATE, communicationOpt.getAcceptedDate());
						obj.put(CDSConstants.JANRAIN_OPTS_SCHEDULE_PREF, communicationOpt.getSchedule());
						obj.put(CDSConstants.JANRAIN_OPTS_FORMAT, communicationOpt.getFormat());
						janrainJson = obj.toString();
						CDSOUtils.replaceCommOptsFromJanrain(janrainUUID,janrainOpts.getId(),janrainJson);
					}
				}
			}
			
		}
		else
		{
		    logger.info("Status Code :"+ janrainReaponse.get("StatusCode")+" Response "+janrainReaponse.get("ResponseSTR")+" Exception "+janrainReaponse.get("Exception"));
		}
		
	}
}
