package com.ko.cds.exceptions.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ko.cds.exceptions.BadRequestException;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;
@Provider
public class BadRequestExceptionMapper  implements ExceptionMapper<BadRequestException> {
	private static final Logger logger = LoggerFactory.getLogger(BadRequestExceptionMapper.class);
	@Context
	HttpServletRequest httpServletRequest;
	@Override
	public Response toResponse(BadRequestException exception) {
		String sessionId = null;
		String requestURI = null;
		if(httpServletRequest != null){
			sessionId = httpServletRequest.getSession().getId();
			requestURI = httpServletRequest.getRequestURI();
		}
		logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" BadRequestExceptionMapper :"+exception.getMessage());
		if((exception.getMessage() != null && exception.getMessage().contains("SQLCODE=") )||( exception.getMessage() != null && exception.getMessage().contains("SQLSTATE="))){
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" BadRequestExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST, ErrorCode.GEN_INVALID_ARGUMENT, ErrorCode.DATA_BASE_INTERNAL_ERROR);
		}else if(ErrorCode.MEMBER_NOT_FOUND.equals(exception.getErrorCode()) || ErrorCode.NO_HISTORY_FOUND.equals(exception.getErrorCode()) || ErrorCode.DUPLICATE_SESSION_UUID_FOUND.equals(exception.getErrorCode()) ){
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" BadRequestExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_OK,exception.getErrorCode(), exception.getMessage());
		} else{
			logger.error("Session ID :"+sessionId+" Request URI:"+requestURI+" BadRequestExceptionMapper :",exception);
			return CDSOUtils.createErrorResponse(ErrorCode.STATUS_CODE_BAD_REQUEST,exception.getErrorCode(), exception.getMessage());
		}
	}

}
