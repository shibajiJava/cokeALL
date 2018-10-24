package com.ibm.app.services.ws;


import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import com.ibm.app.services.ws.HttpHeaders;

public class OutgoingSecuritySpringInterceptor implements ClientHttpRequestInterceptor{

	
	private static ThreadLocal<String> authenticationParameter = new ThreadLocal<String>();
	public static ThreadLocal<String> getAuthenticationParameter() {
		return authenticationParameter;
	}

	public static void setAuthenticationParameter(
			ThreadLocal<String> authenticationParameter) {
		OutgoingSecuritySpringInterceptor.authenticationParameter = authenticationParameter;
	}

	/* (non-Javadoc)
     * @see org.springframework.http.client.ClientHttpRequestInterceptor#intercept(org.springframework.http.HttpRequest, byte[], org.springframework.http.client.ClientHttpRequestExecution)
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
        wrapper = addToRequestHeaders(wrapper);
        return execution.execute(wrapper, body);
    }

    /**
     * Adds the received authentication parameter data (SAML token) to the HTTP
     * request headers of an outgoing service call.
     * 
     * @param req HTTP request with headers
     * @return
     */
    HttpRequestWrapper addToRequestHeaders(HttpRequestWrapper req) {
        org.springframework.http.HttpHeaders headers = req.getHeaders();
        //if authentication required 
        /*
        String authParams = AuthenticationParameterHolder.get();
        if (authParams != null) {
            headers.add(HttpHeaders.AUTHORIZATION, authParams);
        }*/
        return req;
    }
    
}
