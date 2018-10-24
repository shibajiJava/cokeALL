package com.ibm.app.services.ws;

import static com.ibm.app.services.ws.HttpHeaders.CONTENT_ENCODING;
import static com.ibm.app.services.ws.HttpHeaders.SERVER;
import static com.ibm.app.services.ws.HttpHeaders.TRANSFER_ENCODING;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.ibm.app.services.util.ErrorCodes;

public abstract class AbstractWebService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	/**
     * Interceptors which modify the outgoing service calls to implement 
     * certain functionalities.
     */
    private final static List<ClientHttpRequestInterceptor> INTERCEPTORS;

    static {
        INTERCEPTORS = new LinkedList<>();
        INTERCEPTORS.add(new CorrelationContextSpringInterceptor());
        INTERCEPTORS.add(new OutgoingSecuritySpringInterceptor());
        INTERCEPTORS.add(new EnableServerCompressionInterceptor(true));
    }

  

    @Autowired
    protected ServletContext servletContext;

    @Autowired
    protected RestTemplate restTemplateHttps;

//    @Autowired
//    protected RestTemplate restTemplateHttp;

    @Autowired
    protected ProxyConfigurationProvider configurationProvider;

    private HttpClient client;

    private final String HTTPS = "https"; //configurationProvider.getProperty("HTTPS");

    private final int HTTPS_PORT = 443; //Integer.parseInt(configurationProvider.getProperty("HTTPS_PORT"));

    private final String HTTPS_HOST = "localhost"; //configurationProvider.getProperty("HTTPS_HOST");

    protected AbstractWebService() {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Configured outgoing interceptors: %s", INTERCEPTORS));
        }
    }

    /**
     * Forwards a request to another web service implementation using HTTP GET.
     * 
     * @param nwr
     * @param urlKey key indicating the real fulfilling web service URL
     * @return response from the proxied web service
     */
    
    @SuppressWarnings("rawtypes")
    protected ResponseEntity forwardWebserviceRequestGet(final NativeWebRequest nwr, final String urlKey) {
        HttpServletRequest req = ((HttpServletRequest) nwr.getNativeRequest());
        String decodedQuerySuffix = (req.getQueryString() != null ? /*"?" +*/  URLDecoder.decode(req.getQueryString()) : "");
        log.debug("Processing: "+req.getRequestURL() + decodedQuerySuffix);
        return callWebserviceRequestGet(nwr, urlKey, "", decodedQuerySuffix, String.class);
    }

    @SuppressWarnings("rawtypes")
    protected ResponseEntity forwardWebserviceRequestGet(final NativeWebRequest nwr,
            final String urlKey, final Map<String, String> replacements) {
        HttpServletRequest req = ((HttpServletRequest) nwr.getNativeRequest());
        String decodedQuerySuffix = (req.getQueryString() != null ? /*"?" +*/  URLDecoder.decode(req.getQueryString()) : "");
        log.debug("Processing: "+req.getRequestURL() + decodedQuerySuffix);
        return callWebserviceRequestGet(nwr, urlKey, "", decodedQuerySuffix, String.class, replacements);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ResponseEntity callWebserviceRequestGet(final NativeWebRequest nwr, final String urlKey, final Class responseClass) {
        return callWebserviceRequestGet(nwr, urlKey, responseClass, Collections.EMPTY_MAP);
    }

    /**
     * Calls a web service implementation using HTTP GET.
     * 
     * @param nwr
     * @param urlKey key indicating the real fulfilling web service URL
     * @return response from the proxied web service
     */
    
    @SuppressWarnings("rawtypes")
    protected ResponseEntity callWebserviceRequestGet(
            final NativeWebRequest nwr, final String urlKey, final Class responseClass, 
            final Map<String, String> replacements) {
        HttpServletRequest req = ((HttpServletRequest) nwr.getNativeRequest());
        String decodedQuerySuffix = (req.getQueryString() != null ? "?" +  URLDecoder.decode(req.getQueryString()) : "");
        String urlPackagesList = configurationProvider.getProperty(urlKey);
        String destinationUrl = urlPackagesList + decodedQuerySuffix;
        log.info("Calling web service: GET " + destinationUrl);

        RestTemplate restTemplate = createRestTemplate();
        @SuppressWarnings("unchecked")
        ResponseEntity entity = restTemplate.getForEntity(destinationUrl, responseClass, replacements);
        logResponse(entity);

        @SuppressWarnings("unchecked")
        ResponseEntity response = new ResponseEntity(
                entity.getBody(), 
                createResponseHeaders(entity.getHeaders()), 
                entity.getStatusCode());
        return response;
    }

    /**
     * Calls a web service implementation using HTTP GET and Apache HttpClient 3.1.
     *
     * @param nwr
     * @param urlKey key indicating the real fulfilling web service URL
     * @return response from the proxied web service
     */
    
    @SuppressWarnings("rawtypes")
    protected ResponseEntity callHTTPSWebserviceRequestGet(
            final NativeWebRequest nwr, final String urlKey, final Class responseClass, 
            final Object replacements) {
        HttpServletRequest req = ((HttpServletRequest) nwr.getNativeRequest());
        String decodedQuerySuffix = (req.getQueryString() != null ? "?" +  URLDecoder.decode(req.getQueryString()) : "");
        String urlPackagesList = configurationProvider.getProperty(urlKey);
        String destinationUrl = urlPackagesList + decodedQuerySuffix;
        log.info("Calling web service: GET " + destinationUrl);

        RestTemplate restTemplate = createHTTPSRestTemplate();
        @SuppressWarnings("unchecked")
        ResponseEntity entity = restTemplate.getForEntity(destinationUrl, responseClass, replacements);
        logResponse(entity);

        @SuppressWarnings("unchecked")
        ResponseEntity response = new ResponseEntity(
                entity.getBody(),
                createResponseHeaders(entity.getHeaders()),
                entity.getStatusCode());
        return response;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ResponseEntity callWebserviceRequestGet(
            final NativeWebRequest nwr, final String urlKey, final String urlSuffix, 
            final String queryParameters, final Class responseClass) {
        return callWebserviceRequestGet(nwr, urlKey, urlSuffix, queryParameters, responseClass, Collections.EMPTY_MAP);
    }
    /**
     * Calls a web service implementation using HTTP GET.
     * 
     * @param nwr
     * @param urlKey key indicating the real fulfilling web service URL
     * @param queryParameters
     * @param responseClass
     * @return response from the proxied web service
     */
   
    @SuppressWarnings("rawtypes")
    protected ResponseEntity callWebserviceRequestGet(
            final NativeWebRequest nwr, final String urlKey, String urlSuffix, 
            final String queryParameters, final Class responseClass,final  Map<String, String> replacements) {
        String destinationUrl = configurationProvider.getProperty(urlKey);
        if (urlSuffix == null) {
            urlSuffix = "";
        }
        String querySuffix = StringUtils.isEmpty(queryParameters) ? "" : ("?" + queryParameters);
        log.info("Calling web service: GET " + destinationUrl + urlSuffix + querySuffix);
        // TODO Encode parameters (query suffix)?
        String targetUrl = destinationUrl + urlSuffix + querySuffix;
        RestTemplate restTemplate = createRestTemplate();
        @SuppressWarnings("unchecked")
        ResponseEntity entity = restTemplate.getForEntity(
                targetUrl, 
                responseClass, replacements);
        logResponse(entity);

        @SuppressWarnings("unchecked")
        ResponseEntity response = new ResponseEntity(
                entity.getBody(), 
                createResponseHeaders(entity.getHeaders()), 
                entity.getStatusCode());
        return response;
    }

    /**
     * Calls a web service implementation using HTTP POST.
     * 
     */
    @SuppressWarnings("rawtypes")
    protected ResponseEntity callWebserviceRequestPost(
            final NativeWebRequest nwr, final String urlKey, 
            final Object body, final Class responseClass) {
        String urlPackagesList = configurationProvider.getProperty(urlKey);
        String destinationUrl = urlPackagesList;
        log.info("Calling web service: POST " + destinationUrl);
        log.debug("- body      : " + body);
        destinationUrl = "http://localhost:8080/cocacolaSRV/test/testWS";
        RestTemplate restTemplate = createRestTemplate();
        @SuppressWarnings("unchecked")
        ResponseEntity entity = restTemplate.postForEntity(destinationUrl, body, responseClass);
        logResponse(entity);

        @SuppressWarnings("unchecked")
        ResponseEntity response = new ResponseEntity(
                entity.getBody(), 
                createResponseHeaders(entity.getHeaders()), 
                entity.getStatusCode());
        return response;
    }

    /**
     * Calls a web service implementation using HTTPS POST.
     *
     */
    @SuppressWarnings("rawtypes")
    protected ResponseEntity callHTTPSWebserviceRequestPost(
            final NativeWebRequest nwr, 
            final String urlKey, final Object body, final Class responseClass) {
        String urlPackagesList = configurationProvider.getProperty(urlKey);
        String destinationUrl = urlPackagesList;
        log.info("Calling https web service: POST " + destinationUrl);
        log.debug("- body      : " + body);

        RestTemplate restTemplate = createHTTPSRestTemplate();
        @SuppressWarnings("unchecked")
        ResponseEntity entity = restTemplate.postForEntity(destinationUrl, body, responseClass);
        logResponse(entity);

        @SuppressWarnings("unchecked")
        ResponseEntity response = new ResponseEntity(
                entity.getBody(),
                createResponseHeaders(entity.getHeaders()),
                entity.getStatusCode());
        return response;
    }

    protected HttpHeaders createResponseHeaders(final HttpHeaders httpHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(httpHeaders);
        // Remove unnecessary (misleading) headers
        headers.remove(TRANSFER_ENCODING);
        headers.remove(SERVER);
        headers.remove(CONTENT_ENCODING);
        return headers;
    }

    protected void logRequestHeaders(final HttpServletRequest req) {
        log.debug("Request character encoding: "+req.getCharacterEncoding());
        log.debug("Request headers:");
        for (@SuppressWarnings("unchecked")
        Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            if (log.isDebugEnabled()) {
                log.debug("- " + key + ": " + req.getHeader(key));
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void logResponse(final ResponseEntity responseEntity) {
        log.debug("Web service response:");
        log.debug("- statusCode: " + responseEntity.getStatusCode());
        log.debug("- headers   : " + responseEntity.getHeaders());
        MediaType mediaType = responseEntity.getHeaders().getContentType();
        String contentType = mediaType != null ? mediaType.toString() : "";
        if (log.isDebugEnabled()
                && (MediaType.APPLICATION_XML.equals(contentType) || contentType.startsWith("text/"))) {
            log.debug("- body      : " + responseEntity.getBody());
        }
    }

    protected Element createElementWithContent(final Document doc, final String name,
            final String content) {
                Element e = doc.createElement(name);
                e.appendChild(doc.createTextNode(StringEscapeUtils.escapeXml(content)));
                return e;
            }

    /**
     * Create URL prefix for response tags containing URLs for further resources.
     * 
     * @param nativeRequest
     * @return URL prefix of a resource
     */
    protected String getServletPrefix(final NativeWebRequest nativeRequest) {
        HttpServletRequest req = (HttpServletRequest) nativeRequest.getNativeRequest();
        String servletPrefix = req.getScheme() + "://"+ req.getServerName() + ":" + req.getServerPort() + servletContext.getContextPath();
        return servletPrefix;
    }

    /**
     * @return new RestTemplate instance with configured interceptor
     */
    protected RestTemplate createRestTemplate() {
        RestTemplate template = new RestTemplate();
        // Add interceptor which adds the correlation context header.
        template.setInterceptors(INTERCEPTORS);    
        return template;
    }

    /**
     * @return new RestTemplate instance with configured interceptor
     */
    protected RestTemplate createHTTPSRestTemplate() {
        log.info("restTemplate: " + restTemplateHttps.getClass());
        log.info("- ReqFactory: " + restTemplateHttps.getRequestFactory().getClass());
        if (restTemplateHttps.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory)
        {
        	final HttpComponentsClientHttpRequestFactory factory = (HttpComponentsClientHttpRequestFactory) restTemplateHttps.getRequestFactory();
            try {
                final Protocol authhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
                Protocol.registerProtocol("https", authhttps);
                ((HttpClient) factory.getHttpClient()).getHostConfiguration().setHost("localhost", 443, authhttps);
               
            } catch (GeneralSecurityException e) {
                log.error("Couldn't instantiate EasySSLProtocolSocketFactory. GeneralSecurityException", e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                log.error("Couldn't instantiate EasySSLProtocolSocketFactory. IOException", e);
                throw new RuntimeException(e);
            }
            // Add interceptor which adds the correlation context header.
            InterceptingClientHttpRequestFactory interceptingClientHttpRequestFactory =
                    new InterceptingClientHttpRequestFactory(factory, INTERCEPTORS);
            restTemplateHttps.setRequestFactory(interceptingClientHttpRequestFactory);
        }
        return restTemplateHttps;
    }

//    @PostConstruct
//    private void initRestTemplates() {
//        log.info("Initialising restTemplateHttp...");
//        // COnfigure RestTemplate instance for HTTP
//        restTemplateHttp.setInterceptors(INTERCEPTORS);
//
//        // Configure RestTemplate instance for HTTPS
//        if (restTemplateHttp.getRequestFactory() instanceof CommonsClientHttpRequestFactory)
//        {
//            CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplateHttp.getRequestFactory();
//            this.client = factory.getHttpClient();
//            try {
//                Protocol authhttps = new Protocol(HTTPS, new EasySSLProtocolSocketFactory(), HTTPS_PORT);
//                Protocol.registerProtocol(HTTPS, authhttps);
//                client.getHostConfiguration().setHost(HTTPS_HOST, HTTPS_PORT, authhttps);
//            } catch (GeneralSecurityException e) {
//                log.error("Couldn't instantiate EasySSLProtocolSocketFactory. GeneralSecurityException", e);
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                log.error("Couldn't instantiate EasySSLProtocolSocketFactory. IOException", e);
//                throw new RuntimeException(e);
//            }
//            // Add interceptor which adds the correlation context header.
//            InterceptingClientHttpRequestFactory interceptingClientHttpRequestFactory =
//                    new InterceptingClientHttpRequestFactory(factory, INTERCEPTORS);
//            restTemplateHttp.setRequestFactory(interceptingClientHttpRequestFactory);
//        } else {
//            log.info("restTemplateHttp: " + restTemplateHttp.getClass());
//            log.info("- RequestFactory: " + restTemplateHttp.getRequestFactory().getClass());
//            log.warn("Could not configure RestTemplate for SSL.");
//        }
//        
//    }



    // ==================================================
    // Common Exception Handling
    // ==================================================

    /**
     * Handles all errors caused by REST web service calls.
     * 
     * @param rce exception raised from {@link RestTemplate} call
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity handleRestClientException(
            final RestClientException rce, final HttpServletRequest req) 
            throws SAXException, ParserConfigurationException, IOException {
        HttpStatus httpStatus;
        int errorCode;
        String message;
        String technicalMessage;
        String moreInfo;
        String correlationContext = CorrelationContextParameterHolder.get();

        logRequest(req, "REST web service call failed.", rce);

        RestServiceFaultAssessment serviceFaultAssessment = new RestServiceFaultAssessment(rce);
        httpStatus = serviceFaultAssessment.getHttpStatus();
        errorCode = serviceFaultAssessment.getIBMCocaErrorCode();
        message = serviceFaultAssessment.getIBMCocaMessage();
        technicalMessage = serviceFaultAssessment.getIBMCocaTechnicalMessage();
        moreInfo = serviceFaultAssessment.getMoreInfo();

        return ErrorResponseGenerator.createErrorResponse(log, httpStatus, errorCode, message, technicalMessage, moreInfo, correlationContext);
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(
            final RuntimeException re, final HttpServletRequest req) {
        HttpStatus httpStatus;
        int errorCode;
        String message;
        String technicalMessage = null;
        String moreInfo = null;
        String correlationContext = CorrelationContextParameterHolder.get();

        if (re instanceof IllegalArgumentException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = re.getMessage();
            errorCode = ErrorCodes.TECHNICAL_ILLEGAL_ARGUMENT;
        } else if (re instanceof HttpMessageNotReadableException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            errorCode = ErrorCodes.TECHNICAL_ILLEGAL_ARGUMENT;
            message = "Illegal argument used in request ";
            technicalMessage = re.getMessage();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode = ErrorCodes.TECHNICAL_RUNTIME_ERROR;
            message = "A runtime error occured during this service request.";
        }
        logRequest(req, "RuntimeException occured.", re);
        @SuppressWarnings("rawtypes")
        ResponseEntity responseEntity = ErrorResponseGenerator.createErrorResponse(
            log, httpStatus, errorCode, message, technicalMessage, moreInfo, correlationContext); 
        return responseEntity;
    }

    private void logRequest(final HttpServletRequest req, final String msg, final Throwable t) {
        log.error(msg, t);
        StringBuilder sb = new StringBuilder("Service was called with: ");
        sb.append(req.getMethod()).append(" ");
        sb.append(req.getRequestURI());
        if (req.getQueryString() != null) {
            sb.append("?").append(req.getQueryString());
        }
        log.error(sb.toString());
    }
	
}
