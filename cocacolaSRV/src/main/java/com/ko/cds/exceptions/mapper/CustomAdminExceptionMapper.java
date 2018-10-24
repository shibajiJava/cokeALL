package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.ko.cds.exceptions.CustomAdminException;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;
@Provider
public class CustomAdminExceptionMapper  implements ExceptionMapper<CustomAdminException> {
	private static final Logger logger = LoggerFactory.getLogger(CustomAdminExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(CustomAdminException exception) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
		}
		
		if((exception.getMessage() != null && exception.getMessage().contains(ErrorCode.SQL_CODE_PK_VIOLATION)  && exception.getMessage().contains(ErrorCode.SQL_STATE_PK_VIOLATION))){
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" CustomAdminExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.RECORD_ALREADY_EXISTS, ErrorCode.RECORD_ALREADY_EXISTS_DESC);
		}else if((exception.getMessage() != null && exception.getMessage().contains(ErrorCode.SQL_CODE_FK_VIOLATION) && exception.getMessage().contains(ErrorCode.SQL_STATE_FK_VIOLATION))){
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" CustomAdminExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.REFERENCE_NOT_FOUND, ErrorCode.REFERENCE_NOT_FOUND_DESC);
		}else if((exception.getMessage() != null && exception.getMessage().contains("SQLCODE=") )||( exception.getMessage() != null && exception.getMessage().contains("SQLSTATE="))){
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" CustomAdminExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.UNEXPECTED_DB_ERROR, ErrorCode.UNEXPECTED_DB_ERROR_DESC);
		}else{
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" CustomAdminExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST,exception.getErrorCode(), exception.getMessage());
		}
	}

}
