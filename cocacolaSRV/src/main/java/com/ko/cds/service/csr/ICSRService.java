package com.ko.cds.service.csr;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.csr.ActivitySearchRequest;
import com.ko.cds.request.csr.ActivityTagJSON;
import com.ko.cds.request.csr.CSRAdvanceSearchRequest;
import com.ko.cds.request.csr.CSRTicketGetRequest;
import com.ko.cds.request.csr.CSRTicketPostRequest;
import com.ko.cds.request.csr.CodeRedemptionHistoryRequest;
import com.ko.cds.request.csr.DeleteMemberRequest;
import com.ko.cds.request.csr.SessionSearchRequest;
import com.ko.cds.request.csr.TagJSON;

/**
 * Service interface to handle REST request for CSR
 * @author IBM
 *
 */
@Component
@Path("/cds/v1")
public interface ICSRService {
	/***
	 * This service is used by CSR tools.The advanced search allows for wild cards for the firstName, lastName, and email address. The other fields are set to not allow for wildcard transactions.
	 *  Wildcard search is automatic when the client uses an “*” to denote a wildcard search
	 *  The advanced search fields can be combined together to allow for narrowing down a list of available members.  This would be an “and” type of action. 
	 *  There is not as “or” type of call to the search member API
	 * 
	 * 
	 * @param advanceSearchRequest
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/member/csr/info")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response csrAdvanceSearch(@BeanParam CSRAdvanceSearchRequest advanceSearchRequest,@QueryParam("socialDomain") TagJSON socialDomain,@QueryParam("memberIdentifier") TagJSON memberIdentifer ) throws BadRequestException;
	
	
	
	/***
	 * The API returns a complete history either by Log, campaign, clientTransactionId or memberId . 
	 *  The API is able to provide this information by joining on clientTransactionId across the code redemption and points transaction tables
	 *  If there is a join on clienTransactionid then return "Points" else "No points redemption" 
	 * 
	 * @param codeRedemptionHistoryRequest
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/history/coderedemption")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response getCodeRedemptionHistory(@BeanParam CodeRedemptionHistoryRequest codeRedemptionHistoryRequest)	throws BadRequestException ;


	
	/**
	 * The search session search hits the record session table and provides records on when and where a member accessed specific sites. 
	 * Only clients that are calling the record session API appear within this transaction log. 
	 *
	 *
	 * @param sessionSearchRequest
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/search/session")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response getSessionSearch(@BeanParam SessionSearchRequest sessionSearchRequest)	throws BadRequestException ;

	
	
	
	/**
	 * The search activity API allows for a CSR agent to search for a specific member’s activity that occurred on the site. 
	 * The CSR rep needs to know the specific tag and the member ID to perform a search.
	 * Only one Tag can be searched at a time during the search.
	 * 
	 * 
	 * @param activitySearchRequest
	 * @param tagObject
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/search/activity")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response getActivitySearch(@BeanParam ActivitySearchRequest activitySearchRequest,@QueryParam("tagObject") ActivityTagJSON tagObject)	throws BadRequestException ;

	
	
	
	/**
	 * The delete member API deletes the member in CDS and CDS then updates janrain a deleted reason. This immediate call to janrain removes the consumer/member forever and this transaction cannot be rolled back.  
	 * CDS stores the deleted reason code in CDSO and then copies this reason into CDSA for reporting. 
	 *  
	 *  @param deleteMemberRequest
	 * @return
	 * @throws BadRequestException
	 */
	@POST
	@Path("/member/delete")
	@Produces({MediaType.APPLICATION_JSON}) 
	@Consumes({MediaType.APPLICATION_JSON})
	public Response postDeleteMember(DeleteMemberRequest deleteMemberRequest)	throws BadRequestException ;
	
	
	
	/**
	 * The clientTransactionId need to be used for storing the information into the CDS system and must be used on subsequent call.
	 *  This is to allow for matching across different calls within the system
	 *  This API inserts records in CSR_Ticket_History table
	 * @param deleteMemberRequest
	 * @return
	 * @throws BadRequestException
	 */
	@POST
	@Path("/csrTicket/history")
	@Produces({MediaType.APPLICATION_JSON}) 
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createCSRTicketHistory(CSRTicketPostRequest cSRTicketPostRequest)	throws BadRequestException ;
	
	/**
	 * This API returns CSR transaction history based on memberId and sessionUUID
	 * @param deleteMemberRequest
	 * @return
	 * @throws BadRequestException
	 */
	@GET
	@Path("/csrTicket/history")
	@Produces({MediaType.APPLICATION_JSON}) 
	public Response getCSRTicketHistory(@BeanParam CSRTicketGetRequest cSRTicketGetRequest)	throws BadRequestException ;
}
