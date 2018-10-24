package com.ko.cds.service.helper;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.points.PointAccountDAO;
import com.ko.cds.dao.sessionId.SessionIdDAO;
import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.activity.TagObject;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.pojo.janrainIntegration.ExternalIds;
import com.ko.cds.pojo.janrainIntegration.Result;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.member.Email;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.MemberSocialDomain;
import com.ko.cds.pojo.member.MergeMemberInfo;
import com.ko.cds.pojo.member.MergedMember;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.pojo.points.TransactionHistory;
import com.ko.cds.request.member.MemberSearchRequest;
import com.ko.cds.request.member.MemberSearchRequestV2;
import com.ko.cds.request.member.MergeMemberRequest;
import com.ko.cds.request.member.UpdateMemberRequest;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.EmailValidator;
import com.ko.cds.utils.ErrorCode;

@Component
public class MemberServiceHelper {
	

	private static final String UPDATE_CDSOID="Update_cdsoID";
	private static final String UPDATE_SMS="Update_SMSNumber";
	private static final String UPDATE_EXTERNALID="Update_ExternalID";
	private static final String UPDATE_OPTS="Update_COMM_OPTS";
	private static final Logger logger = LoggerFactory.getLogger(MemberServiceHelper.class);
	 @Autowired
	private MemberDAO memberDAO;
	@Autowired
	private SequenceNumberDAO sequenceNumberDAO;
	@Autowired
	private SessionIdDAO sessionIdDAO;
	@Autowired
	private PointAccountDAO pointAccountDAO;
	@Autowired
	private ICacheService iCacheService;
	@Autowired
	private PointAccountServiceHelper pointAccountServiceHelper;
	@Autowired
	private SurveyDAO surveyDAO;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
	private EhCacheCacheManager cacheManager;
	@Autowired
	private ICacheService cacheService;
	
	
	public BigInteger createMemberInfo(final MemberInfo memberInfo) throws BadRequestException {
	String validationErrors= null;	
		try {
			validationErrors=CDSOUtils.validate(memberInfo, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.error("createOrUpdateMemberInfo :",e);
			throw new WebApplicationException(e);
		}
		 if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		    }
		// Validate Email using custom validator
		 if(memberInfo != null && memberInfo.getEmailAddress() != null){
			 if(!emailValidator.validate(memberInfo.getEmailAddress())){
				 throw new BadRequestException("EMAIL is not Valid",ErrorCode.GEN_INVALID_ARGUMENT);
			 }
		 }
	
		 // check for duplicate UUID if found if UUID found return existing member ID 
		 
		 //if UUID not in the request need to link this member with Janrain 
		 BigInteger memberId;
		 synchronized (memberInfo) {
			
			 System.out.println("Looking for UUID : "+memberInfo.getUuid());
			 
			 if(memberInfo.getUuid()!=null)
			 {
				   memberId = memberDAO.getMemberIdbyJanrainUUID(memberInfo.getUuid());
				  //memberId = iCacheService.getMemberIdByUUID(memberInfo.getUuid());
				 //System.out.println("memberId reveived from cache : "+memberId);
				 //if memberId not found with request UUID normal flow 
				 if(memberId!=null)
				 {
					 //if member id found just call janrain ID , not to create  member in CDSO DB and return that member id(if two record with that UUID return last one)
					 memberInfo.setMemberId(memberId);
					 ExecutorService es = Executors.newFixedThreadPool(1);
				        @SuppressWarnings("unchecked")
						final Future future = es.submit(new Callable() {
				                    public Object call() throws Exception {
				                    	updateJanrain(memberInfo,UPDATE_CDSOID,null);
				                        return null;
				                    }
				                });
						
						es.shutdown();
						return memberId;
				 }
			 }
		 
		
			memberId= sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber(CDSConstants.MEMBER_ID_SEQ));
			Cache ehcache = cacheManager.getCache(CDSConstants.MEMBER_UUID_CACHE);
			ehcache.put(memberInfo.getUuid(), memberId);
		 }
		memberInfo.setMemberId(memberId);
		if(memberInfo.getMemberStatus() == null){
			memberInfo.setMemberStatus(CDSConstants.STATUS_ACTIVE);
		}
		memberDAO.insertMemberInfo(memberInfo);
		//check if there is email address
		if(memberInfo.getEmailAddress() != null){
			
			memberDAO.insertEmail(populateEmail(memberInfo));
		}
		//Insert Addresses
		if(memberInfo.getAddresses()!=null && memberInfo.getAddresses().size() >0){
			for (Address address : memberInfo.getAddresses()) {
				 address.setMemberId(memberId);
				 memberDAO.insertAddress(address);
			}
		}
		//Insert Phone Numbers
		if(memberInfo.getPhoneNumbers()!= null && memberInfo.getPhoneNumbers().size()>0){
			for (PhoneNumber phoneNumber : memberInfo.getPhoneNumbers()) {
				phoneNumber.setMemberId(memberId);
				memberDAO.insertPhoneNumber(phoneNumber);
			}
		}
		// Insert SMS number
		if(memberInfo.getSmsNumbers() != null && memberInfo.getSmsNumbers().size()>0){
			for (SMSNumber smsNumber : memberInfo.getSmsNumbers()) {
				smsNumber.setMemberId(memberId);
				if(smsNumber.getSmsType() == null){
					smsNumber.setSmsType(CDSConstants.DEFAULT_SMS_TYPE);
				}
				memberDAO.insertSMSNumber(smsNumber);
			}
			
		}
		//Insert member identifiers 
		if(memberInfo.getMemberIdentifiers()!= null && memberInfo.getMemberIdentifiers().size() >0){
			for (MemberIdentifier memberIdentifier : memberInfo.getMemberIdentifiers()) {
				memberIdentifier.setMemberId(memberId);
				memberDAO.insertMemberIdentifier(memberIdentifier);
			}
		}
		
		//Insert member Social Domain 
				if(memberInfo.getSocialDomain()!= null && memberInfo.getSocialDomain().size() >0){
					for (MemberSocialDomain memberSocialDomain : memberInfo.getSocialDomain()) {
						memberSocialDomain.setMemberId(memberId);
						//profile data to be obtained from the sequence.
						SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
						BigInteger profileId = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
						memberSocialDomain.setProfileId(profileId);
						memberDAO.insertMemberSocialData(memberSocialDomain);
					}
				}
		
		//Call Janrain to update CDSO member ID and proper UUID found 
		/*if(memberInfo.getUuid()!=null)
		{
				updateJanrain(memberInfo,UPDATE_CDSOID,null);
		}*/
		
/*		ExecutorService es = Executors.newFixedThreadPool(1);
        @SuppressWarnings("unchecked")
		final Future future = es.submit(new Callable() {
                    public Object call() throws Exception {
                    	updateJanrain(memberInfo,UPDATE_CDSOID,null);
                        return null;
                    }
                });
		
		es.shutdown();*/
		
		CDSOUtils.JanrainMemberUpdateTaskList.add(new UpdateJanrainTask(memberInfo,UPDATE_CDSOID,null));
		return memberId;
	}
	private Email populateEmail(MemberInfo memberInfo){
		Email email = new Email();
		email.setMemberId(memberInfo.getMemberId());
		email.setEmail(memberInfo.getEmailAddress());
		email.setPrimaryIndicator(CDSConstants.Y);
		email.setStatusCode(CDSConstants.STATUS_ACTIVE);
		return email;
		
	}
	public MemberInfo searchMemberInfo(MemberSearchRequest memberSearchRequest) throws BadRequestException{
		String validationErrors = null;
		MemberInfo memberInfo = null;
		try {
			validationErrors=CDSOUtils.validate(memberSearchRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("createOrUpdateMemberInfo :",e);
			throw new WebApplicationException(e);
		}
		if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		  }
		try{
			memberInfo=memberDAO.searchMemberInfo(memberSearchRequest);
			if(null == memberInfo){
				logger.error("Member not Found with the Details sent in the search Parameter"+memberSearchRequest.toString());
				throw new BadRequestException(ErrorCode.MEMBER_NOT_FOUND_DESC,ErrorCode.MEMBER_NOT_FOUND);
			}
		}catch(DataIntegrityViolationException rex){
			//logger.error("MemberService :BadRequestException",rex);
			throw new BadRequestException(rex.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT,rex);
		}catch(BadSqlGrammarException rex){
			//logger.error("MemberService :BadRequestException",rex);
			
			throw new BadRequestException(rex.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT,rex);
		}
		return memberInfo;
	}
	
	public BigInteger mergeLiteMemberDetails(MergeMemberRequest mergeMemberRequest) throws BadRequestException{
		String validationErrors = null;
		try {
			validationErrors = CDSOUtils.validate(mergeMemberRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("mergeMember : "+e);
			throw new WebApplicationException(e);
		}
						
		if (validationErrors != null) {
			throw new BadRequestException(validationErrors, ErrorCode.GEN_INVALID_ARGUMENT);
			
		}
		
		//deactive the lite account values at first to avoid the primary key constaint violation.
		updateStatusCode(mergeMemberRequest.getFromMemberId());
		
		final BigInteger toMemberId = new BigInteger(mergeMemberRequest.getToMemberId().toString());
		BigInteger fromMemberId = new BigInteger(mergeMemberRequest.getFromMemberId().toString());
		//get the from Member Details 
		MergeMemberInfo fromMemberInfo=memberDAO.getmemberInfoForMerge(fromMemberId);
		//get the To Memeber Details
		MergeMemberInfo toMemberInfo=memberDAO.getmemberInfoForMerge(toMemberId);
		
		if(null == fromMemberInfo || null == toMemberInfo){
			throw new BadRequestException("Either From OR To Account is not a Valid Member", ErrorCode.MEMBER_NOT_FOUND);
		}
		//form account should always be lite account else Throw error
		if(fromMemberInfo.getUuid() != null || toMemberInfo.getUuid() == null ){
			throw new BadRequestException("Either From Account is not Lite Account OR To Account is not Full Account", ErrorCode.NOT_MERGED_NONLITE_ACCOUNT);
		}
		
		//its a vlaid Request TO process
		
		//email checking for merge if exist the just add one entry  in email table else change the update date.
		Email fromEmail = fromMemberInfo.getEmail();
		if(fromEmail != null){
			Email email = new Email();
			email.setMemberId(toMemberId);
			email.setEmaiVerifiedDt(fromEmail.getEmaiVerifiedDt());
			email.setPrimaryIndicator(fromEmail.getPrimaryIndicator());
			//email.setStatusCode(fromEmail.getStatusCode());
			email.setEmail(fromEmail.getEmail());
			email.setValidInd(fromEmail.getValidInd());
			
			//insert if the to member Id is null else update as the tables have 1:1 relation.
			if(toMemberInfo.getEmail() == null){
				memberDAO.insertEmail(email);
			}
			
			//for future 1:M relation.
			/*boolean flag = false;			
			if(toMemberInfo.getEmail() != null){
				if(fromMemberInfo.getEmail().getEmail().equals(toMemberInfo.getEmail().getEmail())){
					flag = true;
				}else{
					//insert the new email address but with different primary indicator in case of 1:M relation 
					fromMemberInfo.getEmail().setPrimaryIndicator("N");
				}
			}
			
			//insert if the to member Id is null else do not perform anything as we are not touching the jainrain data.
			if(!flag){
				fromMemberInfo.getEmail().setMemberId(toMemberId);
				fromMemberInfo.getEmail().setStatusCode(CDSConstants.STATUS_ACTIVE);
				memberDAO.insertEmail(fromMemberInfo.getEmail());
			}*/
		}
		
		//smsNumber for Merge
		if(fromMemberInfo.getSmsNumbers() != null && fromMemberInfo.getSmsNumbers().size()>0){
			//loop through both the from and too member to check for same data if so then skip that else add new
			for(final SMSNumber fromSmsNumber : fromMemberInfo.getSmsNumbers()){
				boolean flag = false;
				SMSNumber sms = new SMSNumber();
				sms.setAlias(fromSmsNumber.getAlias());
				sms.setMemberId(toMemberId);
				sms.setSmsNumber(fromSmsNumber.getSmsNumber());
				sms.setSmsType(fromSmsNumber.getSmsType());
				//sms.setStatusCode(); //status code set form the POJO
				sms.setValidationCode(fromSmsNumber.getValidationCode());
				
				//check for primary Key constraints
				if(toMemberInfo.getSmsNumbers() != null && toMemberInfo.getSmsNumbers().size()>0){
					for(SMSNumber toSmsNumber : toMemberInfo.getSmsNumbers()){
						if(fromSmsNumber.getSmsType().equalsIgnoreCase(toSmsNumber.getSmsType())){
							flag = true; //same merge information exists in DB record exists in DB update it with from users Information 
							break;
						}
					}
				}
				if(flag){
					//update the data means change the update dt only
					memberDAO.updateSMSNumber(sms);
				}else{
					//insert new record
					memberDAO.insertSMSNumberForMerge(sms);
				}

				/*ExecutorService es = Executors.newFixedThreadPool(1);
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Future future = es.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	updateJanrain(fromSmsNumber,UPDATE_SMS,toMemberId);
		                        return null;
		                    }
		                });
				es.shutdown();*/
				CDSOUtils.JanrainMemberUpdateTaskList.add(new UpdateJanrainTask(fromSmsNumber,UPDATE_SMS,toMemberId));
			}
		}
		
		//Identifier Merge
		if(fromMemberInfo.getMemberIdentifiers() != null && fromMemberInfo.getMemberIdentifiers().size()>0){
			for (final MemberIdentifier fromMemberIdentifier : fromMemberInfo.getMemberIdentifiers()) {
				boolean flag = false;
				MemberIdentifier identifier = new MemberIdentifier();
				identifier.setChannelTypeCode(fromMemberIdentifier.getChannelTypeCode());
				identifier.setDomainName(fromMemberIdentifier.getDomainName());
				identifier.setMemberId(toMemberId);
				//identifier.setStatusCode(fromMemberIdentifier.getStatusCode());
				identifier.setUserId(fromMemberIdentifier.getUserId());
				identifier.setUserName(fromMemberIdentifier.getUserName());
				
				//check for Primary Key Constraints
				if(toMemberInfo.getMemberIdentifiers() != null && toMemberInfo.getMemberIdentifiers().size()>0){
					for (MemberIdentifier toMemberIdentifier : toMemberInfo.getMemberIdentifiers()) {
						if(fromMemberIdentifier.getDomainName().equalsIgnoreCase(toMemberIdentifier.getDomainName())){
							flag = true ; //Multiple doamin name is not allowed for a member.
							break;
						}
					}
				}
				if(flag){
					//update the member domain details
					memberDAO.updateMemberIdentifier(identifier);
				}else{
					//insert the memberIdentiofier details
					memberDAO.insertMemberIdentifier(identifier);
				}
				//need to update the Jainrain ,call jainrain
				/*ExecutorService es = Executors.newFixedThreadPool(1);
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Future future = es.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	//form the tag object for the memberIdentifier
		                    	TagObject fromMemberIdentifierTagObj = new TagObject();
		                    	fromMemberIdentifierTagObj.setName(fromMemberIdentifier.getDomainName());
		                    	fromMemberIdentifierTagObj.setValue(fromMemberIdentifier.getUserId());
		                    	updateJanrain(fromMemberIdentifierTagObj,UPDATE_EXTERNALID,toMemberId);
		                        return null;
		                    }
		                });
				es.shutdown();*/
				
				TagObject fromMemberIdentifierTagObj = new TagObject();
            	fromMemberIdentifierTagObj.setName(fromMemberIdentifier.getDomainName());
            	fromMemberIdentifierTagObj.setValue(fromMemberIdentifier.getUserId());
            	CDSOUtils.JanrainMemberUpdateTaskList.add(new UpdateJanrainTask(fromMemberIdentifierTagObj,UPDATE_EXTERNALID,toMemberId));
			}
		}
		
		//Merge the Communication Opts 
		if(fromMemberInfo.getComOpts() != null && fromMemberInfo.getComOpts().size()>0){
			for (CommunicationOptIn fromOptIn : fromMemberInfo.getComOpts()) {
					CommunicationOptIn comOptIn  = new CommunicationOptIn();
					comOptIn.setAcceptedDate(fromOptIn.getAcceptedDate());
					comOptIn.setClientId(fromOptIn.getClientId());
					comOptIn.setCommunicationTypeName(fromOptIn.getCommunicationTypeName());
					comOptIn.setFormat(fromOptIn.getFormat());
					comOptIn.setMemberId(toMemberId);
					comOptIn.setOptedInIndicator(fromOptIn.getOptedInIndicator());
					comOptIn.setSchedulePreference(fromOptIn.getSchedulePreference());
					comOptIn.setType(fromOptIn.getType());
					comOptIn.setInsertDate(fromOptIn.getInsertDate());
					surveyDAO.insertOpts(comOptIn);
			}
			//perform jainrain update
			//check the max insert date and do the update 
			final List<CommunicationOptIn> jainrainOpts = new ArrayList<CommunicationOptIn>();
			List<CommunicationOptIn> currentOptIns= surveyDAO.getOptsAfterMerge(toMemberId);
			for (CommunicationOptIn currentOptPref : currentOptIns) {
				boolean flag = false;
				for (final CommunicationOptIn fromOptIn : fromMemberInfo.getComOpts()) {
					if((fromOptIn.getInsertDate().equals(currentOptPref.getInsertDate()) && fromOptIn.getCommunicationTypeName().endsWith(currentOptPref.getCommunicationTypeName()))){
						flag = true;
					}else if ((fromOptIn.getInsertDate().after(currentOptPref.getInsertDate()) && fromOptIn.getCommunicationTypeName().endsWith(currentOptPref.getCommunicationTypeName()))){
						flag = true;
					}
					
					if(flag){
						jainrainOpts.add(currentOptPref);
						break;
					}
				}
			}
			logger.info("Json Set to Jainrain for Opts Update "+ jainrainOpts.toString());
			if(jainrainOpts.size()>0){
				//perform jainrain update
				//need to update the Jainrain ,call jainrain
				/*ExecutorService es = Executors.newFixedThreadPool(1);
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final Future future = es.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	updateJanrain(jainrainOpts,toMemberId);
		                        return null;
		                    }
		                });
				es.shutdown();*/
				
				CDSOUtils.JanrainOptsTaskList.add(new UpdateJanrainForOptsTask(jainrainOpts,toMemberId));
			}
		}
		
		//SocialDomain Merge
		if(fromMemberInfo.getSocialDomains() != null && fromMemberInfo.getSocialDomains().size()>0){
			for (final MemberSocialDomain fromMemberSocialDomain : fromMemberInfo.getSocialDomains()) {
				boolean flag = false;
				MemberSocialDomain socialDomain = new MemberSocialDomain();
				socialDomain.setDomain(fromMemberSocialDomain.getDomain());
				socialDomain.setIdentifier(fromMemberSocialDomain.getIdentifier());
				socialDomain.setMemberId(toMemberId);
				socialDomain.setProfileId(fromMemberSocialDomain.getProfileId());
				socialDomain.setUserName(fromMemberSocialDomain.getUserName());
				
				//check for Primary Key Constraints
				if(toMemberInfo.getSocialDomains() != null && toMemberInfo.getSocialDomains().size()>0){
					for (MemberSocialDomain toMemberSocialDomain : toMemberInfo.getSocialDomains()) {
						if(fromMemberSocialDomain.getDomain().equalsIgnoreCase(toMemberSocialDomain.getDomain())){
							flag = true ; //Multiple doamin name is not allowed for a member.
							break;
						}
					}
				}
				if(flag){
					//update the member domain details
					memberDAO.updateMemberSocialDomain(socialDomain);
				}else{
					//insert the memberIdentiofier details
					SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
					BigInteger profileId = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
					socialDomain.setProfileId(profileId);
					memberDAO.insertMemberSocialData(socialDomain);
				}
			}
		}
	
		
		//updating the balance..
		if(mergeMemberRequest.getBonus().equals("Y")){
			pointAccountServiceHelper.updatePointBalanceForBonusMerge(fromMemberId, toMemberId);
			//updating the Point Trans Table..for the Bonus Merge Only
			fetchAndInsertPointTrans(fromMemberId,toMemberId,mergeMemberRequest.getBonus());
		}else{
			pointAccountServiceHelper.updatePointBalanceForMerge(fromMemberId, toMemberId);
			//updating the Point Trans Table
			//fetchAndInsertPointTrans(fromMemberId,toMemberId,mergeMemberRequest.getBonus());
		}
		
		//updating the Merged Member Details table this table will be refred by ETL jobs to set the respective Id to Deactivate
		BigInteger mergedId = insertMergedMembersDetails(fromMemberId, toMemberId);
		memberDAO.updateMemberStatus(CDSConstants.DEACTIVE_STATUS_CODE,fromMemberId);
		//delete the lite member from DB after the Merge -as a quick fix in place of ETL jobs.
		deleteMemberInformation(fromMemberId);
		return mergedId;
	}
	
	public void updateStatusCode(BigInteger fromMemberId){	
		String statusCode = CDSConstants.DEACTIVE_STATUS_CODE;
		memberDAO.updateStatusOfSMSNUmber(statusCode, fromMemberId);
		memberDAO.updateStatusOfEmail(statusCode, fromMemberId);
		memberDAO.updateStatusOfIdentifier(statusCode, fromMemberId);
		//memberDAO.updateStatusOfOpts(statusCode, fromMemberId);	//there is no status column in OPts table	
	}
	
	public void deleteMemberInformation(BigInteger fromMemberId){	
		memberDAO.deleteMemberFromSMSNUmber(fromMemberId);
		memberDAO.deleteMemberFromIdentifier(fromMemberId);
		//memberDAO.deleteMemberFromEmail(fromMemberId);
	}
	
	public void fetchAndInsertPointTrans(BigInteger fromMemberId, BigInteger toMemberId,String isBonus){
		TransactionHistory transactionHistory=new TransactionHistory();
		transactionHistory.setMemberId(fromMemberId); 
		List<TransactionHistory> fromMemberTransHistory=pointAccountDAO.getPointHistoryByMemberId(transactionHistory);
		
		for(TransactionHistory fromMemberPointsHistory:  fromMemberTransHistory)
		{
		    TransactionHistory memberTransferPtsHistory=new TransactionHistory();
		    memberTransferPtsHistory.setAccountType(fromMemberPointsHistory.getAccountType());
		    memberTransferPtsHistory.setClientTransId(fromMemberPointsHistory.getClientTransId());
		    memberTransferPtsHistory.setHoldTime(fromMemberPointsHistory.getHoldTime());
		    memberTransferPtsHistory.setItemId(fromMemberPointsHistory.getItemId());
		    memberTransferPtsHistory.setMemberId(toMemberId);
		    memberTransferPtsHistory.setPointsQuanity(fromMemberPointsHistory.getPointsQuanity());
		    memberTransferPtsHistory.setPointsSource(fromMemberPointsHistory.getPointsSource());
		    memberTransferPtsHistory.setPointsType(fromMemberPointsHistory.getPointsType());
		    memberTransferPtsHistory.setProductID(fromMemberPointsHistory.getProductID());
		    memberTransferPtsHistory.setPromotionId(fromMemberPointsHistory.getPromotionId());
		    if(isBonus.equalsIgnoreCase("Y")){
		    	 memberTransferPtsHistory.setReasonCode(CDSConstants.BONUS_MERGE_REASON_CODE);
		    }else{
		    	 memberTransferPtsHistory.setReasonCode(CDSConstants.MERGE_REASON_CODE);
		    }
		   
		    memberTransferPtsHistory.setRelatedTransactionId(fromMemberPointsHistory.getRelatedTransactionId());
		    memberTransferPtsHistory.setSessionId(fromMemberPointsHistory.getSessionId());
		    memberTransferPtsHistory.setStakeHolderId(fromMemberPointsHistory.getStakeHolderId());
		    memberTransferPtsHistory.setTransactionTimeStampString(fromMemberPointsHistory.getTransactionTimeStampString());
		    memberTransferPtsHistory.setTransactionType(fromMemberPointsHistory.getTransactionType());
		    memberTransferPtsHistory.setInsertDt(fromMemberPointsHistory.getInsertDt());
		    SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
		    memberTransferPtsHistory.setPointTransId(sequenceNumberDAO.getNextSequenceNumber(sequenceNumber));
		    //Code change for the Member Merge new feilds update based on CSR Points API.
		    memberTransferPtsHistory.setBalance(fromMemberPointsHistory.getBalance());
	        pointAccountDAO.insertTransactionHistoryForMerge(memberTransferPtsHistory);
		}
		
	}
	
	public BigInteger insertMergedMembersDetails(BigInteger fromMemberId, BigInteger toMemberId){
		MergedMember mergedMember=new MergedMember();
		mergedMember.setMergedFrom(fromMemberId);
		mergedMember.setMergedTo(toMemberId);
		memberDAO.insertMergedMember(mergedMember);
		return toMemberId;
	}
	
	
	public boolean updateJanrain(Object memberInfo, String callerMethod,BigInteger toMemId) 
	{
	    boolean uuidFound=true;
	    String janrainUUID="";
	    String updateSTR="";
		boolean returnValue=true;
		MemberInfo memberDetails = null;
		SMSNumber mergrSmsNumber=null;
		TagObject memberIdentifier = null;
		CommunicationOptIn commOpts = null;
		try{
    		if(callerMethod.equals(UPDATE_CDSOID))
    		{
    			memberDetails = (MemberInfo)memberInfo;
    			if(null==memberDetails.getUuid())
    			{
    			    uuidFound=false;
    			}
    			else
    			{
    			    janrainUUID=memberDetails.getUuid();
    			    updateSTR=getAttributeValueCDSO(memberDetails);
    			}
    			
    		}
    		
    		if(callerMethod.equals(UPDATE_SMS))
    		{
    		    mergrSmsNumber = (SMSNumber)memberInfo;
    		    janrainUUID=(memberDAO.getMemberUUIDbyMemID(toMemId)).getUuid();
    		    if(null == janrainUUID)
    		    {
    		        uuidFound=false;
    		    }
    		    else
    		    {
    		        updateSTR=getAttributeValueSMS(mergrSmsNumber);
    		    }
    			
    		}else if(callerMethod.equals(UPDATE_EXTERNALID)){
    			memberIdentifier = (TagObject)memberInfo;
    		    janrainUUID=(memberDAO.getMemberUUIDbyMemID(toMemId)).getUuid();
    		    if(null == janrainUUID)
    		    {
    		        uuidFound=false;
    		    }
    		    else
    		    {
    		        updateSTR=getAttributeValueExternalId(memberIdentifier);
    		    }
    			
    		}else if(callerMethod.equals(UPDATE_OPTS)){
    			commOpts = (CommunicationOptIn)memberInfo;
    		    janrainUUID=memberDAO.getMemberUUIDbyMemID(toMemId).getUuid();
    		    if(null == janrainUUID)
    		    {
    		        uuidFound=false;
    		    }
    		    else
    		    {
    		        updateSTR=getAttributeValueLegalAcceptance(commOpts);
    		    }
    			
    		}
    		
    		
    		if(uuidFound)
    		{
        		Map<String,String> janrainReaponse = CDSOUtils.updateCDSOIDtoJanrain(updateSTR,janrainUUID);
        		if(janrainReaponse.get("StatusCode").equals("200") &&  janrainReaponse.get("ResponseSTR").equals("{\"stat\":\"ok\"}"))
        		{
        		    returnValue=true;
        		}
        		else
        		{
        		    logger.info("Status Code :"+ janrainReaponse.get("StatusCode")+" Response "+janrainReaponse.get("ResponseSTR")+" Exception "+janrainReaponse.get("Exception"));
        		    returnValue = false;
        		}
    		}
    		else
    		{
    		    logger.info("UUID not set to janrain UPDATE");
    		}
		}
		catch(Exception exx)
		{
		    logger.error("Exception found in Janrain Update",exx);
		    returnValue=false;
		}
		return returnValue;
	}
	private String getAttributeValueCDSO(MemberInfo memberInfo) {
		
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("{\"externalIds\": [{\"useForLogin\": false,\"value\":\""+memberInfo.getMemberId()+"\",\"type\": \"CDS\",\"lastUpdatedTime\": \""+CDSOUtils.getCurrentUTCTimestamp()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	
	private String getAttributeValueSMS(SMSNumber memberSMSInfo) {
		
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("{\"phoneNumbers\": [{\"countryCode\":\"001\",\"type\":\""+memberSMSInfo.getSmsType()+"\",\"value\":\""+memberSMSInfo.getSmsNumber()+"\",\"dateVerified\": \""+CDSOUtils.getCurrentUTCTimestamp()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	private String getAttributeValueExternalId(TagObject memberIdentifier) {
		
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("{\"externalIds\": [{\"useForLogin\": false,\"value\":\""+memberIdentifier.getValue()+"\",\"type\": \""+memberIdentifier.getName()+"\",\"lastUpdatedTime\": \""+CDSOUtils.getCurrentUTCTimestamp()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	private String getAttributeValueLegalAcceptance(CommunicationOptIn commOpts){
		StringBuilder returnStr = new StringBuilder();
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String environment = configProp.get(CDSConstants.ENVORONMENT_KEY);
		String janrainClientId  = 	configProp.get(CDSConstants.JANRAIN_CLIENT_ID_KEY+"_"+environment);
		returnStr.append("{\"communicationOpts\": [{\"optId\":\""+commOpts.getCommunicationTypeName()+"\",\"clientId\":\""+janrainClientId+"\",\"accepted\": \""+commOpts.getOptedInIndicator()+"\",\"type\": \""+commOpts.getType()+"\",\"dateAccepted\":\""+commOpts.getAcceptedDate()+"\",\"schedulePreference\":\""+commOpts.getSchedulePreference()+"\",\"format\":\""+commOpts.getFormat()+"\"}]}") ;
		logger.info("Janrain Update With "+returnStr.toString());
		
		return returnStr.toString();
	}
	
	
	public void updateJanrain(List<CommunicationOptIn> commOpt, BigInteger toMemId) {
		
		String janrainUUID=((memberDAO.getMemberUUIDbyMemID(toMemId)).getUuid()).toString();
	    if(null != janrainUUID)
	    {
	    	// For janrain update - formatting json
			JSONArray array = new JSONArray();
			JSONObject obj = null;
			String janrainJson = "";
			Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
			String environment = configProp.get(CDSConstants.ENVORONMENT_KEY);
			String janrainClientId  = 	configProp.get(CDSConstants.JANRAIN_CLIENT_ID_KEY+"_"+environment);
			try {
				for (CommunicationOptIn opt : commOpt) {
					obj = new JSONObject();
					obj.put(CDSConstants.JANRAIN_OPTS_ID, opt.getCommunicationTypeName());
					obj.put(CDSConstants.JANRAIN_OPTS_CLIENT_ID, janrainClientId);
					obj.put(CDSConstants.JANRAIN_OPTS_ACCEPTED, opt.getOptedInIndicator());
					obj.put(CDSConstants.JANRAIN_OPTS_TYPE, opt.getType());
					obj.put(CDSConstants.JANRAIN_OPTS_SCHEDULE_PREF, opt.getSchedulePreference());
					obj.put(CDSConstants.JANRAIN_OPTS_FORMAT, opt.getFormat());
					Date date = opt.getAcceptedDate();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					String outputDate = format.format(date);
					obj.put(CDSConstants.JANRAIN_OPTS_ACCEPT_DATE, outputDate);
					array.put(obj);
				}

				obj = new JSONObject();
				obj.put(CDSConstants.JANRAIN_OPTS, array);
				janrainJson = obj.toString();
				logger.info("Janrain json for postOpts : " + janrainJson);
			} catch (JSONException e) {
				logger.error("JSONException : " + e.getMessage());
			}

			Map<String, String> janrainResponse = CDSOUtils.updateCDSOIDtoJanrain(
					janrainJson, janrainUUID);
			if (janrainResponse.get("StatusCode").equals("200")
					&& janrainResponse.get("ResponseSTR").equals(
							"{\"stat\":\"ok\"}")) {
				logger.info("janrain insert is successful");
			} else {
				logger.info("Status Code :" + janrainResponse.get("StatusCode")
						+ " Response " + janrainResponse.get("ResponseSTR")
						+ " Exception " + janrainResponse.get("Exception"));
			}
	    }else
		{
		    logger.info("UUID not set to janrain UPDATE");
		}
	}
	
	public void updateMember(final UpdateMemberRequest updateMemberRequest) throws BadRequestException{
		String validationErrors= null;	
		boolean isSMSUpdate = false;
		boolean isExternalIDsUpdate = false;
		MemberIdentifier memberIdentifier = null;
		try {
			validationErrors=CDSOUtils.validate(updateMemberRequest, CDSConstants.UPDATE_MEMBER_PROFILE_NAME, false);
		} catch (IOException e) {
			logger.info("createOrUpdateMemberInfo :",e);
			throw new WebApplicationException(e);
		}
		 if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		  }
		 if(updateMemberRequest.getSmsObject()==null&& updateMemberRequest.getMemberIdentifiers()==null  && updateMemberRequest.getSocialDomain() == null){
			 throw new BadRequestException("SMSObject and MemberIdentifiers and Social Domain both are null",ErrorCode.GEN_INVALID_ARGUMENT);
		 }else if(updateMemberRequest.getSmsObject()!=null && updateMemberRequest.getSmsObject().getSmsNumber()==null && updateMemberRequest.getSmsObject().getSmsType()== null && updateMemberRequest.getMemberIdentifiers()!= null && updateMemberRequest.getMemberIdentifiers().getName()== null&& updateMemberRequest.getMemberIdentifiers().getValue()== null  ){
			 throw new BadRequestException("SMSObject and MemberIdentifiers both are null",ErrorCode.GEN_INVALID_ARGUMENT);
		 }else if(updateMemberRequest.getMemberIdentifiers()==null && updateMemberRequest.getSmsObject() != null && updateMemberRequest.getSmsObject().getSmsType() == null){
			 throw new BadRequestException("SMSType in SMSObject is null",ErrorCode.GEN_INVALID_ARGUMENT);
		 }else if(updateMemberRequest.getSmsObject()==null && updateMemberRequest.getMemberIdentifiers() != null && updateMemberRequest.getMemberIdentifiers().getName()== null){
			 throw new BadRequestException("Name in MemberIdentifiers is null",ErrorCode.GEN_INVALID_ARGUMENT);
		 }else if (updateMemberRequest.getMemberIdentifiers() ==null && updateMemberRequest.getSmsObject() ==null && updateMemberRequest.getSocialDomain() != null && updateMemberRequest.getSocialDomain().getDomain() == null){
			 throw new BadRequestException("Domain in Social Domain is null",ErrorCode.GEN_INVALID_ARGUMENT);
		 }
		 
		 if(updateMemberRequest.getSmsObject() != null && updateMemberRequest.getSmsObject().getSmsType()!= null){
			 //check unique validation for SMS Number
			 if(updateMemberRequest.getSmsObject().getSmsNumber()!= null){
				 /*SMSNumber smsNumber=memberDAO.findBySMSNumber(updateMemberRequest.getSmsObject().getSmsNumber());
				 if(smsNumber != null && smsNumber.getSmsNumber()!= null ){
					 throw new BadRequestException(ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER_DESC,ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER); 
					 
				 }*/
				 try{
					 memberDAO.updateSMSNumberForMember(updateMemberRequest);
				 }catch(DuplicateKeyException exception){
					 throw new BadRequestException(ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER_DESC+"\n",ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER);
				 }
			 }else if(updateMemberRequest.getSmsObject().getSmsNumber()== null){
				 // Delete record for that member from database using memberId, statusCd and smsType
				 SMSNumber smsNumber = new SMSNumber();
				 smsNumber.setMemberId(updateMemberRequest.getMemberId());
				 smsNumber.setSmsType(updateMemberRequest.getSmsObject().getSmsType());
				 smsNumber.setStatusCode(CDSConstants.STATUS_ACTIVE);
				 memberDAO.deleteFromSMSNumberForUpdateMember(smsNumber);
				
			 }
			 
			 isSMSUpdate= true;
		 }if(updateMemberRequest.getMemberIdentifiers() != null && updateMemberRequest.getMemberIdentifiers().getName()!= null){
			//check unique validation for SMS Number
			 if(updateMemberRequest.getMemberIdentifiers().getValue()!= null){
				/* memberIdentifier=memberDAO.findByExternalId(updateMemberRequest.getMemberIdentifier().getValue());
				 if(memberIdentifier != null && memberIdentifier.getUserId() != null ){
					 throw new BadRequestException(ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER_DESC,ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER);
				 }*/
				 memberIdentifier= populateMemberIdentifier(updateMemberRequest);
				 try{
					 memberDAO.updateExternalIDForMember(memberIdentifier);
				 }catch(DuplicateKeyException exception){
					 throw new BadRequestException(ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER_DESC+"\n",ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER);
				 }
			 }else if(updateMemberRequest.getMemberIdentifiers().getValue()== null){
				 //Delete record for the member from database using memberId, domainName
				 memberIdentifier= populateMemberIdentifier(updateMemberRequest);
				 memberDAO.deleteFromMemberIdentifierForUpdateMember(memberIdentifier);
				 
			 }
			
			 isExternalIDsUpdate= true;
		 }
		 if(updateMemberRequest.getSocialDomain() !=null && updateMemberRequest.getSocialDomain().getDomain() !=null){
			 //update the Social Domain Data
			 if(updateMemberRequest.getSocialDomain().getUserName() != null){
				 MemberSocialDomain socialDomain  = new MemberSocialDomain();
				 socialDomain.setDomain(updateMemberRequest.getSocialDomain().getDomain());
				 socialDomain.setIdentifier(updateMemberRequest.getSocialDomain().getIdentifier());
				 socialDomain.setMemberId(updateMemberRequest.getMemberId());
				 socialDomain.setUserName(updateMemberRequest.getSocialDomain().getUserName());
				 SequenceNumber sequenceNumber = new SequenceNumber(CDSConstants.TRANS_ID_SEQ);
				 BigInteger profileId = sequenceNumberDAO.getNextSequenceNumber(sequenceNumber);
				 socialDomain.setProfileId(profileId);
				 try{
					int  i= memberDAO.updateMemberSocialDomainForMember(socialDomain);
				 }catch(DuplicateKeyException exception){
					 throw new BadRequestException(ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER_DESC+"\n",ErrorCode.UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER);
				 } 
			 }else{
			 //Delete the Social Domain Data
			 System.out.println("deleting the social Domain");
			 MemberSocialDomain socialDomain  = new MemberSocialDomain();
			 socialDomain.setDomain(updateMemberRequest.getSocialDomain().getDomain());
			 socialDomain.setMemberId(updateMemberRequest.getMemberId());
			 socialDomain.setUserName(updateMemberRequest.getSocialDomain().getUserName());
			 System.out.println(socialDomain);
			int j = memberDAO.deleteMemberSocialDomain(socialDomain);
			System.out.println(j);
			}
		 }
		 
		  if(isSMSUpdate){
			  /*ExecutorService es = Executors.newFixedThreadPool(1);
	        @SuppressWarnings("unchecked")
			final Future future = es.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateJanrain(updateMemberRequest.getSmsObject(),UPDATE_SMS,updateMemberRequest.getMemberId());
	                    	
	                        return null;
	                    }
	                });
	        es.shutdown();*/
	        
	        CDSOUtils.JanrainMemberUpdateTaskList.add(new UpdateJanrainTask(updateMemberRequest.getSmsObject(),UPDATE_SMS,updateMemberRequest.getMemberId()));
	        
		  } if(isExternalIDsUpdate){
			  
			  if(updateMemberRequest.getMemberIdentifiers().getValue() != null){
	          		//updateJanrain(updateMemberRequest.getMemberIdentifiers(),UPDATE_EXTERNALID,updateMemberRequest.getMemberId());
					  CDSOUtils.JanrainMemberUpdateTaskList.add(new UpdateJanrainTask(updateMemberRequest.getMemberIdentifiers(),UPDATE_EXTERNALID,updateMemberRequest.getMemberId()));
	          	  }else{
	          		//hard delete from janrain for the given external id info only.- Since the Data is deleted form CDSO.
	          		fetchAndDeleteJanrainExternalId(updateMemberRequest);
	          	  }
			  /*
			  ExecutorService es = Executors.newFixedThreadPool(1);
			  @SuppressWarnings("unchecked")			 
				final Future future = es.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	if(updateMemberRequest.getMemberIdentifiers().getValue() != null){
		                    		updateJanrain(updateMemberRequest.getMemberIdentifiers(),UPDATE_EXTERNALID,updateMemberRequest.getMemberId());
		                    	}else{
		                    		//hard delete from janrain for the given external id info only.- Since the Data is deleted form CDSO.
		                    		fetchAndDeleteJanrainExternalId(updateMemberRequest);
		                    	}
		                        return null;
		                    }
		                });
				es.shutdown();*/
		  }
	}
	private MemberIdentifier populateMemberIdentifier(UpdateMemberRequest updateMemberRequest){
		MemberIdentifier memberIdentifier = new MemberIdentifier();
		memberIdentifier.setMemberId(updateMemberRequest.getMemberId());
		memberIdentifier.setUserId(updateMemberRequest.getMemberIdentifiers().getValue());
		memberIdentifier.setDomainName(updateMemberRequest.getMemberIdentifiers().getName());
		return memberIdentifier;
	}
	
	private void fetchAndDeleteJanrainExternalId(UpdateMemberRequest updateMemberRequest){
		//get the janrain UniqueId for this ExternalId and Delete it from Janrain.
		String janrainUUID=(memberDAO.getMemberUUIDbyMemID(updateMemberRequest.getMemberId())).getUuid();
		if(janrainUUID!=null){
				Map<String,String> janrainReaponse = CDSOUtils.findExternalIdInfo(janrainUUID);
				Result allMemberInfo=null;
				if(janrainReaponse.get("StatusCode").equals("200") &&  janrainReaponse.get("ResponseSTR").contains("\"stat\":\"ok\""))
				{
				    ObjectMapper objectMapper = new ObjectMapper();
					try {
						allMemberInfo = objectMapper.readValue(janrainReaponse.get("ResponseSTR"), Result.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						//logger.info("Error while parsing the Json Received from Janrain for after the find of the External Id Info");
						logger.error("Error while parsing the Json Received from Janrain for after the find of the External Id Info",e);
					}
					List<ExternalIds> externalIds = allMemberInfo.getResults().get(0).getExternalIds();
					for (ExternalIds ids : externalIds) {
						if(ids.getType().equals(updateMemberRequest.getMemberIdentifiers().getName())){
							 CDSOUtils.deleteExternalIdFromJanrain(janrainUUID,ids.getId());
						}
					}
				}
				else
				{
				    logger.info("Status Code :"+ janrainReaponse.get("StatusCode")+" Response "+janrainReaponse.get("ResponseSTR")+" Exception "+janrainReaponse.get("Exception"));
				}
		}else{
			logger.info("Janrain UUID is null for member :"+updateMemberRequest.getMemberId());
		}
	}
	
	@Transactional
	public List<MemberInfo> searchMemberInfoV2(final MemberSearchRequestV2 memberSearchRequest) throws BadRequestException{
		String validationErrors = null;
		List<MemberInfo> members = null;
		try {
			validationErrors=CDSOUtils.validate(memberSearchRequest, CDSConstants.DEFAULT_PROFILE_NAME, true);
		} catch (IOException e) {
			logger.info("createOrUpdateMemberInfo :",e);
			throw new WebApplicationException(e);
		}
		if(validationErrors != null){
			 throw new BadRequestException(validationErrors,ErrorCode.GEN_INVALID_ARGUMENT);
		   
		  }
		if((memberSearchRequest.getSearchParameterName().equalsIgnoreCase("TWITTER") ||memberSearchRequest.getSearchParameterName().equalsIgnoreCase("FACEBOOK") ||memberSearchRequest.getSearchParameterName().equalsIgnoreCase("INSTAGRAM"))&&memberSearchRequest.getSocialSearch().equalsIgnoreCase("N")){
			 throw new BadRequestException("Social Search has to be set to Y for TWITTER,FACEBOOK or INSTAGRAM",ErrorCode.GEN_INVALID_ARGUMENT);
		}
		try{
			if("Y".equalsIgnoreCase(memberSearchRequest.getSocialSearch()))
			{
				members=memberDAO.searchMemberInfoV2WithSocialSearch(memberSearchRequest);
			}
			else if("N".equalsIgnoreCase(memberSearchRequest.getSocialSearch()))
			{
					members=memberDAO.searchMemberInfoV2WithOutSocialSearch(memberSearchRequest);
			
			}
		}catch(DataIntegrityViolationException rex){
			//logger.error("MemberService :BadRequestException",rex);
			throw new BadRequestException(rex.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT,rex);
		}catch(BadSqlGrammarException rex){
			//logger.error("MemberService :BadRequestException",rex);
			throw new BadRequestException(rex.getMessage(),ErrorCode.GEN_INVALID_ARGUMENT,rex);
		}
		
		if(null== members || members.size()==0)
		{
			throw new BadRequestException(ErrorCode.MEMBER_NOT_FOUND_DESC,ErrorCode.MEMBER_NOT_FOUND);
		}
		return members;
	}
	
}
