package com.ko.cds.filter.mockResponse;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ko.cds.jmxImpl.JmxConfigurationProperty;
import com.ko.cds.utils.CDSConstants;

public class  MockResponseFilter implements Filter {

		FilterConfig filterConfig = null;
		
		public static Logger logger = LoggerFactory.getLogger(MockResponseFilter.class);
		
		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			this.filterConfig = filterConfig;
		}
		
		@Override
		public void destroy() {
		}
		
		@Override
		public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
				throws IOException, ServletException {
			
			final CdsRequestWrapper CdsRequestWrapper = new CdsRequestWrapper((HttpServletRequest) servletRequest);

			final ServletRequest updateLogequest = servletRequest;
			
			/*ExecutorService es = Executors.newFixedThreadPool(1);
	        @SuppressWarnings("unchecked")
			final Future future = es.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateAuditTrialLog(updateLogequest, CdsRequestWrapper);
	                        return null;
	                    }
	                });
	        es.shutdown();*/
			updateAuditTrialLog(updateLogequest, CdsRequestWrapper);
			//if Its not Production environment the Apply the Filter criteria.
			if(JmxConfigurationProperty.getIsDevelopment().equalsIgnoreCase(CDSConstants.MOCK_RESPONSE_VALUE)){
				String mockResponseFlag = servletRequest.getParameter(CDSConstants.MOCK_RESPONSE_STRING);
				if(null != mockResponseFlag && mockResponseFlag.equalsIgnoreCase(CDSConstants.MOCK_RESPONSE_VALUE)){
					//reply with Mock Response Else Leave.
					logger.info(" Mock Response Flag is TRUE ");
					HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
					HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
					String requestURI = httpRequest.getRequestURI();
					requestURI= requestURI.replaceAll("/", "_");
					String apiName = httpRequest.getMethod().toLowerCase()+requestURI.substring(requestURI.lastIndexOf("v1")+2);
					apiName = apiName.replaceAll("__", "_");
					RequestDispatcher rd = httpRequest.getRequestDispatcher("mockResponse?apiServiceName="+apiName);
					rd.forward(httpRequest, httpResponse);
					return;
				}else{
					logger.info("No Filteration Required.");
				}
			}else{
				logger.info("Its a Production Environment");
			}
			filterChain.doFilter(CdsRequestWrapper, servletResponse);
		}
		
		
		private void updateAuditTrialLog(ServletRequest servletRequest,
				CdsRequestWrapper CdsRequestWrapper) {
			Logger auditTrail = LoggerFactory.getLogger("auditTrail");
			String sessionId=((HttpServletRequest)servletRequest).getSession().getId();
			StringBuilder sb = new StringBuilder();
			sb.append("Call from IP : "+CdsRequestWrapper.getRemoteHost()+" Port "+servletRequest.getRemotePort()+" Session ID :"+sessionId+ " Request URI:"+CdsRequestWrapper.getRequestURI() +" at :"+new java.util.Date());
			sb.append("\n "+"Request is : \n");
			if("POST".equalsIgnoreCase(CdsRequestWrapper.getMethod())){
				sb.append(CdsRequestWrapper.getBody());
			}else if("GET".equalsIgnoreCase(CdsRequestWrapper.getMethod())){
				sb.append(CdsRequestWrapper.getQueryString());
			}
			servletRequest.setAttribute(CDSConstants.SESSION_ID_KEY, sessionId);
			//System.out.println("===>> "+sb);
			auditTrail.info(sb.toString());
		}
	}