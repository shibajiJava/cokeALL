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
import com.ko.cds.request.member.MergeMemberRequest;
import com.ko.cds.request.member.UpdateMemberRequest;

/**
 * Service interface to handle REST request
 * @author IBM
 *
 */
@Component
@Path("/cds/v1")

public interface IMemberService {

	
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/member/info
	 * @param memberSearchRequest
	 * @return 
	 * @throws BadRequestException 
	 * 
	 */
	@GET
	@Path("/member/info")
	@Produces({MediaType.APPLICATION_JSON})
	public  Response searchMemberInfo( @BeanParam MemberSearchRequest memberSearchRequest) throws BadRequestException;
	
	/**
	 *  http://localhost:8080/cocacolaSRV/rest/cds/v1/member/create
	 * @param createOrUpdateMemberRequest
	 * @return
	 * @throws BadRequestException 
	 */
	@POST
	@Path("/member/create")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON}) 
		
	public Response createOrUpdateMemberInfo(MemberInfo memberInfo) throws BadRequestException;
	
	
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/member/merge
	 * 
	 * @param MergeMemberRequest
	 * @return
	 * @author IBM
	 * @throws BadRequestException
	 * 
	 */
	@POST
	@Path("/member/merge")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response mergeMembers(MergeMemberRequest memberRequest) throws BadRequestException;
	
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/member/update/id
	 * 
	 * @param memberRequest
	 * @return
	 * @author IBM
	 * @throws BadRequestException
	 */
	@POST
	@Path("/member/update/id")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateMember(UpdateMemberRequest memberRequest) throws BadRequestException;

}
