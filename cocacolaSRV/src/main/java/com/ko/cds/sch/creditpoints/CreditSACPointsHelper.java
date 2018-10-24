package com.ko.cds.sch.creditpoints;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.etlstagging.EtlStaggingLoadDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.etlstagging.EtlLoadTracker;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.points.TransactionHistory;
import com.ko.cds.request.points.CreditPointRequest;
import com.ko.cds.service.helper.PointAccountServiceHelper;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;

@Component
public class CreditSACPointsHelper {
	private static final Logger logger = LoggerFactory
			.getLogger("creditSACPointsTask");
	
	private static final Logger logger1 = LoggerFactory
			.getLogger("creditSACPointsFailures");
	
	@Autowired
	EtlStaggingLoadDAO etlStaggingLoadDAO;
	
	@Autowired
	PointAccountDAO pointAccountDAO;
	
	@Autowired
	PointAccountServiceHelper helper;
	
	@Transactional
	public  void execute(){
		//fetching the credit request form the process table for processing.
		List<EtlLoadTracker> etlLoadTrackers = etlStaggingLoadDAO.getETLLoadStatus();
		
		if (etlLoadTrackers.size()<=0) {
			logger.error("Please check CDSOSTG.ETL_LOAD_TRACKER table,There are no records in this table");
			return;
		}
		
		for (EtlLoadTracker etlLoadTracker : etlLoadTrackers) {
				if (etlLoadTracker.getStatus().equals(CDSConstants.SAC_PROCESS_LOAD_SATRT_STATUS)) {
					List<TransactionHistory> sacTransactionHistories = new ArrayList<TransactionHistory>();
					
					if(etlLoadTracker.getTargetTable().equals(CDSConstants.SAC_PROCESS_TARGET_TABLE)){
						sacTransactionHistories = etlStaggingLoadDAO.getSACHistory();
					}else if(etlLoadTracker.getTargetTable().equals(CDSConstants.SAVSTR_PROCESS_TARGET_TABLE)){
						sacTransactionHistories = etlStaggingLoadDAO.getSavingStarHistory();
					}
					BigInteger trans_id = null;
					for (TransactionHistory sacHistory : sacTransactionHistories) {
						try {
							CreditPointRequest creditRequest = new CreditPointRequest();
							creditRequest.setAccountType(sacHistory.getAccountType());
							creditRequest.setBrandId(sacHistory.getBrandId());
							creditRequest.setCreditType(sacHistory.getPointsType());
							creditRequest.setMemberId(sacHistory.getMemberId());
							creditRequest.setPointsQuantity(sacHistory.getPointsQuanity());
							creditRequest.setPointsSource(sacHistory.getPointsSource());
							creditRequest.setProductId(sacHistory.getProductID());
							creditRequest.setPromotion(sacHistory.getPromotionId());
							creditRequest.setReasonCode(sacHistory.getReasonCode());
							creditRequest.setStakeHolder(sacHistory.getStakeHolderId());
							if(etlLoadTracker.getTargetTable().equals(CDSConstants.SAVSTR_PROCESS_TARGET_TABLE)){
								creditRequest.setClientTransactionId(sacHistory.getClientTransId());
							}
							//calling credit API for Credit Action.
							//trans_id = helper.creditPoint(creditRequest);
							callCreditAPIviaAPIManager(creditRequest.toJson());
						} catch (Exception e) {
							if(e.getMessage().length()>900){
								sacHistory.setErrorDetail(e.getMessage().substring(0, 900));
							}else{
								sacHistory.setErrorDetail(e.getMessage());
							}
							logger1.error("ERROR while processing the request for "+etlLoadTracker.getTargetTable()+" =>"+sacHistory.getSACAttributes() + " ERROR Message =>"+e.getMessage());
							pointAccountDAO.logCDSACreditProcessDetails(sacHistory);
							continue;
						}
					}
					logger.info("Number of "+etlLoadTracker.getTargetTable()+" Credit Request processed is "+sacTransactionHistories.size());
					
					//update the API Read status in ETL Load Tracker table
					EtlLoadTracker sacLoadStatus = new EtlLoadTracker();
					sacLoadStatus.setTargetTable(etlLoadTracker.getTargetTable());
					sacLoadStatus.setStatus(CDSConstants.SAC_PROCESS_LOAD_FINISH_STATUS);
					etlStaggingLoadDAO.updateETLLoadStatus(sacLoadStatus);
					logger.info("ETL Load Tracker Table with API Read status "+CDSConstants.SAC_PROCESS_LOAD_FINISH_STATUS +" For the table "+ etlLoadTracker.getTargetTable());
					
				}else{
					logger.error("ETL Load is not Completed yet for the table "+etlLoadTracker.getTargetTable());
				}
		}
			
	}

	public static String callCreditAPIviaAPIManager (String json) throws IOException, BadRequestException{

		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		
			String env= configProp.get("sac_Env");
            StringBuilder apiManagerendPointURL=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            apiManagerendPointURL.append(configProp.get("creditURL"+env));
           
            HttpPost httppost = new HttpPost(apiManagerendPointURL.toString());
            if(env.equals("Test")|| env.equals("Prod")){
                 	String encoded = DatatypeConverter.printBase64Binary((configProp.get("credential"+env)).getBytes("UTF-8"));
                     httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                 }
                StringEntity input = new StringEntity(json);
        		input.setContentType("application/json");
        		httppost.setEntity(input);
                // Execute HTTP Post Request    
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
            	String responseString = EntityUtils.toString(entity, "UTF-8");
                if(response.getStatusLine().getStatusCode()==200)
                {
                    logger1.info("SUCCESS While Executing the Request for json =>"+ json);
                    return responseString;
                }
                else
                {
                    throw new BadRequestException(responseString);
                }
		
	}
}
