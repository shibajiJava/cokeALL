package com.ibm.app.services.aspectServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.ko.cds.report.metrics.MetricsLogger;


/**
 * 
 * @author IBM
 *
 */

@Aspect
public class IOAspectServices {
		//Timer timer = null;
		//Timer.Context   timerContext =null;
	    private static final Logger LOG = LoggerFactory.getLogger(IOAspectServices.class);
	    private String startTime ;
	    private String endTime ;
	    private String executionTime ;
	   
	    @Pointcut("execution(public * com.ko.cds.service.*.*.*(..)) &&  @annotation(com.ko.cds.customAnnotation.AopServiceAnnotation)")
	    public void serviceMethods() {
	        //
	    }

	    @Before("serviceMethods()")
	    public void beforeMethod(final JoinPoint joinpoint) {
	    	//Setting the times to null;
	    	executionTime = "";
	    	endTime ="";
	    	startTime = "";
	    	Date date = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	    	String formattedDate = sdf.format(date);
	    	startTime = formattedDate;
	        for (int i = 0; i < joinpoint.getArgs().length; i++) {
	            Object object = joinpoint.getArgs()[i];
	            
	                LOG.info("Calling service {} with following parameters {}", joinpoint.getSignature(),  object);  
	                LOG.info("Start Time :"+formattedDate);   
	            
	        }
	    }

	    @Around("serviceMethods()")
	    public Object aroundMethod(final ProceedingJoinPoint joinpoint) {
	        try {
	            long start = System.nanoTime();
	            Object result = joinpoint.proceed();
	            long end = System.nanoTime();
	            long elapsedTime = end - start;
	            double seconds = (double) elapsedTime / 1000000000.0;
	            MethodSignature signature = (MethodSignature) joinpoint.getSignature();
	            if (signature.getReturnType().isAssignableFrom(ResponseEntity.class) && result != null) {
	                ResponseEntity<?> entity = (ResponseEntity<?>) result;
	                LOG.info(String.format("%s took %s s with Result Code %d", joinpoint.getSignature(), String.valueOf(seconds), entity.getStatusCode().value()));
	                if (signature.toShortString().contains("ReportExecService.getFile")) {
	                    if (entity.getHeaders().containsKey("Content-Disposition")) {
	                        LOG.info("{} downloaded {}", joinpoint.getSignature(), entity.getHeaders().getFirst("Content-Disposition"));
	                    }
	                }
	            } else {
	                LOG.info(String.format("%s Time taken for this Service   %s s", joinpoint.getSignature(), String.valueOf(seconds)));
	                executionTime =String.valueOf(seconds);
	            }
	            return result;
	        } catch (RuntimeException e) {
	            throw e;
	        } catch (Throwable e) {
	            // TODO Exception thrown by wrapped method MUST NOT be caught or wrapped here!
	            throw new RuntimeException(e);
	        }
	    }

	   @After("serviceMethods()")
	    public void afterMethod(JoinPoint joinpoint) throws IOException, NumberFormatException, WebApplicationException {
		    Date date = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	    	String formattedDate = sdf.format(date);
	        MethodSignature signature = (MethodSignature) joinpoint.getSignature();
	        if (signature.toShortString().contains("ReportExecService.getFile"))
	        {
	            for (int i = 0; i < joinpoint.getArgs().length; i++) {
	                Object object = joinpoint.getArgs()[i];
	                if (HttpServletResponse.class.isInstance(object)){
	                    HttpServletResponse response = (HttpServletResponse) object;
	                    if (response.containsHeader("Content-Disposition"))
	                    LOG.info("{} downloaded artifactID {}", joinpoint.getSignature(), "Test");                
	                }
	            }
	        }
	        LOG.info("Ending time : "+formattedDate);
	      endTime = formattedDate;
	    	 MetricsLogger.logTimerInfo(startTime,endTime,executionTime,"");
	    }
}
