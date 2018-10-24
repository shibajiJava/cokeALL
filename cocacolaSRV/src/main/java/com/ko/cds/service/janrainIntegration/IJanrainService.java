package com.ko.cds.service.janrainIntegration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component

@Path("/cds/v1")
public interface IJanrainService {

	@Path("/updateMemberInfo")
	@GET
	public void updateMemberInfoFJanrain();
}
