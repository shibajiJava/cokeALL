package com.ibm.app.services.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.http.HttpStatus;

public class ServiceError {
	 private int statusCode;
	    private String incidentId;
	    private String subSystem;
	    private int errorCode;
	    private String message;
	    private String technicalMessage;
	    private String moreInfo;
	    private String correlationContext;

	    /**
	     * Whenever a ServiceError object is instantiated, ensure the provided 
	     * error code will be mapped appropriately in 
	     * {@link ErrorResponseGenerator#mapErrorCodeToStatusCode(int)}.
	     */
	    public ServiceError() {
	        //
	    }

	    @XmlElement(name = "status")
	    public int getStatusCode() {
	        return this.statusCode;
	    }

	    public HttpStatus getHttpStatus() {
	        return HttpStatus.valueOf(statusCode);
	    }

	    public void setStatusCode(int statusCode) {
	        this.statusCode = statusCode;
	    }

	    public void setStatusCode(HttpStatus status) {
	        this.statusCode = status.value();
	    }

	    @XmlElement(name = "incidentId")
	    public String getIncidentId() {
	        return incidentId;
	    }

	    public void setIncidentId(String incidentId) {
	        this.incidentId = incidentId;
	    }

	    @XmlElement(name = "subSystem")
	    public String getSubSystem() {
	        return subSystem;
	    }

	    public void setSubSystem(String subSystem) {
	        this.subSystem = subSystem;
	    }

	    @XmlElement(name = "code")
	    public int getErrorCode() {
	        return errorCode;
	    }

	    public void setErrorCode(int errorCode) {
	        this.errorCode = errorCode;
	    }

	    @XmlElement(name = "message")
	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    @XmlElement(name = "technicalMessage")
	    public String getTechnicalMessage() {
	        return technicalMessage;
	    }

	    public void setTechnicalMessage(String technicalMessage) {
	        this.technicalMessage = technicalMessage;
	    }

	    @XmlElement(name = "moreInfo")
	    public String getMoreInfo() {
	        return moreInfo;
	    }

	    public void setMoreInfo(String moreInfo) {
	        this.moreInfo = moreInfo;
	    }

	    @XmlElement(name = "correlationContext")
	    public String getCorrelationContext() {
	        return this.correlationContext;
	    }

	    public void setCorrelationContext(String correlationContext) {
	        this.correlationContext = correlationContext;
	    }

	    @Override
	    public String toString() {
	        return "ServiceError [statusCode="
	                + statusCode
	                + ", "
	                + (incidentId != null ? "incidentId=" + incidentId + ", " : "")
	                + (subSystem != null ? "subSystem=" + subSystem + ", " : "")
	                + "errorCode="
	                + errorCode
	                + ", "
	                + (message != null ? "message=" + message + ", " : "")
	                + (technicalMessage != null ? "technicalMessage="
	                        + technicalMessage + ", " : "")
	                + (moreInfo != null ? "moreInfo=" + moreInfo + ", " : "")
	                + (correlationContext != null ? "correlationContext="
	                        + correlationContext : "") + "]";
	    }
}
