package com.ibm.app.services.exception;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private boolean clientInduced;

    private int errorCode;

    private String technicalMessage;  // TODO tbd

    private String moreInfo; // TODO tbd

    // TODO parameter list for completion of message??

    /**
     * <b>NOTE:</b> Ensure that the provided 
     * error code will be mapped appropriately in 
     * {@link ErrorResponseGenerator#mapErrorCodeToStatusCode(int)}.
     * 
     * @param message message
     * @param cause causing exception
     * @param clientInduced <code>true</code> indicates that error was caused by bad parameters passed by client.
     * @param errorCode IBM COCOCOLA  error code. 
     */    
    public ServiceException(String message, Throwable cause, boolean clientInduced, int errorCode) {
        super(message, cause);
        this.clientInduced = clientInduced;
        this.errorCode = errorCode;
    }

    /**
     * @return error code identifying the issue cause.
     * @see ErrorCodes
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @return information for technical-orientied audience (e.g. log files) about the issue. Should NOT be shown to the end user.
     */
    public String getTechnicalMessage() {
        return technicalMessage;
    }

    /**
     * @return Additional information about the issue.
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * @return <code>true</code> signals that the issue was caused by wrong or bad parameter provided by the client; <code>false</code> indicates a problem at server's implementation side.
     */
    public boolean isClientInduced() {
        return clientInduced;
    }

    @Override
    public String toString() {
        return "ServiceException [clientInduced=" + clientInduced
                + ", errorCode=" + errorCode + ", technicalMessage="
                + technicalMessage + ", moreInfo=" + moreInfo + "]";
    }
}
