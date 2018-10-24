package com.ko.cds.service.csr;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.csr.CSRMemberInfo;
import com.ko.cds.pojo.csr.CSRTicket;
import com.ko.cds.request.csr.ActivitySearchRequest;
import com.ko.cds.request.csr.ActivityTagJSON;
import com.ko.cds.request.csr.CSRAdvanceSearchRequest;
import com.ko.cds.request.csr.CSRTicketGetRequest;
import com.ko.cds.request.csr.CSRTicketPostRequest;
import com.ko.cds.request.csr.CodeRedemptionHistoryRequest;
import com.ko.cds.request.csr.DeleteMemberRequest;
import com.ko.cds.request.csr.SessionSearchRequest;
import com.ko.cds.request.csr.TagJSON;
import com.ko.cds.response.csr.ActivitySearchResponse;
import com.ko.cds.response.csr.CSRAdvanceSearchResponse;
import com.ko.cds.response.csr.CSRTicketResponse;
import com.ko.cds.response.csr.CodeRedemptionHistoryResponse;
import com.ko.cds.response.csr.SessionSearchResponse;
import com.ko.cds.service.helper.CSRServiceHelper;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.CSRConstants;


@Component
@Transactional
public class CSRService implements ICSRService {
	
	private static final Logger logger = LoggerFactory.getLogger(CSRService.class);
	
	@Autowired
	 private CSRServiceHelper csrServiceHelper;
	
	 
	@Transactional(readOnly=true)
	@Override
	@AopServiceAnnotation
	public Response csrAdvanceSearch(
			CSRAdvanceSearchRequest advanceSearchRequest,TagJSON socialDomain,TagJSON memberIdentifier)
			throws BadRequestException {
		   if(socialDomain != null)
			 advanceSearchRequest.setSocialDomain(socialDomain.getDTO());
		   if(memberIdentifier != null)
			   advanceSearchRequest.setMemberIdentifier(memberIdentifier.getDTO());
		  
		   List<CSRMemberInfo> csrMemberInfoList =csrServiceHelper.csrAdvanceSearch(advanceSearchRequest);
		   CSRAdvanceSearchResponse advanceSearchResponse = new CSRAdvanceSearchResponse();
		   
		   if(csrMemberInfoList != null && csrMemberInfoList.size() >= CSRConstants.MAX_RECORDS_FOR_POINTS){
			   advanceSearchResponse.setMaximumRecords(CDSConstants.MAX_RECORD_STR);
			   List<CSRMemberInfo> truncatedList = CDSOUtils.copyList(csrMemberInfoList, 0, CSRConstants.MAX_RECORDS_FOR_POINTS);
			   advanceSearchResponse.setCsrMembers(truncatedList);
		   }else{
			   advanceSearchResponse.setCsrMembers(csrMemberInfoList);   
		   }
		return CDSOUtils.createOKResponse(advanceSearchResponse);
	}

	
	@AopServiceAnnotation
	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@Override
	public Response getCodeRedemptionHistory(
			CodeRedemptionHistoryRequest codeRedemptionHistoryRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		CodeRedemptionHistoryResponse resp= csrServiceHelper.getCodeRedemptionHistory(codeRedemptionHistoryRequest);
		return CDSOUtils.createOKResponse(resp);
	}
	
	
	
	@Override
	@AopServiceAnnotation
	@Transactional(readOnly=true)
	public Response getSessionSearch(SessionSearchRequest sessionSearchRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		SessionSearchResponse resp = csrServiceHelper.searchSession(sessionSearchRequest);
		return CDSOUtils.createOKResponse(resp);
	}

	@Override
	@AopServiceAnnotation
	@Transactional(readOnly=true)
	public Response getActivitySearch(
			ActivitySearchRequest activitySearchRequest,ActivityTagJSON tagobject)
			throws BadRequestException {
		// TODO Auto-generated method stub
		 if(tagobject != null)
			 activitySearchRequest.setTagObject(tagobject.getDTO());
		ActivitySearchResponse act = csrServiceHelper.searchActivity(activitySearchRequest);
		return CDSOUtils.createOKResponse(act);
	}
	
	
	
	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@Override
	@AopServiceAnnotation
	public Response postDeleteMember(DeleteMemberRequest deleteMemberRequest)
			throws BadRequestException {
		// TODO Auto-generated method stub
		csrServiceHelper.deleteMemberfromCdso(deleteMemberRequest);
		
		return CDSOUtils.createOKResponse(null);
	}


	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@Override
	@AopServiceAnnotation
	public Response createCSRTicketHistory(
			CSRTicketPostRequest cSRTicketPostRequest)
			throws BadRequestException {
		csrServiceHelper.createCSRTicketHistory(cSRTicketPostRequest);
		return CDSOUtils.createOKResponse(null);
	}


	@Override
	@AopServiceAnnotation
	public Response getCSRTicketHistory(CSRTicketGetRequest cSRTicketGetRequest)
			throws BadRequestException {
		List<CSRTicket> csrTicketList=csrServiceHelper.getCSRTicketHistory(cSRTicketGetRequest);
		CSRTicketResponse csrResponse = new CSRTicketResponse();
		csrResponse.setCsrTickets(csrTicketList);
		if(csrTicketList != null && csrTicketList.size() >= CSRConstants.MAX_RECORDS_FOR_POINTS){
			 csrResponse.setMaximumRecords(CDSConstants.MAX_RECORD_STR);
		   }
		return CDSOUtils.createOKResponse(csrResponse);
	}
}
