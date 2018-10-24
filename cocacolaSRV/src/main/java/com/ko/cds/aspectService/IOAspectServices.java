package com.ko.cds.aspectService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.glassfish.jersey.message.internal.OutboundJaxrsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ko.cds.TimerTask.BunchBallActivityTask;
import com.ko.cds.TimerTask.JanrainTimerTask;
import com.ko.cds.TimerTask.JanrainTimerTaskForMemberUpdate;
import com.ko.cds.TimerTask.JanrainTimerTaskForOpts;
import com.ko.cds.TimerTask.JanrainTimerTaskForPostOpts;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.report.metrics.MetricsListener;
import com.ko.cds.report.metrics.MetricsLogger;
import com.ko.cds.service.errorLog.ErrorLogService;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;


/**
 * 
 * @author IBM
 *
 */


@Aspect
public class IOAspectServices {
	
	 	private static SimpleDateFormat sdf;
		
	
		//Timer timer = null;
		//Timer.Context   timerContext =null;
	    private static final Logger LOG = LoggerFactory.getLogger(IOAspectServices.class);
	    private String startTime = "" ;
	    private String endTime = "";
	    private String executionTime = "";
	    private Date StDate;
	    private Date endDate;
	    private Map<String,Timer> matricTimeMapper = new HashMap<String,Timer>();
	   
	  
	   
	    static{
	    	
	    	JanrainTimerTask timerTask = new JanrainTimerTask();
	    	JanrainTimerTaskForMemberUpdate timerTaskMemberUpdate = new JanrainTimerTaskForMemberUpdate();
	    	JanrainTimerTaskForOpts timerTaskOpts = new JanrainTimerTaskForOpts();
	    	JanrainTimerTaskForPostOpts timerTaskPostOpts = new JanrainTimerTaskForPostOpts();
	    	BunchBallActivityTask bunchBallActivityTask = new BunchBallActivityTask();
	    	
	        //running timer task as daemon thread
	    	java.util.Timer timer = new java.util.Timer(true);
	        timer.scheduleAtFixedRate(timerTask, 0, 10000);
	        timer.scheduleAtFixedRate(timerTaskMemberUpdate,0,10000);
	        timer.scheduleAtFixedRate(timerTaskOpts,0,10000);
	        timer.scheduleAtFixedRate(timerTaskPostOpts,0,10000);
	        timer.scheduleAtFixedRate(bunchBallActivityTask, 0,10000);
	    	
			sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	    	TimeZone utc = TimeZone.getTimeZone("UTC");
			sdf.setTimeZone(utc);
		}
	    
	    Timer.Context memberInfoContext=null;
	   
	    @Pointcut("execution(public * com.ko.cds.service.*.*.*(..)) &&  @annotation(com.ko.cds.customAnnotation.AopServiceAnnotation)")
	    public void serviceMethods() {
	        //
	    }

	    @Before("serviceMethods()")
	    public void beforeMethod(final JoinPoint joinpoint) {
	    	//timer = MetricsListener.REGISTRY.timer(joinpoint.getSignature().getDeclaringTypeName()+"."+joinpoint.getSignature().getName());
    		//timerContext = timer.time();
	    	Timer serviceTimer;
	    	if(matricTimeMapper.get(joinpoint.getSignature())!=null)
	    	{
	    		serviceTimer = matricTimeMapper.get(joinpoint.getSignature().getName());
	    	}
	    	else
	    	{
	    		serviceTimer = MetricsListener.REGISTRY.timer(MetricRegistry.name(joinpoint.getTarget().getClass(), "Response-Time",joinpoint.getSignature().getName()));
	    		matricTimeMapper.put(joinpoint.getSignature().getName(), serviceTimer);
	    	}
	    	memberInfoContext = serviceTimer.time();
	    	Date date = new Date();
	    	StDate=date;
	    	/*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
	    	TimeZone utc = TimeZone.getTimeZone("UTC");
			sdf.setTimeZone(utc);*/
	    	String formattedDate = sdf.format(date);
	    	startTime = formattedDate;
	        for (int i = 0; i < joinpoint.getArgs().length; i++) {
	            Object object = joinpoint.getArgs()[i];
	            
	                LOG.info("Calling service {} with following parameters {}", joinpoint.getSignature(),  object);  
	                LOG.info("Start Time :"+formattedDate);   
	        }
	    }

	    @Transactional
	    private String[] insertErrorLog(Object[] args, Throwable ThrObject, String errorCode, String methodName)
	    {
	      String[] errlogArgsList = new String[4];
	      try
	      {
	        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
	        ErrorLogService errorLogService = (ErrorLogService)ctx.getBean("errorLogService", ErrorLogService.class);
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        errlogArgsList[0] = methodName;
	        errlogArgsList[1] = ThrObject.toString();
	        errlogArgsList[2] = errorCode;
	        
	        Object json = objectMapper.readValue(objectMapper.writeValueAsString(args[0]), Object.class);
	        
	        errlogArgsList[3] = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
	        
	        errlogArgsList[3] = errlogArgsList[3].replace("\n", "").replace("\r", "");
	        errlogArgsList[3] = errlogArgsList[3].replaceAll("\\r\\n|\\r|\\n", " ");
	        errlogArgsList[3] = (errlogArgsList[3].substring(0, errlogArgsList[3].length() / 4) + "}");
	        
	        errorLogService.inserErrorInDB(errlogArgsList);
	      }
	      catch (Exception exx)
	      {
	        exx.printStackTrace();
	      }
	      return errlogArgsList;
	    }
	    
	    private void WriteInFile(String[] errorLog)
	    {
	      //System.out.println("------------------------Creating file ---- ERROR FOUND-------------------------------------");
	      try
	      {
	        BufferedWriter bw = new BufferedWriter(new FileWriter("/data/mailErrorLog/errorLog.txt", true));
	        //System.out.println("File created ------------");
	        StringBuffer errorLogSB = new StringBuffer();
	        errorLogSB.append("Error/Exception \n");
	        errorLogSB.append("========================= \n");
	        errorLogSB.append("API Name : " + errorLog[0] + "\n" + " Error/Exception : " + errorLog[1] + "\n" + " Error Code : " + errorLog[2] + "\n" + " Error Msg " + errorLog[3]);
	        //System.out.println("Error Str " + errorLogSB.toString());
	        
	        bw.write(errorLogSB.toString() + "\n");
	        bw.newLine();
	        bw.flush();
	        bw.close();
	      }
	      catch (Exception exx)
	      {
	        //System.out.println("------------------------Not able to Creating file ---- ERROR FOUND-------------------------------------");
	        exx.printStackTrace();
	      }
	    }
	    
	    @Around("serviceMethods()")
	    public Object aroundMethod(final ProceedingJoinPoint joinpoint) throws BadRequestException,CustomAdminException {
	        try {
	            long start = System.nanoTime();
	            Object result = joinpoint.proceed();
	            long end = System.nanoTime();
	            long elapsedTime = end - start;
	            double seconds = (double) elapsedTime / 1000000000.0;
	            DecimalFormat df = new DecimalFormat("#.########"); //changes for the bunchball ones
	            executionTime =String.valueOf(df.format(seconds));
	            LOG.info("Execution Time for this service method: "+joinpoint.getSignature()+" => "+executionTime);   
	            
	           //  joinpoint.getArgs()
	            return result;
	        } catch (RuntimeException e) {
	            Object[] args = joinpoint.getArgs();
	            String[] arrorLog = insertErrorLog(args, e, "5002", joinpoint.getSignature().getName());
	        	executionTime = null;
	        	WriteInFile(arrorLog);
	        	throw e;
	        	//throw new BadRequestException(e.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT);
	        } catch (BadRequestException e) {
	        	Object[] args = joinpoint.getArgs();
	            String[] arrorLog = insertErrorLog(args, e, "5001", joinpoint.getSignature().getName());
	        	executionTime= null;
	        	WriteInFile(arrorLog);
	            throw e;
	        } catch (CustomAdminException e) {
	        	Object[] args = joinpoint.getArgs();
	            String[] arrorLog = insertErrorLog(args, e, "5004", joinpoint.getSignature().getName());
	        	executionTime= null;
	        	WriteInFile(arrorLog);
	            throw e;
	        }  catch (Throwable e) {
	            // TODO Exception thrown by wrapped method MUST NOT be caught or wrapped here!
	        	Object[] args = joinpoint.getArgs();
	            String[] arrorLog = insertErrorLog(args, e, "5003", joinpoint.getSignature().getName());
	        	executionTime = null;
	        	WriteInFile(arrorLog);
	            throw new RuntimeException(e);
	        }
	    }

	    
	    @AfterReturning(pointcut="serviceMethods()", returning = "result")
	    @Order(2)
	    public void logAfter(JoinPoint joinPoint, Object result)
	    {
	    	String sessionId = null;
	    	StringBuffer sb = new StringBuffer();
	    	RequestAttributes attribs = RequestContextHolder.currentRequestAttributes();
	    	if(attribs != null)
	    		sessionId=(String)attribs.getAttribute(CDSConstants.SESSION_ID_KEY, RequestAttributes.SCOPE_REQUEST) ;
	    	
	    	sb.append(" Session ID :");
			sb.append(sessionId);
			    	
	    	sb.append(" API Name :");
	    	sb.append(joinPoint.getSignature().getName());
	    	String responseMarker = sb.toString();
	    	
	    	/**
	    	ExecutorService es = Executors.newFixedThreadPool(1);
	        @SuppressWarnings("unchecked")
			final Future future = es.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	auditTrailLogUpdate(logUpdateObject,responseMarker);
	                        return null;
	                    }
	                });
	        es.shutdown();
	        */
	    	//auditTrailLogUpdate(result,responseMarker);
	    }

		/*private void auditTrailLogUpdate(Object result, String responseMarker) {
			Logger auditTrail = LoggerFactory.getLogger("auditTrail");
	    	
	    	
	    	try{
		    	ObjectMapper objectMapper = new ObjectMapper();
		    	objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		    	StringWriter responseText = new StringWriter();
		        objectMapper.writeValue(responseText, (((OutboundJaxrsResponse)result).getEntity()));
		        auditTrail.info(responseMarker +"Response is :"+responseText.toString());
	    	}
	    	catch(Exception exx)
	    	{
	    		//exx.printStackTrace();
	    		LOG.error("auditTrailLogUpdate :"+exx);
	    	}
		} 
	    */
	    
	    
	   @After("serviceMethods()")
	    public void afterMethod(JoinPoint joinpoint) throws IOException, NumberFormatException, WebApplicationException {
		    Date date = new Date();
		    endDate = date;
	    	
	    	final String formattedDate = sdf.format(date);
	        MethodSignature signature = (MethodSignature) joinpoint.getSignature();
	       
	        //updateMetricsTime(formattedDate);
	       
	       
	       /*ExecutorService pool = Executors.newFixedThreadPool(1);
	    	 @SuppressWarnings("unchecked")
	 		 final Future future = pool.submit(new Callable() {
	 	                public Object call() throws Exception {
	 	                	System.out.println("callling writeCSVData 1");
	 	                	updateMetricsTime(formattedDate);
	 	                    return null;
	 	     }
	 	     });
	    	 pool.shutdown();*/
	    	 updateMetricsTime(formattedDate,joinpoint.getSignature().getName());
	      
	    }

	private void updateMetricsTime(String formattedDate,String apiName) {
		
		
		endTime = formattedDate;
	      //stopping the Mertics timer for each of the request.
	      memberInfoContext.stop();
	      
	      //logging the Execution time incase of error and success.
	      if(null != executionTime && executionTime.length() > 0){
	    	  //LOG.info("Logging Metric Values for the Successfull Rest API execution");
		    	// MetricsLogger.logTimerInfo(startTime,endTime,executionTime,CDSConstants.REQUEST_STATUS_SUCCESS);
	    	  	 CDSOUtils.metricsLoggerTaskList.add(new MetricsLogger(startTime,endTime,executionTime,CDSConstants.REQUEST_STATUS_SUCCESS,apiName));
		     }else{
		    	 //LOG.info("Exception Occured while Procesig the Request, Hence its logged in Metrics with the error response Time.");
		    	  long errorProcessingTime = (endDate.getTime() - StDate.getTime())/1000;
		    	  executionTime = String.valueOf(errorProcessingTime);
		    	  CDSOUtils.metricsLoggerTaskList.add(new MetricsLogger(startTime,endTime,executionTime,CDSConstants.REQUEST_STATUS_ERROR,apiName));
		    	  
		    	 //new MetricsLogger(startTime,endTime,executionTime,CDSConstants.REQUEST_STATUS_ERROR);
		     }
	}
	   
	  
}
