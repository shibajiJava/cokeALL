package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.CSRConstants;
import com.ko.cds.utils.ErrorCode;
@Provider
public class CDSWebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException>{

	private static final Logger logger = LoggerFactory.getLogger(CDSWebApplicationExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(WebApplicationException ex) {
		Throwable runTimeEx = null;
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
		}
		if(ex.getCause()!= null)
			runTimeEx = ex.getCause().getCause();
		else
			runTimeEx=ex.getCause();
		
		if(runTimeEx instanceof  IllegalArgumentException){
			
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" CDSWebApplicationExceptionMapper :IllegalArgumentException :"+ex.getCause().getMessage());
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ErrorCode.GEN_INVALID_ARGUMENT_DESC);
		}
		/*
		else if(ex.getCause() instanceof DataIntegrityViolationException){
			logger.info("CDSWebApplicationExceptionMapper :DataIntegrityViolationException :"+ex.getCause().getMessage());
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ex.getCause().getMessage());
		}else if(ex.getCause() instanceof BadSqlGrammarException){
			logger.info("CDSWebApplicationExceptionMapper :BadSqlGrammarException :"+ex.getCause().getMessage());
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ex.getCause().getMessage());
		}*/
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+"CDSWebApplicationExceptionMapper :WebApplicationException :"+ex.getMessage());
		//logger.error("Error : CDSWebApplicationExceptionMapper :WebApplicationException :"+ex);
		if(ex.getMessage() != null && ex.getMessage().contains("SQLCODE=")){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ErrorCode.DATA_BASE_INTERNAL_ERROR);
		}else if (ex.getMessage() != null && ex.getMessage().contains("404")){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_SERVICE_NOT_AVAILABLE, ErrorCode.SERVICE_NOT_FOUND);
		}else if (ex.getMessage() != null && ex.getMessage().contains("405")){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_SERVICE_NOT_AVAILABLE, ErrorCode.SERVICE_METHOD_NOT_ALLOWED);
		}else if (ex.getMessage().equals(CSRConstants.TAG_SEARCH_LIMIT_DESC)){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, CSRConstants.TAG_SEARCH_LIMIT_DESC);
		}else if (ex.getMessage().equals(CSRConstants.TAG_OBJECT_ERROR)){
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, CSRConstants.TAG_OBJECT_ERROR);
		}else{
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, null);
		}
		
		
		
	}

}
