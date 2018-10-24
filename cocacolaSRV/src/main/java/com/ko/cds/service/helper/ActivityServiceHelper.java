package com.ko.cds.service.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;

import com.ko.cds.dao.activity.ActivityDAO;
import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.activity.ActivityTrans;
import com.ko.cds.pojo.activity.RedemptionTrans;
import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.request.activity.BunchBallActivityRequest;
import com.ko.cds.request.activity.RecordRedemptionRequest;
import com.ko.cds.request.activity.RecordSessionInfoRequest;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

/**
 * Service Helper class for Activity APIs.
 * @author IBM
 *
 */
@Component
public class ActivityServiceHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(ActivityServiceHelper.class);
	
	@Autowired
	private ActivityDAO activityDAO;
	@Autowired
	private ICacheService cacheService;
	
	
	
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO;
	
	public Response recordSessionInfo(
			RecordSessionInfoRequest recordSessionInfoRequest) throws BadRequestException {		
		int resp = 0;
		Response response = null;
		//Validating the request
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(recordSessionInfoRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("recordSessionInfo : "+e);
			throw new WebApplicationException(e);
		}
		
	    if(validationErrors != null){
	    	throw new BadRequestException(validationErrors,
					ErrorCode.GEN_INVALID_ARGUMENT);
	    }		

		try {
			long start = System.nanoTime();
			resp = activityDAO.recordSessionInfo(recordSessionInfoRequest);
			logger.info("No. of records inserted : " + resp);
			activityDAO.recordMemberSite(recordSessionInfoRequest);
			
		} catch(BadSqlGrammarException rex){
			logger.error("recordSessionInfo :", rex);	
			throw new BadRequestException(rex.getMessage(), ErrorCode.GEN_INVALID_ARGUMENT);
		}catch(DataIntegrityViolationException e){
			logger.error("recordSessionInfo :",e);
			if(e.getMessage() != null && e.getMessage().contains("SQLCODE=-803")){
				logger.error("Duplicate Session UUID Found for the Memebr Id "+recordSessionInfoRequest.getMemberId()+" and session UUID "+recordSessionInfoRequest.getSessionUUID());
				logger.info("Duplicate Session UUID Found for the Memebr Id "+recordSessionInfoRequest.getMemberId()+" and session UUID "+recordSessionInfoRequest.getSessionUUID());
				throw new BadRequestException("Duplicate Session UUID Found", ErrorCode.DUPLICATE_SESSION_UUID_FOUND);
			}else if(e.getMessage() != null && e.getMessage().contains("SQLCODE=-530")){
				BigInteger siteID = activityDAO.getSiteId(new BigInteger(recordSessionInfoRequest.getSiteId().toString()));
				if(null!=siteID)
				{
					throw new BadRequestException("THE INSERT VALUE OF FOREIGN KEY IS INVALID FOR MEMBER_ID", ErrorCode.GEN_INVALID_ARGUMENT);
				}
				else 
				{
					BigInteger memberId = activityDAO.getMemberId(new BigInteger(recordSessionInfoRequest.getMemberId().toString()));
					if(null!=memberId)
					{
						throw new BadRequestException("THE INSERT VALUE OF FOREIGN KEY IS INVALID FOR SITE_ID", ErrorCode.GEN_INVALID_ARGUMENT);
					}
					else
					{
						throw new BadRequestException("THE INSERT VALUE OF FOREIGN KEY IS INVALID FOR MEMBER_ID AND SITE_ID", ErrorCode.GEN_INVALID_ARGUMENT);
					}
				}
				
			}else{
				throw new BadRequestException(e.getMessage(), ErrorCode.DATA_BASE_INTERNAL_ERROR);
			}
		}

		return CDSOUtils.createOKResponse(response);
	}
	
	// bunchBallActivity
			public void bunchBallActivity(BunchBallActivityRequest bunchBallActivityRequest)
					throws BadRequestException{
				ActivityTrans activityTrans = null;
				
				
				String validationErrors = null;
				try {
					validationErrors = CDSOUtils.validate(bunchBallActivityRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
				} catch (IOException e) {
					logger.error("bunchBallActivity : "+e);
					throw new WebApplicationException(e);
				}
								
				if (validationErrors != null) {
					logger.error("bunchBallActivity :",validationErrors+" memberId :"+bunchBallActivityRequest.getMemberId());
					throw new BadRequestException(validationErrors, ErrorCode.GEN_INVALID_ARGUMENT);
					
				}

				if(bunchBallActivityRequest.getTag()==null
						|| bunchBallActivityRequest.getTag().size()==0){
					logger.error("bunchBallActivity :","Tag Object Cannot be null"+" memberId :"+bunchBallActivityRequest.getMemberId());
					throw new BadRequestException("Tag Object Cannot be null", ErrorCode.GEN_INVALID_ARGUMENT);
				}

				try {
					BigInteger sessionId = null;
					if(bunchBallActivityRequest.getSessionUUID() != null){
						sessionId = cacheService
							.getSessionID(bunchBallActivityRequest.getSessionUUID());
					}
					//if(sessionId != null){
						List<ActivityTrans> activityTransList = populateActivityTrans(
								bunchBallActivityRequest, sessionId);
						for (int i = 0; i < activityTransList.size(); i++) {
							activityTrans = activityTransList.get(i);		
							long start = System.nanoTime();
							activityDAO.bunchBallInfo(activityTrans);
							long end = System.nanoTime();
				            long elapsedTime = end - start;
				            double seconds = (double) elapsedTime / 1000000000.0;
				            logger.info("===========Time Taken by bunchBallInfo ============> "+String.valueOf(seconds));
						}
					/*}else{
						logger.info("bunchBallActivity :","This is not a valid Session UUID"+ " sessionUUID"+ sessionId +" memberId :"+bunchBallActivityRequest.getMemberId());
						throw new BadRequestException("This is not a valid Session UUID", ErrorCode.GEN_INVALID_ARGUMENT);
					}*/
					
					

				} catch (RuntimeException rex) {
					logger.error("bunchBallActivity :",rex.getMessage() );
					
				} 
				
			}

			private List<ActivityTrans> populateActivityTrans(
					BunchBallActivityRequest bunchBallActivityRequest, BigInteger sessionUuid) throws BadRequestException {
				Timestamp currentTimestamp = Timestamp.valueOf(CDSOUtils.formatDateDBDate(sequenceNumberDAO.getCurrentDBTime()));
				BigInteger activityTransactionNo =  sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber("TRANSACTION_ID_SEQ"));
				List<ActivityTrans> activityTransList = new ArrayList<ActivityTrans>();
				ActivityTrans activityTrans = null;
				List<TagObject> tagObjectLists = bunchBallActivityRequest.getTag();
				
				for (TagObject tagObject : tagObjectLists) {					
						if(tagObject.getName()!=null || tagObject.getValue()!=null){
							activityTrans=new ActivityTrans();
							activityTrans.setSessionId(sessionUuid);
							activityTrans.setMemberId(bunchBallActivityRequest.getMemberId());
							//activityTrans.setBevProdCd(bunchBallActivityRequest.getBevProdCd());
							//truncate TagName and tag value
							if(tagObject.getName()!= null && tagObject.getName().length()>255){
								activityTrans.setName(tagObject.getName().substring(0, 255));
							}else{
							activityTrans.setName(tagObject.getName());
							}
							if(tagObject.getValue()!= null && tagObject.getValue().length()>255){
								activityTrans.setValue(tagObject.getValue().substring(0, 255));
							}else{
								activityTrans.setValue(tagObject.getValue());
							}
							
							activityTrans.setTransactionID(activityTransactionNo);
							activityTrans.setInsertDtm(currentTimestamp);
							activityTransList.add(activityTrans);
							
						}					
				}
				return activityTransList;
			}	
			
			
			//recordRedemption API
			public BigInteger InsertRecordRedemptionDetails(RecordRedemptionRequest recordRedemptionRequest) 
					throws BadRequestException{
				RedemptionTrans redemptionTrans1=new RedemptionTrans();
				String validationErrors = null;
				try {
					validationErrors = CDSOUtils.validate(recordRedemptionRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
				} catch (IOException e) {
					logger.error("InsertRecordRedemptionDetails : "+e);
					throw new WebApplicationException(e);
				}
								
				if (validationErrors != null) {
					logger.error("recordRedemption  :",validationErrors+" memberId :"+recordRedemptionRequest.getMemberId());
					throw new BadRequestException(validationErrors, ErrorCode.GEN_INVALID_ARGUMENT);
					
				}
				//lotId and CampaignId are in either or logic .
				//change of logic that both can not be blank for MCR changes 
				//as per logic both can not be null , but both can have value to insert into 
				//Date : 03/22/2017 (as requested by Kyle)
				
				/*if((recordRedemptionRequest.getLotId() != null && recordRedemptionRequest.getCampaignId() == null)){
					logger.error(ErrorCode.REEDEMTION_LOT_CAMPAING_ERROR);
					throw new BadRequestException(ErrorCode.REEDEMTION_LOT_CAMPAING_ERROR,ErrorCode.GEN_INVALID_ARGUMENT);
				}*/
				if((null == recordRedemptionRequest.getLotId() && null == recordRedemptionRequest.getCampaignId())){
				logger.error(ErrorCode.REEDEMTION_LOT_CAMPAING_ERROR);
				throw new BadRequestException("CampaignID , LotId, they both canot be null",ErrorCode.GEN_INVALID_ARGUMENT);
				}
				
				BigInteger transactionId = null;
				try {
					BigInteger sessionId = null;
					if(recordRedemptionRequest.getSessionUUID() != null){
						sessionId=cacheService.getSessionID(recordRedemptionRequest.getSessionUUID());
					}
					redemptionTrans1.setSessionId(sessionId);
					redemptionTrans1.setMemberId(recordRedemptionRequest.getMemberId());
					redemptionTrans1.setProgramId(recordRedemptionRequest.getProgramId());
					redemptionTrans1.setLotId(recordRedemptionRequest.getLotId());
					redemptionTrans1.setProductId(recordRedemptionRequest.getProductId());
					redemptionTrans1.setClientTransactionId(recordRedemptionRequest.getClientTransactionId());
					redemptionTrans1.setCampaignId(recordRedemptionRequest.getCampaignId());
					redemptionTrans1.setBrandId(recordRedemptionRequest.getBrandId());
					SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
					transactionId = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
					redemptionTrans1.setTransactionId(transactionId);
					
					long start = System.nanoTime();
					 activityDAO.recordRedemption(redemptionTrans1);	
					 long end = System.nanoTime();
			            long elapsedTime = end - start;
			            double seconds = (double) elapsedTime / 1000000000.0;
			            logger.info("===========Time Taken by recordRedemption ============> "+String.valueOf(seconds));

				} catch (DuplicateKeyException rex) {
					throw new BadRequestException(rex.getMessage(),
							ErrorCode.GEN_INVALID_ARGUMENT);
				} catch (DataIntegrityViolationException rex) {
					throw new BadRequestException(rex.getMessage(),
							ErrorCode.GEN_INVALID_ARGUMENT);
				}				
				return transactionId;
			}

			public ActivityDAO getActivityDAO() {
				return activityDAO;
			}

			public void setActivityDAO(ActivityDAO activityDAO) {
				this.activityDAO = activityDAO;
			}

			public ICacheService getCacheService() {
				return cacheService;
			}

			public void setCacheService(ICacheService cacheService) {
				this.cacheService = cacheService;
			}

			public SequenceNumberDAO getSequenceNumberDAO() {
				return sequenceNumberDAO;
			}

			public void setSequenceNumberDAO(SequenceNumberDAO sequenceNumberDAO) {
				this.sequenceNumberDAO = sequenceNumberDAO;
			}
			
			
			
			
			
}