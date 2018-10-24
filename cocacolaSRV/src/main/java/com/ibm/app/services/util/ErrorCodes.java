package com.ibm.app.services.util;

public abstract class ErrorCodes {
	// TODO Change to enum?
    public static final int TECHNICAL_CODES = 10000;
    public static final int TECHNICAL_CODES_END = 19999;
    public static final int SECURITY_CODES = 20000;
    public static final int SECURITY_CODES_END = 29999;
    public static final int BUSINESS_CODES = 30000;
    public static final int BUSINESS_CODES_END = 39999;
    public static final int EXTERNAL_CAUSE_CODES = 40000;
    public static final int EXTERNAL_CAUSE_CODES_END = 49999;

    // Security Authentification/Authorisation
    public static final int SECURITY_UNSPECIFIED = SECURITY_CODES + 0;
    public static final int SECURITY_AUTHENTIFICATION_FAILED = SECURITY_CODES + 1;
    public static final int SECURITY_AUTHORISATION_FAILED = SECURITY_CODES + 2;
    public static final int SECURITY_CREDENTIALS_EXPIRED = SECURITY_CODES + 3;

    // Technical issues (not caused by external systems)
    public static final int TECHNICAL_UNSPECIFIED = TECHNICAL_CODES + 0;
    public static final int TECHNICAL_RUNTIME_ERROR = TECHNICAL_CODES + 1;
    public static final int TECHNICAL_ILLEGAL_ARGUMENT = TECHNICAL_CODES + 2;
    public static final int TECHNICAL_WEBSERVICE_UNREACHABLE = TECHNICAL_CODES + 3;

    // Business
    public static final int BUSINESS_UNSPECIFIED = BUSINESS_CODES + 0;
    public static final int BUSINESS_REPORT_DOES_NOT_EXIST = BUSINESS_CODES + 1;

    // Issues caused by external systems
    public static final int EXTERNAL_CAUSE_UNSPECIFIED = EXTERNAL_CAUSE_CODES + 0;
    public static final int EXTERNAL_SYSTEM_UNAVAILABLE = EXTERNAL_CAUSE_CODES + 1;
    
    // - Charting service related
    public static final int CHARTING_UNSPECIFIED = EXTERNAL_CAUSE_CODES + 2000;
}
