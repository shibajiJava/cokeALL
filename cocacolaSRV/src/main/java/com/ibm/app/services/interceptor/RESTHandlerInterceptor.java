package com.ibm.app.services.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RESTHandlerInterceptor extends HandlerInterceptorAdapter {
	//private static final Logger LOG = LoggerFactory.getLogger(RESTHandlerInterceptor.class);

    /***
     * Executes after completion of
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        try {
            super.afterCompletion(request, response, handler, ex);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            //LOG.error(e.getMessage());
        }
    }

}
