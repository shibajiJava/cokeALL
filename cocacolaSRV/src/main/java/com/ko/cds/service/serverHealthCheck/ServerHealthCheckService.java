package com.ko.cds.service.serverHealthCheck;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.service.helper.ServerHealthCheckServiceHelper;
import com.ko.cds.utils.CDSOUtils;


@Component
@Transactional
public class ServerHealthCheckService implements IServerHealthCheckService{

	@Autowired
	private ServerHealthCheckServiceHelper healthCheckHelper;
	
	@Override
	@AopServiceAnnotation
	public Response serverHealthCheck() throws BadRequestException {
		// TODO Auto-generated method stub
		return CDSOUtils.createOKResponse(healthCheckHelper.checkServerHealth());
	}

}
