package com.ko.cds.service.member;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.request.member.MemberSearchRequest;
import com.ko.cds.request.member.MemberSearchRequestV2;
import com.ko.cds.request.member.MergeMemberRequest;
import com.ko.cds.request.member.UpdateMemberRequest;
import com.ko.cds.response.member.CreateOrUpdateMemberResponse;
import com.ko.cds.response.member.MergeMemberResponse;
import com.ko.cds.service.helper.MemberServiceHelper;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.utils.CDSOUtils;

@Component

@Transactional
public class MemberService implements IMemberService,IMemberServiceV2 {
	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    @Autowired
	private MemberDAO memberDAO;
    @Autowired
    private MemberServiceHelper serviceHelper;
    @Autowired
    private PointAccountServiceHelper pointAccountServiceHelper;
    @Autowired
    private PointAccountDAO pointAccountDAO;
	
	@Transactional(readOnly=true)
	@Override
	@AopServiceAnnotation
	public Response searchMemberInfo(
			MemberSearchRequest memberSearchRequest) throws BadRequestException {
		MemberInfo memberInfo=serviceHelper.searchMemberInfo(memberSearchRequest);
		return CDSOUtils.createOKResponse(memberInfo);
	}

	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@Override
	@AopServiceAnnotation
	public Response createOrUpdateMemberInfo(
			MemberInfo memberInfo) throws BadRequestException {
		CreateOrUpdateMemberResponse createResponse = new CreateOrUpdateMemberResponse();
		BigInteger memberId = null;
		memberId=serviceHelper.createMemberInfo(memberInfo);
			
		createResponse.setMemberId(memberId);
		return CDSOUtils.createOKResponse(createResponse);
	}

	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@Override
	@AopServiceAnnotation
	public Response mergeMembers(MergeMemberRequest mergeMemberRequest) throws BadRequestException{
		
		MergeMemberResponse mergeMemberResponse =new MergeMemberResponse(); 
		BigInteger tomemberId=null;
		
		//tomemberId=serviceHelper.mergeMemberDetails(mergeMemberRequest);

		tomemberId=serviceHelper.mergeLiteMemberDetails(mergeMemberRequest);
		mergeMemberResponse.setMemberId(tomemberId);
		logger.info("Merge operation is complete for lite Memeber Account "+mergeMemberRequest.getFromMemberId()+" to Full Member Account "+ mergeMemberRequest.getToMemberId());
		return CDSOUtils.createOKResponse(mergeMemberResponse);
		
	}
	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@Override
	@AopServiceAnnotation
	public Response updateMember(UpdateMemberRequest memberRequest)
			throws BadRequestException {
		logger.info("Inside updateMember");
		serviceHelper.updateMember(memberRequest);
		//Update Janrain

		return CDSOUtils.createOKResponse(null);
	}

	public MemberDAO getMemberDAO() {
		return memberDAO;
	}

	public void setMemberDAO(MemberDAO memberDAO) {
		this.memberDAO = memberDAO;
	}
	public MemberServiceHelper getServiceHelper() {
		return serviceHelper;
	}

	public void setServiceHelper(MemberServiceHelper serviceHelper) {
		this.serviceHelper = serviceHelper;
	}

	@Transactional(readOnly=true)
	@Override
	@AopServiceAnnotation
	public Response searchMemberInfoV2(MemberSearchRequestV2 memberSearchRequest) throws BadRequestException {
		List<MemberInfo> members=serviceHelper.searchMemberInfoV2(memberSearchRequest);
		return CDSOUtils.createOKResponse(members);
	}
	

	/*private String validateSearchParameterValueAgaintsSearchParameterName(String searchParameterName,String searchParameterValue){
		//if searchParameterName is CDS Member ID , then searchParameterValue will be BigInteger
		//if(CDSConstants.MEMBER_SEARCH_PARAMETER_MEMMER_ID.equals(searchParameterName)){
			//DigitsCheck digitCheck = new DigitsCheck();
			
		//}
		return null;
	}*/
    
	

}
