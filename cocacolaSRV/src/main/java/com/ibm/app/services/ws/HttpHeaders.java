package com.ibm.app.services.ws;

public abstract class HttpHeaders {
	
	 //---------- Standard HTTP headers ----------

    public static final String ACCEPT = "Accept";

    public static final String AUTHORIZATION = "Authorization";

    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    public static final String LOCATION = "Location";

    public static final String VARY = "Vary";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String SERVER = "Server";

    public static final String COOKIE = "Cookie";

    public static final String PRAGMA = "Pragma";

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String EXPIRES = "Expires";

    //---------- IBM specific HTTP headers ----------

    public static final String IBM_CORRELATION_CONTEXT = "X-IbmCorrelationContext";

    //---------- IBM specific HTTP headers ----------

    public static final String IBM_ERROR_PRESENT = "X-IBM-Error-Present";

    //---------- CORS related HTTP headers ----------

    // Request headers

    public static final String CORS_ORIGIN = "Origin";

    public static final String CORS_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    public static final String CORS_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    // Response headers

    /**
     * This header must be included in all valid CORS responses; omitting the
     * header will cause the CORS request to fail. The value of the header can
     * either echo the Origin request header (as in the example above), or be a
     * '*' to allow requests from any origin. If you’d like any site to be able
     * to access your data, using '*' is fine. But if you’d like finer control
     * over who can access your data, use an actual value in the header.
     */
    public static final String CORS_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    /**
     * By default, cookies are not included in CORS requests. Use this header to
     * indicate that cookies should be included in CORS requests. The only valid
     * value for this header is true (all lowercase). If you don't need cookies,
     * don't include this header (rather than setting its value to false).
     */
    public static final String CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    /**
     * (optional) - The XMLHttpRequest 2 object has a getResponseHeader() method
     * that returns the value of a particular response header. During a CORS
     * request, the getResponseHeader() method can only access simple response
     * headers. Simple response headers are defined as follows:
     * <ul>
     * <li>Cache-Control
     * <li>Content-Language
     * <li>Content-Type
     * <li>Expires
     * <li>Last-Modified
     * <li>Pragma
     * </ul>
     * If you want clients to be able to access other headers, you have to use
     * the Access-Control-Expose-Headers header. The value of this header is a
     * comma-delimited list of response headers you want to expose to the
     * client.
     */
    public static final String CORS_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    public static final String CORS_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String CORS_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String CORS_ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    private HttpHeaders() {
        // It's an utility class.
    }

}
