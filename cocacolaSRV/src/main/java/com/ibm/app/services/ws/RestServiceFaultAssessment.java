package com.ibm.app.services.ws;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.app.services.util.XmlUtils;
import com.ibm.app.services.util.ErrorCodes;

import javax.xml.parsers.ParserConfigurationException;

/*import com.ibm.app.framework.error.ErrorCodes;
import com.ibm.app.framework.error.ErrorResponseGenerator;
import com.ibm.app.framework.util.XmlUtils;*/

public class RestServiceFaultAssessment {

	
	 private final Logger log = LoggerFactory.getLogger(getClass());

	    private RestClientException rce;

	    private HttpStatus httpStatus;
	    private int errorCode;
	    private String message;
	    private String technicalMessage;
	    private String moreInfo;
	    private String correlationContext;

	    public RestServiceFaultAssessment(final RestClientException rce) throws SAXException, ParserConfigurationException, IOException {
	        this.rce = rce;
	        parse(rce);
	    }

	    public HttpStatus getHttpStatus() {
	        return httpStatus;
	    }

	    public int getIBMCocaErrorCode() {
	        return errorCode;
	    }

	    public String getIBMCocaMessage() {
	        return message;
	    }

	    public String getIBMCocaTechnicalMessage() {
	        return technicalMessage;
	    }

	    public String getMoreInfo() {
	        return moreInfo;
	    }

	    public String getCorrelationContext() {
	        return correlationContext;
	    }

	    private void parse(final RestClientException rce) throws SAXException, ParserConfigurationException, IOException {
	        if (rce instanceof HttpStatusCodeException) {
	            // Known 4xx or 5xx errors 
	            HttpStatusCodeException hsce = (HttpStatusCodeException) rce;
	            // log.error("The called service returned with:\n" + hsce.getResponseBodyAsString());
	            httpStatus = hsce.getStatusCode();
	            boolean errorDataPresent = ErrorResponseGenerator.isIBMCocaErrorDataPresent(hsce.getResponseHeaders());
	            Element errorNode = null;
	            String body = hsce.getResponseBodyAsString();
	            if (errorDataPresent && !StringUtils.isEmpty(body)) {
	                Document doc = textToXmlDocument(body);
	                if (doc == null) {
	                    errorDataPresent = false;
	                } else {
	                    NodeList list = doc.getElementsByTagName("error");
	                    errorNode = (Element) (XmlUtils.isEmpty(list) ? null : list.item(0));
	                    if (errorNode == null) {
	                        errorDataPresent = false;
	                    }
	                }
	                if (!errorDataPresent || errorNode == null) {
	                    log.warn(String.format("IBMCoca error data not present as indicated by header - body=%s", body));
	                }
	            }
	            if (errorDataPresent && errorNode != null) {
	                // Response contains a IBMCoca standard error response, forward it
	                String IBMCocaErrorCode = StringEscapeUtils.unescapeXml(getChildContent(errorNode, "code"));
	                String IBMCocaMessage = StringEscapeUtils.unescapeXml(getChildContent(errorNode, "message"));
	                String IBMCocaTechnicalMessage = StringEscapeUtils.unescapeXml(getChildContent(errorNode, "technicalMessage"));
	                String IBMCocaMoreInfo = StringEscapeUtils.unescapeXml(getChildContent(errorNode, "moreInfo"));
	                String IBMCocaCorrelationContext = StringEscapeUtils.unescapeXml(getChildContent(errorNode, "correlationContext"));

	                httpStatus = hsce.getStatusCode(); 
	                // TODO pass hsce.getStatusText() also back to client (Spring framework will loose it)
	                errorCode = Integer.parseInt(IBMCocaErrorCode);
	                message = IBMCocaMessage;
	                technicalMessage = IBMCocaTechnicalMessage;
	                moreInfo = IBMCocaMoreInfo;
	                correlationContext = IBMCocaCorrelationContext;
	            } else {
	                
	                errorCode =  ErrorCodes.TECHNICAL_UNSPECIFIED;
	                message = "Another service reported an error.";
	                technicalMessage = 
	                        hsce.getStatusCode() + " " + hsce.getStatusText() + "\n"
	                        + hsce.getResponseBodyAsString();
	                moreInfo = null;
	            }
	        } else if (rce instanceof ResourceAccessException) {
	            // The requested REST web service was not accessible.
	            ResourceAccessException rae = (ResourceAccessException) rce;
	            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
	            errorCode = ErrorCodes.TECHNICAL_WEBSERVICE_UNREACHABLE;
	            message = "Another system is not available to complete this request.";
	            technicalMessage = "Web service is not accessible: " + rae.getMessage();
	            moreInfo = null;
	        
	        } else {
	            // All other errors
	            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
	            errorCode = ErrorCodes.TECHNICAL_UNSPECIFIED;
	            message = "Another system reported an error.";
	            technicalMessage = "Call to REST web service failed: " + rce.getMessage();
	            moreInfo = null;
	        }
	    }

	    private String getChildContent(final Element e, final String childName) {
	        NodeList list = e.getElementsByTagName(childName);
	        if (list == null || list.getLength() == 0) {
	            return null;
	        }
	        return list.item(0).getTextContent();
	    }

	    private Document textToXmlDocument(final String xml) {
	        try {
	            return XmlUtils.getDocument(xml);
	        } catch (SAXException | ParserConfigurationException | IOException e) {
	            log.warn(String.format("%s %s - Could not convert to xml: %s", 
	                    e.getClass().getName(), e.getMessage(), xml), e);
	            return null;
	        }
	    }
}
