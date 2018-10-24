package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

public class UnrecognizedPropertyExceptionMapper implements
		ExceptionMapper<UnrecognizedPropertyException> {
	private static final Logger logger = LoggerFactory
			.getLogger(UnrecognizedPropertyExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(UnrecognizedPropertyException ex) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
			}
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+ " UnrecognizedPropertyException", ex);
		return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST,
				ErrorCode.GEN_INVALID_ARGUMENT, ex.getMessage());

	}
}
