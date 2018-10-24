package com.ko.cds.service.activity;

import java.math.BigInteger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.request.activity.BunchBallActivityRequest;
import com.ko.cds.request.activity.RecordRedemptionRequest;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.response.record.RecordRedemptionResponse;
import com.ko.cds.service.helper.ActivityServiceHelper;
import com.ko.cds.service.helper.BunchBallActivityAsynService;
import com.ko.cds.utils.CDSOUtils;

/**
 * Service implementation class.
 * 
 * @author IBM
 * 
 */
@Component
@Transactional
public class ActivityService implements IActivityService {

	private static final Logger logger = LoggerFactory
			.getLogger(ActivityService.class);
	@Autowired
	private ActivityDAO activityDAO;
	@Autowired
	private ActivityServiceHelper serviceHelper;

	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response recordSessionInfo(
			RecordSessionInfoRequest recordSessionInfoRequest)
			throws BadRequestException {
		logger.info("Into ActivityService : recordSessionInfo method");
		return serviceHelper.recordSessionInfo(recordSessionInfoRequest);
	}

	@Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response bunchBall(final BunchBallActivityRequest bunchBallActivityRequest)
			throws BadRequestException {
		/* ExecutorService es = Executors.newFixedThreadPool(1);
		 @SuppressWarnings("unchecked")
			final Future future = es.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	serviceHelper.bunchBallActivity(bunchBallActivityRequest);
	                    	
	                        return null;
	                    }
	                });
		 es.shutdown();*/
		
		 CDSOUtils.BunchBallActivityAsynList.add(new BunchBallActivityAsynService(bunchBallActivityRequest));
		return CDSOUtils.createOKResponse(null);
	}
	

	@AopServiceAnnotation
	@Override
	public Response recordRedemption(
			RecordRedemptionRequest recordRedemptionRequest)
			throws BadRequestException {
		RecordRedemptionResponse recordRedemptionResponse=new RecordRedemptionResponse(); 
		
		BigInteger transactionId=serviceHelper.InsertRecordRedemptionDetails(recordRedemptionRequest);
		recordRedemptionResponse.setTransactionId(transactionId);
		
		return CDSOUtils.createOKResponse(recordRedemptionResponse);
	}
	
	
	

}
