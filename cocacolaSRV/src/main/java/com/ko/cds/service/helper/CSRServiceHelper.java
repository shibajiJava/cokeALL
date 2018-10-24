package com.ko.cds.service.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.dao.codeRedemption.CodeRedemptionDAO;
import com.ko.cds.dao.csr.CSRTicketDAO;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.csr.ActivitySearch;
import com.ko.cds.pojo.csr.CSRMemberInfo;
import com.ko.cds.pojo.csr.CSRTicket;
import com.ko.cds.pojo.csr.CodeRedemptionHistory;
import com.ko.cds.pojo.csr.SessionSearch;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.request.csr.ActivitySearchRequest;
import com.ko.cds.request.csr.CSRAdvanceSearchRequest;
import com.ko.cds.request.csr.CSRTicketGetRequest;
import com.ko.cds.request.csr.CSRTicketPostRequest;
import com.ko.cds.request.csr.CodeRedemptionHistoryRequest;
import com.ko.cds.request.csr.DeleteMemberRequest;
import com.ko.cds.request.csr.SessionSearchRequest;
import com.ko.cds.response.csr.ActivitySearchResponse;
import com.ko.cds.response.csr.CodeRedemptionHistoryResponse;
import com.ko.cds.response.csr.SessionSearchResponse;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.CSRConstants;
import com.ko.cds.utils.ErrorCode;

@Component
public class CSRServiceHelper {
	private static final Logger logger = LoggerFactory.getLogger(CSRServiceHelper.class);
	@Autowired
	private MemberDAO memberDAO;
	
	@Autowired
	private ICacheService cacheService;

	@Autowired
	private CodeRedemptionDAO codeRedmptionDAO;
	
	@Autowired
	private ActivityDAO activityDAO;
	
	@Autowired
	private CSRTicketDAO cSRTicketDAO;

	public List<CSRMemberInfo> csrAdvanceSearch(
			CSRAdvanceSearchRequest advanceSearchRequest)
			throws BadRequestException {
		String validationErrors = null;
		try {
			validationErrors=CDSOUtils.validate(advanceSearchRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("csrAdvanceSearch :",e);
			throw new WebApplicationException(e);
		}
		if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		  }
		
		// Check if no field selected for search
		if(!isFieldSelectedForSearch(advanceSearchRequest)){
			throw new BadRequestException(ErrorCode.FIELD_NOT_SELECTED_FOR_SEARCH,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		//check for Param combimation
		searchFieldCombinationValidation(advanceSearchRequest);
		
		// check for too few parameter
		validateWildCard(advanceSearchRequest);
		advanceSearchRequestPreProcessing(advanceSearchRequest);
		List<CSRMemberInfo> csrMemberList=memberDAO.csrAdvanceSearch(advanceSearchRequest);
		if(csrMemberList == null || csrMemberList.size() == 0 ){
			 throw new BadRequestException(ErrorCode.ADVANCE_SEARCH_MEMBER_NOT_FOUND_DESC,ErrorCode.MEMBER_NOT_FOUND);
		}
		return csrMemberList;
	}

	public CodeRedemptionHistoryResponse getCodeRedemptionHistory(CodeRedemptionHistoryRequest request) throws BadRequestException{
		
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(request, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("getCodeRedemptionHistory : "+e);
			throw new WebApplicationException(e);
		}
		
	    if(validationErrors != null){
	    	throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
	    }	
		
		if((request.getRedemptionHistoryStartDate() == null && request.getRedemptionHistoryEndDate() == null
				&&  request.getMemberId() == null &&  request.getLotId() == null &&  request.getCampaigned() == null &&  request.getSearchClientTransactionId() == null
				&&  request.getSessionUUID() == null)){
			logger.error(CSRConstants.NO_FIELD_SELECTION_ERROR);
			throw new BadRequestException(CSRConstants.NO_FIELD_SELECTION_ERROR,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		if(request.getRedemptionHistoryStartDate() != null && request.getRedemptionHistoryEndDate() != null){
			validateDate(request.getRedemptionHistoryStartDate(), request.getRedemptionHistoryEndDate());
		}
		
		List<CodeRedemptionHistory> crObjList = codeRedmptionDAO.getCodeRedemptionInfo(request);
		CodeRedemptionHistoryResponse resp = new CodeRedemptionHistoryResponse();
		if(crObjList != null && crObjList.size()>0){

			for (CodeRedemptionHistory codeRedemptionObject : crObjList) {
				if(codeRedemptionObject.getClientTransactionIdPoints() == null){
					codeRedemptionObject.setAward(CSRConstants.CHALLENGE_CODE);
				}else{
					codeRedemptionObject.setAward(CSRConstants.POINT_REDEMPTION);
				}
			}
			
			if(crObjList.size() >= CSRConstants.MAX_RECORDS_FOR_API){
				resp.setMaximumRecords(CSRConstants.MAX_RECORDS);
				resp.setCodeRedemptions(crObjList.subList(0, CSRConstants.MAX_RECORDS_FOR_API));
				return resp;
			}
			
		}else{
			throw new BadRequestException(ErrorCode.NO_HISTORY_FOUND,ErrorCode.NO_HISTORY_FOUND);
		}
		//valid Data
		resp.setMaximumRecords(null);
		resp.setCodeRedemptions(crObjList);
		return resp;
	}
	
	
	
	public Response deleteMemberfromCdso(DeleteMemberRequest request) throws BadRequestException{
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(request, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("deleteMemberfromCdso : "+e);
			throw new WebApplicationException(e);
		}
		
	    if(validationErrors != null){
	    	throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
	    }	
	    //get the SessionID,
	    if(request.getSessionUUID() != null){
			BigInteger sessionId=cacheService.getSessionID(request.getSessionUUID());
			request.setSessionId(sessionId);
		}
		//update the Member Status in CDS
	    int i = setMemberStatusDelete(request.getMemberId());
	    if(i==0){
	    	throw new BadRequestException(CSRConstants.MEMBER_NOT_EXISTS,ErrorCode.MEMBER_NOT_FOUND);
	    }
	    //update the Janrain Table with Deletion Reason.
	    int rowcount = memberDAO.getMemberStatusFromDeleteTable(request.getMemberId());
	    int j = 0;
	    if(rowcount == 0){
	    	 j = memberDAO.insertMemberDeletionReason(request);
	    }else{
	    	 throw new BadRequestException(CSRConstants.MEMBER_SCHEDULE_FOR_DELETE,ErrorCode.MEMBER_NOT_FOUND);
	    }
	    
	    if(j>0 && request.getJanrainDelete().equals("Y")){
	    	//call Janrain Delete
	    	final MemberInfo memberInfo = memberDAO.getMemberUUIDbyMemID(request.getMemberId());
	    	if ((memberInfo != null) && (memberInfo.getUuid() != null) && !(memberInfo.getUuid().isEmpty())) {
			logger.info("Memeber Id "+request.getMemberId()+" with UUID : "+memberInfo.getUuid()+" is set for Jainrain DELETE");	
			ExecutorService es = Executors.newFixedThreadPool(1);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final Future future = es.submit(new Callable() {
	                    public Object call() throws Exception {
	                        janrainMemberDelete(memberInfo.getUuid());
	                        return null;
	                    }
	                });
			es.shutdown();
	    	} else {
				logger.info("UUID not set to janrain DELETE, may be its a lite account ");
			}
	    	
	    }else{
	    	logger.info("Janrain Delete not Called");
	    }
	    
	    return CDSOUtils.createOKResponse(null);
	}
		
	public void createCSRTicketHistory(CSRTicketPostRequest cSRTicketPostRequest) throws BadRequestException{
		String validationErrors = null;
		try {
			validationErrors=CDSOUtils.validate(cSRTicketPostRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("createCSRTicketHistory :",e);
			throw new WebApplicationException(e);
		}
		if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		  }
		if(cSRTicketPostRequest.getSessionUUID() != null){
			BigInteger sessionId = cacheService.getSessionID(cSRTicketPostRequest.getSessionUUID());
			cSRTicketPostRequest.setSessionId(sessionId);
	   	}
		cSRTicketDAO.insertCSRTicket(cSRTicketPostRequest);
		
	}
	
	private int setMemberStatusDelete(BigInteger memberId){
		 int i = memberDAO.updateMemberStatus(CDSConstants.DEACTIVE_STATUS_CODE,memberId);
		 memberDAO.updateMemberSMSStatus(CDSConstants.DEACTIVE_STATUS_CODE,memberId);
		 memberDAO.updateMemberIdentifierStatus(CDSConstants.DEACTIVE_STATUS_CODE,memberId);
		return i;
	}
	public List<CSRTicket>  getCSRTicketHistory(CSRTicketGetRequest cSRTicketGetRequest) throws BadRequestException{
		String validationErrors = null;
		List<CSRTicket> csrTicketList = null;
		try {
			validationErrors=CDSOUtils.validate(cSRTicketGetRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("getCSRTicketHistory :",e);
			throw new WebApplicationException(e);
		}
		if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		  }
		/*
		if(cSRTicketGetRequest.getSessionUUID() != null){
			BigInteger sessionId = cacheService.getSessionID(cSRTicketGetRequest.getSessionUUID());
			cSRTicketGetRequest.setSessionId(sessionId);
	   	}*/
		csrTicketList = cSRTicketDAO.getCSRHistory(cSRTicketGetRequest);
		
		return csrTicketList;
	}	
	private boolean janrainMemberDelete(String memberUUID){
		boolean returnValue;
		Map<String,String> janrainReaponse = CDSOUtils.deleteCDSOMemberINJanrain(memberUUID);
		if(janrainReaponse.get("StatusCode").equals("200") &&  janrainReaponse.get("ResponseSTR").equals("{\"stat\":\"ok\"}"))
		{
		    returnValue=true;
		}
		else
		{
		    logger.info("Status Code :"+ janrainReaponse.get("StatusCode")+" Response "+janrainReaponse.get("ResponseSTR")+" Exception "+janrainReaponse.get("Exception"));
		    returnValue = false;
		}
		return returnValue;
	}
	private boolean isFieldSelectedForSearch(CSRAdvanceSearchRequest advanceSearchRequest){
		boolean retValue = false;
		if(advanceSearchRequest.getAddress() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getBirthDate() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getCity() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getState()!= null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getCreatedDate() != null){
			retValue = true;
			return retValue;
		}	
		if(advanceSearchRequest.getDisplayName() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getEmail() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getFirstName() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getLastName() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getHouseHoldId() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getJanrainAccountStatus() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getJanrainUUID() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getMemberId() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getMemberIdentifier() != null && advanceSearchRequest.getMemberIdentifier().getName() != null && advanceSearchRequest.getMemberIdentifier().getValue() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getMobile() != null){
			retValue = true;
			return retValue;
		}
		/*
		if(advanceSearchRequest.getSessionUUID() != null){
			retValue = true;
			return retValue;
		}*/
		if(advanceSearchRequest.getSocialDomain() != null && advanceSearchRequest.getSocialDomain().getName() !=null && advanceSearchRequest.getSocialDomain().getValue() != null){
			retValue = true;
			return retValue;
		}
		if(advanceSearchRequest.getZip() != null){
			retValue = true;
			return retValue;
		}
		return retValue;
	}
	private void advanceSearchRequestPreProcessing(CSRAdvanceSearchRequest advanceSearchRequest){
		 // replace * for wild card search
		   if(advanceSearchRequest.getFirstName() != null){
			  String firstName = advanceSearchRequest.getFirstName();
			  firstName=firstName.replace('*', '%');
			  firstName=firstName.toUpperCase();
			  advanceSearchRequest.setFirstName(firstName);
		   }
		   if(advanceSearchRequest.getLastName() != null ){
			   String lastName = advanceSearchRequest.getLastName();
			   lastName = lastName.replace('*', '%');
			   lastName=lastName.toUpperCase();
			   advanceSearchRequest.setLastName(lastName);
		   }
		   if(advanceSearchRequest.getEmail() != null){
			   String email = advanceSearchRequest.getEmail();
			   email = email.replace('*', '%');
			   email=email.toUpperCase();
			   advanceSearchRequest.setEmail(email);
		   }
		   if(advanceSearchRequest.getAddress() != null ){
			   String address = advanceSearchRequest.getAddress();
			   address = address.toUpperCase();
			   advanceSearchRequest.setAddress(address);
		   }

		   if(advanceSearchRequest.getCity() != null ){
			   String city = advanceSearchRequest.getCity();
			   city = city.toUpperCase();
			   advanceSearchRequest.setCity(city);
		   }
		   if(advanceSearchRequest.getState() != null ){
			   String state = advanceSearchRequest.getState();
			   state = state.toUpperCase();
			   advanceSearchRequest.setState(state);
		   }
		   if(advanceSearchRequest.getDisplayName() != null){
			   String displayName = advanceSearchRequest.getDisplayName();
			   displayName = displayName.toUpperCase();
			   advanceSearchRequest.setDisplayName(displayName);
		   }
		   if(advanceSearchRequest.getJanrainAccountStatus() != null ){
			   String janrainAccountStatus = advanceSearchRequest.getJanrainAccountStatus();
			   janrainAccountStatus = janrainAccountStatus.toUpperCase();
			   advanceSearchRequest.setJanrainAccountStatus(janrainAccountStatus);
		   }
		   if(advanceSearchRequest.getMemberIdentifier() != null && advanceSearchRequest.getMemberIdentifier().getName() != null &&  advanceSearchRequest.getMemberIdentifier().getValue() != null){
			   String name = advanceSearchRequest.getMemberIdentifier().getName();
			   name = name.toUpperCase();
			   advanceSearchRequest.getMemberIdentifier().setName(name);
			   String value = advanceSearchRequest.getMemberIdentifier().getValue();
			   value = value.toUpperCase();
			   advanceSearchRequest.getMemberIdentifier().setValue(value);
		   }
		   if(advanceSearchRequest.getSocialDomain() != null && advanceSearchRequest.getSocialDomain().getName() != null && advanceSearchRequest.getSocialDomain().getValue() != null){
			   String name = advanceSearchRequest.getSocialDomain().getName();
			   name = name.toUpperCase();
			   advanceSearchRequest.getSocialDomain().setName(name);
			   String value = advanceSearchRequest.getSocialDomain().getValue();
			   value = value.toUpperCase();
			   advanceSearchRequest.getSocialDomain().setValue(value);
		   }
		   if(advanceSearchRequest.getZip() != null){
			   String zip = advanceSearchRequest.getZip();
			   zip = zip.toUpperCase();
			   advanceSearchRequest.setZip(zip);
		   }
	}
	public ActivitySearchResponse searchActivity(ActivitySearchRequest searchRequest) throws BadRequestException{
		
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(searchRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("searchActivity : "+e);
			throw new WebApplicationException(e);
		}
		
	    if(validationErrors != null){
	    	throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
	    }

	   /* if(searchRequest.getTagObject().getName()== null || searchRequest.getTagObject().getValue() == null){
	    	throw new BadRequestException(CSRConstants.TAG_OBJECT_ERROR,ErrorCode.GEN_INVALID_ARGUMENT);
	    }*/
	    
	   if(searchRequest.getTagObject() != null && searchRequest.getTagObject().getName() == null && searchRequest.getTagObject().getValue() != null){
	    	throw new BadRequestException(CSRConstants.TAG_OBJECT_NAME_ERROR,ErrorCode.GEN_INVALID_ARGUMENT);
	    }
	    
	    if(searchRequest.getStartDate() != null && searchRequest.getEndDate() != null){
	    	validateDate(searchRequest.getStartDate(),searchRequest.getEndDate());
	    }
	    
	    List<ActivitySearch> activityObjects = activityDAO.searchActivityRev(searchRequest);

	    ActivitySearchResponse resp = new ActivitySearchResponse();
		if(activityObjects != null && activityObjects.size()>0){
			if(activityObjects.size() >= CSRConstants.MAX_RECORDS_FOR_POINTS){
				resp.setMaximumRecords(CSRConstants.MAX_RECORDS);
				resp.setActivityObjects(activityObjects.subList(0, CSRConstants.MAX_RECORDS_FOR_POINTS));
				return resp;
			}
		}else{
			throw new BadRequestException(ErrorCode.NO_HISTORY_FOUND,ErrorCode.NO_HISTORY_FOUND);
		}
		//valid Data
		resp.setMaximumRecords(null);
		resp.setActivityObjects(activityObjects);
		return resp;
	}
	
	public SessionSearchResponse searchSession(SessionSearchRequest request) throws BadRequestException{
		
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(request, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("searchSession : "+e);
			throw new WebApplicationException(e);
		}
		
	    if(validationErrors != null){
	    	throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
	    }	
		
		if((request.getIpAddress() == null && request.getMemberId() == null
				&&  request.getSearchSessionUUID() == null &&  request.getSessionDateSearchEnd() == null &&  request.getSessionDateSearchStart() == null 
				&&  request.getSiteId() == null)){
			logger.error(CSRConstants.NO_FIELD_SELECTION_ERROR);
			throw new BadRequestException(CSRConstants.NO_FIELD_SELECTION_ERROR,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		if(request.getSessionDateSearchStart() != null && request.getSessionDateSearchEnd() != null){
			validateDate(request.getSessionDateSearchStart(), request.getSessionDateSearchEnd());
		}
		
		List<SessionSearch> sessionSearchList = activityDAO.searchSession(request);
		SessionSearchResponse resp = new SessionSearchResponse();
		if(sessionSearchList != null && sessionSearchList.size()>0){
				
			if(sessionSearchList.size() >= CSRConstants.MAX_RECORDS_FOR_POINTS){
				resp.setMaximumRecords(CSRConstants.MAX_RECORDS);
				resp.setSessions(sessionSearchList.subList(0, CSRConstants.MAX_RECORDS_FOR_POINTS));
				return resp;
			}
			
		}else{
			throw new BadRequestException(ErrorCode.NO_HISTORY_FOUND,ErrorCode.NO_HISTORY_FOUND);
		}
		//valid Data
		resp.setMaximumRecords(null);
		resp.setSessions(sessionSearchList);
		return resp;
	}
	private boolean validateDate(String startDate, String endDate) throws BadRequestException{
		// Parsing String to Date
				Date fromDate = null;
				Date toDate = null;
				
				SimpleDateFormat sdf1 = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.SSS");
				try {
					fromDate = sdf1.parse(startDate);
					toDate = sdf1.parse(endDate);				
				} catch (ParseException e) {
					logger.error("ParseException : "	+ startDate + " & "+ endDate);
				}

				// startDate > endDate
				if (fromDate.after(toDate)) {
					throw new BadRequestException(ErrorCode.DATE_ERROR_MESSAGE,ErrorCode.GEN_INVALID_ARGUMENT);
				}
				return true;
	}
	private void validateWildCard(CSRAdvanceSearchRequest advanceSearchRequest) throws BadRequestException{
		if(advanceSearchRequest != null && advanceSearchRequest.getFirstName() != null){
			if(advanceSearchRequest.getFirstName().indexOf('*') != -1 && advanceSearchRequest.getFirstName().length()<=3){
				throw new BadRequestException(ErrorCode.ADV_SEARCH_WILD_LENTH_LIMIT,ErrorCode.GEN_INVALID_ARGUMENT);

			}
		}
		if(advanceSearchRequest != null && advanceSearchRequest.getLastName() != null){
			if(advanceSearchRequest.getLastName().indexOf('*') != -1 && advanceSearchRequest.getLastName().length()<=3){
				throw new BadRequestException(ErrorCode.ADV_SEARCH_WILD_LENTH_LIMIT,ErrorCode.GEN_INVALID_ARGUMENT);

			}
		}
		if(advanceSearchRequest != null && advanceSearchRequest.getEmail() != null){
			if(advanceSearchRequest.getEmail().indexOf('*') != -1 && advanceSearchRequest.getEmail().length()<=3){
				throw new BadRequestException(ErrorCode.ADV_SEARCH_WILD_LENTH_LIMIT,ErrorCode.GEN_INVALID_ARGUMENT);

			}
		}
	}
	
	private void searchFieldCombinationValidation(CSRAdvanceSearchRequest advanceSearchRequest)  throws BadRequestException{
	
		boolean isAtleastOneParamSet = false;
		
		if(advanceSearchRequest.getMemberId()!= null  ^ advanceSearchRequest.getMemberIdentifier() != null){
			isAtleastOneParamSet = true;
		}
		
		if(isAtleastOneParamSet ^ advanceSearchRequest.getHouseHoldId() != null ){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		if(isAtleastOneParamSet  ^ advanceSearchRequest.getSocialDomain() != null ){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		
		if(isAtleastOneParamSet ^ advanceSearchRequest.getDisplayName() != null ){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		

		if(isAtleastOneParamSet ^ advanceSearchRequest.getEmail() != null){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		if(isAtleastOneParamSet ^ advanceSearchRequest.getJanrainUUID() !=null){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		/*
		if(isAtleastOneParamSet ^ advanceSearchRequest.getSessionUUID() !=null){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}*/
		
		if(isAtleastOneParamSet ^ advanceSearchRequest.getMobile() !=null ){
			isAtleastOneParamSet = true;
		}else{
			if(isAtleastOneParamSet)
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}

		if(!(isAtleastOneParamSet ^ (advanceSearchRequest.getAddress() != null || advanceSearchRequest.getCity() != null || advanceSearchRequest.getState() != null || advanceSearchRequest.getFirstName() != null || advanceSearchRequest.getLastName() != null || advanceSearchRequest.getBirthDate() != null
				|| advanceSearchRequest.getCreatedDate() != null || advanceSearchRequest.getJanrainAccountStatus() !=null || advanceSearchRequest.getZip() != null))){
			throw new BadRequestException(ErrorCode.ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC,ErrorCode.GEN_INVALID_ARGUMENT);
		}
		
		
	}
	
}
