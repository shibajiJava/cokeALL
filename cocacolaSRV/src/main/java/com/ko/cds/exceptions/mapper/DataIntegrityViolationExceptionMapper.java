package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

@Provider
public class DataIntegrityViolationExceptionMapper implements ExceptionMapper<DataIntegrityViolationException> {
	private static final Logger logger = LoggerFactory.getLogger(DataIntegrityViolationExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(DataIntegrityViolationException exception) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
		}
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" DataIntegrityViolationExceptionMapper :"+exception.getMessage());
		return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.DATA_BASE_INTERNAL_ERROR, exception.getMessage());
		
	}

}
