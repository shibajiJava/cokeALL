package com.ibm.app.services.ws;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import com.ibm.app.services.exception.ServiceException;

public abstract class AbstractWebServiceImpl {
	
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
	    protected RestTemplate restTemplateHttps;

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
	                //factory.getHttpClient().getConnectionManager().
	               // Httpme
	               /* URI uri = new URI("");
	                ClientHttpRequest clientHttpRequest=   factory.createRequest(uri, HttpMethod.POST);
	                clientHttpRequest. getHostConfiguration().setHost("localhost", 443, authhttps);*/
	            } catch (final GeneralSecurityException e) {
	                log.error("Couldn't instantiate EasySSLProtocolSocketFactory. GeneralSecurityException", e);
	                throw new RuntimeException(e);
	            } catch (final IOException e) {
	                log.error("Couldn't instantiate EasySSLProtocolSocketFactory. IOException", e);
	                throw new RuntimeException(e);
	            }
	            // Add interceptor which adds the correlation context header.
	            final InterceptingClientHttpRequestFactory interceptingClientHttpRequestFactory =
	                    new InterceptingClientHttpRequestFactory(factory, INTERCEPTORS);
	            restTemplateHttps.setRequestFactory(interceptingClientHttpRequestFactory);
	        }
	        return restTemplateHttps;
	    }

	    

	    /**
	     * service exception handler
	     * @param se
	     * @return
	     */
	    @SuppressWarnings("rawtypes")
	    @ExceptionHandler(ServiceException.class)
	    public ResponseEntity handleServiceException(final ServiceException se) {
	        if (!se.isClientInduced()) {
	            log.error("Server based ServiceException occured: " + se.getMessage(), se);
	        }
	        return ErrorResponseGenerator.createErrorResponse(log, se);
	    }

	    private String getFirstLineStackTrace(final Throwable t) {
	        if (t.getStackTrace() != null && t.getStackTrace().length > 0
	                && t.getStackTrace()[0] != null) {
	            return t.getStackTrace()[0].toString();
	        }
	        return null;
	    }

	    @SuppressWarnings("rawtypes")
	    protected ResponseEntity createResponseEntityOnError(
	            final HttpStatus statusCode, final int errorCode,
	            final String message, final String technicalMessage,
	            final String moreInfo) {
	        return ErrorResponseGenerator.createErrorResponse(
	                log, statusCode, errorCode, message, technicalMessage, moreInfo, null);
	    }

}
