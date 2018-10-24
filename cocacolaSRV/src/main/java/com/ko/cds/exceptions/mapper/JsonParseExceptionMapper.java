package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;
@Provider 
public class JsonParseExceptionMapper implements
		ExceptionMapper<JsonParseException> {
	private static final Logger logger = LoggerFactory
			.getLogger(JsonParseExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(JsonParseException exception) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
		}
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" JsonMappingException", exception);
		return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST,
				ErrorCode.GEN_INVALID_ARGUMENT, exception.getMessage());

	}

}
