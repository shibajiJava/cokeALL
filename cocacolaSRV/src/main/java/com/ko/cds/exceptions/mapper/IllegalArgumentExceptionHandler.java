package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;
@Provider
public class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {
    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionHandler.class);
    @Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(IllegalArgumentException ex) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
		}
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+"IllegalArgumentExceptionHandler",ex);
		if(ex.getMessage() != null && ex.getMessage().contains("SQLCODE=")){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ErrorCode.DATA_BASE_INTERNAL_ERROR);
		}{
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ex.getMessage());
		}
		
	}

	

}
