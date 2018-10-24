package com.ibm.app.services.ws;


import static com.ibm.app.services.ws.HttpHeaders.IBM_ERROR_PRESENT;

import java.util.Collections;
import java.util.List;
import java.util.Map;



import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ibm.app.services.exception.ServiceError;
import com.ibm.app.services.exception.ServiceException;
import com.ibm.app.services.util.ErrorCodes;


public class ErrorResponseGenerator {


    private static final String SUBSYSTEM_REPORTING = "reporting";

    private ErrorResponseGenerator() {
        // It's a utility class.
    }

    @SuppressWarnings("rawtypes")
    public static ResponseEntity createErrorResponse(Logger log, ServiceException se) {
        HttpStatus statusCode = mapErrorCodeToStatusCode(se.getErrorCode());
        int errorCode = se.getErrorCode();
        String message = se.getMessage(); // TODO
        String technicalMessage = se.getMessage();
        String moreInfo = null;
        return createErrorResponse(log, statusCode, errorCode, message, technicalMessage, moreInfo, null);
    }

    @SuppressWarnings("rawtypes")
    public static ResponseEntity createErrorResponse(
            Logger log, HttpStatus statusCode,
            int errorCode, String message, String technicalMessage, String moreInfo, String correlationContext) {
        ServiceError error = new ServiceError();
        error.setIncidentId(null); // TODO retrieve as parameter
        error.setSubSystem(SUBSYSTEM_REPORTING);
        error.setStatusCode(statusCode);
        error.setErrorCode(errorCode);
        error.setMessage(message);
        error.setTechnicalMessage(technicalMessage);
        error.setCorrelationContext(correlationContext);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
            new MediaType(MediaType.APPLICATION_XML, Collections.singletonMap("charset", "utf-8")));
        headers.setCacheControl("no-cache");
        headers.add(com.ibm.app.services.ws.HttpHeaders.IBM_ERROR_PRESENT, "yes");
//        ResponseEntity<String> response = new ResponseEntity<String>(
//                sb.toString(), headers, statusCode);
        ResponseEntity<ServiceError> response = new ResponseEntity<ServiceError>(
                error, headers, statusCode);
        logResponseEntity(log, "Error information returned to Client:", response);
        return response;
    }

    private static void logResponseEntity(Logger log, String msg, ResponseEntity<?> re) {
        StringBuilder sb = new StringBuilder();
        if (msg != null) {
            sb.append(msg).append('\n');
        }
        sb.append(re.getStatusCode()).append(' ').append(re.getStatusCode().getReasonPhrase());
        if (re.getHeaders() != null) {
            for (Map.Entry<String, List<String>> header : re.getHeaders().entrySet()) {
                sb.append('\n').append(header.getKey()).append(": ");
                boolean multipleValues = false;
                for (String value : header.getValue()) {
                    if (multipleValues) {
                        sb.append(",");
                    }
                    sb.append(value);
                    multipleValues = true;
                }
            }
        }
        sb.append('\n').append('\n');
        sb.append(re.getBody());
        log.error(sb.toString());
    }

    public static HttpStatus mapErrorCodeToStatusCode(int errorCode) {
        // Fixed mapping
        switch (errorCode) {
            case ErrorCodes.SECURITY_UNSPECIFIED:
            case ErrorCodes.SECURITY_AUTHENTIFICATION_FAILED : return HttpStatus.UNAUTHORIZED;
            case ErrorCodes.SECURITY_AUTHORISATION_FAILED : return HttpStatus.FORBIDDEN;

            case ErrorCodes.TECHNICAL_UNSPECIFIED:
            case ErrorCodes.TECHNICAL_RUNTIME_ERROR: return HttpStatus.INTERNAL_SERVER_ERROR;
            
            default: // see below
        }

        // Range-based mapping
        if (errorCode >= ErrorCodes.EXTERNAL_CAUSE_CODES
                && errorCode <= ErrorCodes.EXTERNAL_CAUSE_CODES_END) {
            
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (errorCode >= ErrorCodes.BUSINESS_CODES
                && errorCode <= ErrorCodes.BUSINESS_CODES_END) {
            return HttpStatus.BAD_REQUEST;
        }
        if (errorCode >= ErrorCodes.SECURITY_CODES
                && errorCode <= ErrorCodes.SECURITY_CODES_END) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (errorCode >= ErrorCodes.TECHNICAL_CODES
                && errorCode <= ErrorCodes.TECHNICAL_CODES_END) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        // Default fallback
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Checks, if the returned error contains a IBM error data structure
     * 
     * @param httpHeaders
     * @return <code>true</code> if HTTP header "X-IBM-Error-Present" is set to "yes"
     */
    public static boolean isIBMCocaErrorDataPresent(HttpHeaders httpHeaders) {
        List<String> values = httpHeaders.get(IBM_ERROR_PRESENT);
        return values != null && values.size() > 0 && values.contains("yes");
    }
	
	
}
