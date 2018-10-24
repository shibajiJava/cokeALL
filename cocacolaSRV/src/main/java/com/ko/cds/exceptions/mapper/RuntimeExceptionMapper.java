package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
	Logger logger = LoggerFactory.getLogger(RuntimeExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(RuntimeException ex) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
			}
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" RuntimeExceptionMapper :RuntimeException :"+ex.getMessage());
		if(ex.getMessage() != null && ex.getMessage().contains("SQLCODE=")){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ErrorCode.DATA_BASE_INTERNAL_ERROR);
		}else{
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, null);
		}
		
	}

}
