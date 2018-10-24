package com.ibm.app.services.ws;

/**
 * 
 */

import static com.ibm.app.services.ws.HttpHeaders.IBM_CORRELATION_CONTEXT;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;


public class CorrelationContextSpringInterceptor implements ClientHttpRequestInterceptor{
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
     * Adds the IBM correlation context to HTTP request headers of an outgoing 
     * service call.
     * 
     * @param req HTTP request with headers
     * @return Augmented HTTP request
     */
    private HttpRequestWrapper addToRequestHeaders(HttpRequestWrapper req) {
        HttpHeaders headers = req.getHeaders();
        String value = CorrelationContextParameterHolder.get();
        if (value != null) {
            headers.add(IBM_CORRELATION_CONTEXT, value);
        }
        return req;
    }
}
