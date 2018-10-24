package com.ko.cds.service.member;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.request.member.MemberSearchRequest;
import com.ko.cds.request.member.MemberSearchRequestV2;
import com.ko.cds.request.member.MergeMemberRequest;
import com.ko.cds.request.member.UpdateMemberRequest;

/**
 * Service interface to handle REST request
 * @author IBM
 *
 */
@Component
@Path("/cds/v2")

public interface IMemberServiceV2 {
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v2/member/info
	 * @param memberSearchRequest
	 * @return 
	 * @throws BadRequestException 
	 * 
	 */
	@GET
	@Path("/member/info")
	@Produces({MediaType.APPLICATION_JSON})
	public  Response searchMemberInfoV2( @BeanParam MemberSearchRequestV2 memberSearchRequest) throws BadRequestException;

}
