package com.ko.cds.service.question;

import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.stereotype.Component;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.question.PostQuestionRequest;



@Component
@Path(value="/cds/v1")
public interface IQuestionService {
	/**
	 * 
	 * http://localhost:8080/cocacolaSRV/rest/cds/v1/postQuestion
	 * @param postQuestion
	 * @return 
	 * @throws BadRequestException 
	 * 
	 */
	
	@POST
	@Path("/postQuestion")	
	@Consumes({MediaType.APPLICATION_JSON})
	//@Produces({MediaType.APPLICATION_JSON})
	public Response postQuestion(PostQuestionRequest postQuestionRequest) throws BadRequestException;

}
