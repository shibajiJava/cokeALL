package com.ko.cds.service.janrainIntegration;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.dao.janrain.IJanrainJDBCTemplate;
import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.pojo.common.SequenceNumber;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterest;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterestDtl;
import com.ko.cds.pojo.janrainIntegration.Result;
import com.ko.cds.pojo.janrainIntegration.Results;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Client;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.member.Email;
import com.ko.cds.pojo.member.LegalAcceptances;
import com.ko.cds.pojo.member.MemberDomainProfile;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CDSOUtils;
@Component
public class JanrainServiceHelper  {
	private static final String ENVORONMENT_KEY="CDS_Evvironment";
	private static final String JANRAIN_API_KEY="Janrain_API_find";
	private static final String JANRAIN_URL_KEY="Janrain_url";
	private static final String JANRAIN_CLIENT_ID_KEY="Janrain_client_id";
	private static final String JANRAIN_SECRET_KEY="Janrain_client_secret";
	private static final String FILTER_DATE_TIME = "lastpoolingDateTime";
	private static final String ENTITY_TYPE_NAME="Janrain_type_name";
	private static final String MAX_RESULT="max_results";
	private static final String FIRST_RESULT="first_result";
	private static final String FILTER_DATE_TIME_LAST_TO_LAST = "lastOfLastpoolingDateTime";
	private static final String JANRAIN_FILE_LOCATION="janrainFileLocation";
	private static final String JANRAIN_ATTRIBUTE="janrainAttr";
	private static final Logger logger = LoggerFactory.getLogger("schedularTask");
	public static String presentTimeStmp="";
	
	 @Autowired
	private MemberDAO memberDAO;
	 @Autowired
	private SurveyDAO serveydao;
	 @Autowired
	private IJanrainJDBCTemplate janrainJDBCTemplate; 
	 @Autowired
	private SequenceNumberDAO sequenceNumberDAO;
	public List<Result> getMemberInfo() throws ClientProtocolException, IOException
	{
		List<Result> allMemberInfoList = new ArrayList<Result>(); 
		Result allMemberInfo=null;
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String apiName=configProp.get(JANRAIN_API_KEY);
		String janrainUrl = configProp.get(JANRAIN_URL_KEY);
		String environmrnt = configProp.get(ENVORONMENT_KEY);
		String entityType = configProp.get(ENTITY_TYPE_NAME);
		final String fileLOcation= configProp.get(JANRAIN_FILE_LOCATION);
		String janrainAttr = configProp.get(JANRAIN_ATTRIBUTE);
		int firstResult=0;
		String maxResult="500";
		boolean continutBreak = true;
		
		if(configProp.get(MAX_RESULT)!=null)
		{
			maxResult = configProp.get(MAX_RESULT);
		}
		
		String filterTimeStamp = (CDSOUtils.getConfigurtionPropertyUpdatable()).get(FILTER_DATE_TIME);
		String filterTimeStampLastToLast = (CDSOUtils.getConfigurtionPropertyUpdatable()).get(FILTER_DATE_TIME_LAST_TO_LAST);
		StringBuilder janrainEndPointUrl=new StringBuilder();
		//final String responseString="";
		if(environmrnt!=null)
		{
			
			
			// Create a new HttpClient and Post Header
		    //HttpClient httpclient = new DefaultHttpClient();
		    janrainEndPointUrl.append(janrainUrl+apiName);
		    
		   // HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
		    String janrainClientId  = 	configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
			String janrainSecretId  = 	configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
			List<NameValuePair> nameValuePairs=null;
			HttpClient httpclient=null;
			HttpPost httppost=null;
			
			
			
			
			
			
		    try {
		        // Add your data
		        
		        
		        do{
		        	nameValuePairs = new ArrayList<NameValuePair>(3);
			        nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
			        nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
			        nameValuePairs.add(new BasicNameValuePair("type_name", entityType));
			        if((filterTimeStampLastToLast!=null && !filterTimeStampLastToLast.equals("999")) && (filterTimeStamp!=null && !filterTimeStamp.equals("999")))
			        {
			        	//nameValuePairs.add(new BasicNameValuePair("filter", "lastUpdated>'"+filterTimeStampLastToLast+"' and lastUpdated<'"+presentTimeStmp+"'"));
			        	nameValuePairs.add(new BasicNameValuePair("filter", "lastUpdated>'"+filterTimeStampLastToLast+"'"));
			        }
			        //nameValuePairs.add(new BasicNameValuePair("filter", "lastUpdated>'"+filterTimeStamp+"'"));
			        logger.info("Filter Time Stamp : "+"filter ::"+"lastUpdated>'"+filterTimeStampLastToLast+"'");
			       // logger.info("Filter Time Stamp : "+"filter ::  lastUpdated>'"+filterTimeStamp+"'");
			        nameValuePairs.add(new BasicNameValuePair("max_results", maxResult));
			        
			        nameValuePairs.add(new BasicNameValuePair("attributes", getJanrainAttr(janrainAttr)));
		        	httpclient = new DefaultHttpClient();
		        	httppost = new HttpPost(janrainEndPointUrl.toString());
		        	String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:kT8duWdD7xJh").getBytes("UTF-8"));
				       
				        
				    httppost.addHeader("AUTHORIZATION","Basic "+encoded);
			        nameValuePairs.add(new BasicNameValuePair("first_result",String.valueOf(firstResult)));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			        // Execute HTTP Post Request
			        presentTimeStmp = getUTCTimeForJanrain();
			        HttpResponse response = httpclient.execute(httppost);
			        if(response.getStatusLine().getStatusCode()==200)
			        {
			        	final String responseString = new BasicResponseHandler().handleResponse(response);
			        	//System.out.println("received from Janrain : "+responseString);
			        	
			        	logger.info("===================================OUTPUT Janrain=========================");
			        	//logger.info(responseString);
			        	logger.info("done");
			        	logger.info("===================================OUTPUT Janrain=========================");
			        	logger.info("local time : " + new Date());
			        	//Write JSON in file 
			        	ExecutorService service = Executors.newFixedThreadPool(1);
			        	@SuppressWarnings("unchecked")
						final Future futureUpdateAddress = service.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	writeJanrainJSONtoFile("\n"+responseString,fileLOcation);
		                        return null;
		                    }
		                });
			        	
			        	service.shutdown();
			        	
			        	
						ObjectMapper objectMapper = new ObjectMapper();
						//System.out.println("Value is : "+responseString);
						
						allMemberInfo = objectMapper.readValue(responseString, Result.class);
						logger.info("1. data getting form Janrain : "+allMemberInfo.getResult_count());
						
						allMemberInfoList.add(allMemberInfo);
						//continutBreak=false;
						
						
						if((allMemberInfo.getResult_count().intValue())<1000)
						{
							continutBreak=false;
						}
						else
						{
							if(firstResult==0)
							{
								firstResult=Integer.valueOf(maxResult);
							}
							else
							{
								firstResult=firstResult+Integer.valueOf(maxResult);
							}
							
						}
			        }
			        else
			        {
			        	logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
			        }
		        }while(continutBreak);
		        	
		        
		    } catch (ClientProtocolException clientExx) {
		    	//logger.info(responseString);
		    	logger.error("getMemberInfo : Client Protocol Exception ",clientExx);
		       
		    } catch (IOException ioExx) {
		    	//logger.info(responseString);
		    	logger.error("getMemberInfo :File Not found Expection ",ioExx);
		        
		    }
		    catch (Exception exx) {
		    	//logger.info(responseString);
		    	logger.info("Value is : "+allMemberInfoList);
		    	logger.error("getMemberInfo :File Not found Expection ",exx);
		    	exx.printStackTrace();
		        
		    }
		    
		}
		logger.info("Total List size recived from Janrain : "+allMemberInfoList.size());
		return allMemberInfoList;
	}
	
	
	
	private void writeJanrainJSONtoFile(String responseString,String fileLocation) {
		
		try
		{
			InputStream is = new ByteArrayInputStream(responseString.getBytes());
	    	BufferedReader bis = new BufferedReader(new InputStreamReader(is));
	    	BufferedOutputStream  bos = new BufferedOutputStream(new FileOutputStream(fileLocation,true));
            int data;  
	    	   while ((data = bis.read()) != -1) {  
	    	    
	    	    bos.write(data);  
	    	   }  
	    	   bos.flush();
	    	   bos.close();
	    	   bis.close();
		}
		catch(Exception exx)
		{
			logger.error("writeJanrainJSONtoFile : exception in "+exx);
		}
	
	}



	private String getUTCTimeForJanrain() {
		Date localTime = new Date(); 
    	DateFormat converter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    	   
        //getting GMT timezone, you can get any timezone e.g. UTC
        converter.setTimeZone(TimeZone.getTimeZone("GMT"));
       
        
       // logger.info("time in GMT :(Janrain Last time stamp) " + converter.format(localTime));
        //presentTimeStmp = converter.format(localTime);
        return converter.format(localTime)+" +0000";
		
	}



	public MemberInfo getMemberByUUID(String UUID)
	{
		
		MemberInfo memberInfo =memberDAO.getmemberInfoWithUUIDForJanrainSync(UUID);
		return memberInfo; 
	}
	
	
	/*public List<Results> getFilterMembersOnLastUpdated(Result janrainResult, java.util.Date lastPoolingDate) throws ParseException
	{
		List<Results>  returnList = new ArrayList<Results>();
		SimpleDateFormat janrainLastUpdatedDate ;
		for(Results result : janrainResult.getResults())
		{
			String lastUpdatedDate = result.getLastUpdated();
			janrainLastUpdatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSZ", Locale.US);
			java.util.Date lastUpdatedDateJanrain =  janrainLastUpdatedDate.parse(lastUpdatedDate);
			if(lastUpdatedDateJanrain.after(lastPoolingDate))
			{
				returnList.add(result);
			}
		}
		
		return returnList;
		
	}*/
	
	
	
	public boolean updateMemberAddressDAO(Address MemberAddress)
	{
		memberDAO.updateMemberAddress(MemberAddress);
		return true;
	}
	
	
	public boolean insertMemberAddressDAO(Address MemberAddress)
	{
		memberDAO.insertAddress(MemberAddress);
		return true;
	}
	
	public Email getEmailByMemberID(BigInteger memId)
	{
		Email memEmail = memberDAO.getEmailAddressByMemId(memId);
		return memEmail;
	}
	
	public boolean updateMemberEmailDAO(Email emailDao)
	{
		memberDAO.updateEmailAddress(emailDao);
		return true;
	}
	
	public boolean updateMemberInfo(MemberInfo memberInfo)
	{
		boolean isTrue=true;
		String validationErrors = null;
		try
		{
			/*validationErrors=CDSOUtils.validate(memberInfo, CDSConstants.DEFAULT_PROFILE_NAME, true);
			if(validationErrors != null)
			{
				logger.info("Validation Error : "+validationErrors+" Unable to Update Data for UUID :"+memberInfo.getUuid());
			}
			else
			{
				
				memberDAO.updateMemberInfoForJanrain(memberInfo);
			}*/
			memberDAO.updateMemberInfoForJanrain(memberInfo);
		} catch (Exception e) {
			isTrue=false;
			logger.error("createOrUpdateMemberInfo :",e);
		}
		return isTrue;
	}
	
	
	public boolean inserUpdateEmailAddress(List<Address> memberListInsert,
			List<Address> memberListUpdate) {
		boolean returnValue=true;
		
		Address addressUpdate = new Address();
		Address addressInsert = new Address();
		try{
			if(memberListInsert.size()>0)
			{
				addressInsert.setMemberAddressListJanrain(memberListInsert);
				memberDAO.insertAddressJanrain(addressInsert);
				
			}
			else
			{
				/*addressUpdate.setMemberAddressListJanrain(memberListUpdate);
				for(Address address : memberListUpdate)
				{
					updateMemberAddressDAO(address);
				}*/
				janrainJDBCTemplate.updateMemberAddressJanrain(memberListUpdate);
				//memberDAO.updateMemberAddressForJantain(addressUpdate);
			}
		}
		catch(Exception exx)
		{
			returnValue=false;
			logger.error(" inserUpdateEmailAddress : Exception in Email Update/Insert ",exx);
		}
		return returnValue;
	}
	
	
	public boolean updateInsertCommunicationOpts(List<CommunicationOptIn> communicationOptsInserList,List<CommunicationOptIn> communicationOptsUpdateList)
	{
		boolean returnValue=true; 
		try{
			CommunicationOptIn communicationOptInUpdate = new CommunicationOptIn();
			CommunicationOptIn communicationOptInInsert = new CommunicationOptIn();
			if(communicationOptsInserList.size()>0)
			{
				communicationOptInInsert.setJanrainCommOptList(communicationOptsInserList);
				serveydao.insertCommunicationOptsJanrain(communicationOptInInsert);
			}
			if(communicationOptsUpdateList.size()>0)
			{
				/*communicationOptInUpdate.setJanrainCommOptList(communicationOptsUpdateList);
				
				for(CommunicationOptIn commOpt : communicationOptsUpdateList)
				{
					serveydao.updateCommunicationOptsJanrain(commOpt);
					//updateOptsLegal(commOpt);
				}*/
				janrainJDBCTemplate.updateCommunicationOptsJanrain(communicationOptsUpdateList);
			}
		}
		catch(Exception exx)
		{
			returnValue=false;
			logger.error("Exception in CommunicationOpts Insert/Update ",exx);
		}
		return returnValue;
	}
	
	
	public boolean updateOptsLegal(CommunicationOptIn cdsOpts)
	{
		String validationErrors = null; 
		boolean returnValue = true;
		try
		{
			validationErrors=CDSOUtils.validate(cdsOpts, CDSConstants.DEFAULT_PROFILE_NAME, true);
			
		} catch (IOException e) {
			logger.info("createOrUpdateMemberInfo :",e);
		}
		
		
		if(validationErrors != null){
			logger.info("Validation Error : "+validationErrors+" Unable to Update Data for UUID :"+cdsOpts.getMemberId());
			returnValue=false;
		}
		else
		{
			
			serveydao.updateOpts(cdsOpts);
		}
		
		
		return returnValue; 
		
	}
	
	
	public boolean insertOptsLegal(CommunicationOptIn cdsOpts)
	{
		String validationErrors = null; 
		boolean returnValue = true;
		try
		{
			validationErrors=CDSOUtils.validate(cdsOpts, CDSConstants.DEFAULT_PROFILE_NAME, true);
			
		} catch (IOException e) {
			logger.error("createOrUpdateMemberInfo :",e);
		}
		
		
		if(validationErrors != null){
			logger.error("createOrUpdateMemberInfo : Validation Error : "+validationErrors+" Unable to Update Data for UUID :"+cdsOpts.getMemberId());
			returnValue=false;
		}
		else
		{
			logger.info("insertOptsLegal : validationErrors is null inserting data for "+cdsOpts.getMemberId());
			serveydao.insertOpts(cdsOpts);
		}
		
		
		return returnValue; 
		
	}
	
	public boolean insertUpdateProfile(MemberDomainProfile cdsProfile, boolean flag){
		String validationErrors = null; 
		boolean returnValue = true;
		try
		{
			validationErrors=CDSOUtils.validate(cdsProfile, CDSConstants.DEFAULT_PROFILE_NAME, true);
			
		} catch (IOException e) {
			logger.error("insertUpdateProfile :",e);
		}
		if(validationErrors != null){
			logger.error("Validation Error : "+validationErrors+" Unable to Validate Data for UUID :"+cdsProfile.getMemberId());
			returnValue=false;
		}
		else
		{
			if(flag){
				logger.info("insertUpdateProfile :inserting data for "+cdsProfile.getMemberId());
				memberDAO.insertProfile(cdsProfile);
			}else{
				logger.info("insertUpdateProfile : update data for "+cdsProfile.getMemberId());
				memberDAO.updateProfile(cdsProfile);
			}
		}
		return returnValue; 
	}
	
	public boolean insertUpdateLegalAcceptance(List<LegalAcceptances> cdsLegalAcceptance)
	{
		return true;
	}
	
	
	
	public boolean insertUpdateLegalAcceptance(LegalAcceptances cdsLegalAcceptance, boolean flag){
		String validationErrors = null; 
		boolean returnValue = true;
		try
		{
			if(flag){
			logger.info("insertUpdateLegalAcceptance : inserting data for "+cdsLegalAcceptance.getMemberId());
			memberDAO.insertLegalAcceptance(cdsLegalAcceptance);
		}else{
			logger.info("insertUpdateLegalAcceptance : updating data for "+cdsLegalAcceptance.getMemberId());
			memberDAO.updateLegalAcceptance(cdsLegalAcceptance);
		}
		}catch(Exception e){
			logger.error("insertUpdateLegalAcceptance memberId"+cdsLegalAcceptance.getMemberId(),e);
		}	
		return returnValue; 
	}
	public void insertUpdateClient(List<Client> janrainInsertClientList ,List<Client> janrainUpdateClientList)
	{
		if(janrainInsertClientList != null && janrainInsertClientList.size()>0){
			janrainJDBCTemplate.insertClientForJanrain(janrainInsertClientList);
		}
		if(janrainUpdateClientList!= null && janrainUpdateClientList.size()>0){
			janrainJDBCTemplate.updateClientForJanrain(janrainUpdateClientList);
		}
		 
	}
	
	
	
	public boolean insertExternalId(MemberIdentifier memberIdentifire)
	{
		String validationErrors = null; 
		boolean returnValue = true;
		
		try
		{
			validationErrors=CDSOUtils.validate(memberIdentifire, CDSConstants.DEFAULT_PROFILE_NAME, true);
			
		} catch (IOException e) {
			logger.error("createOrUpdateMemberInfo :",e);
		}
		
		
		if(validationErrors != null){
			logger.error("Validation Error : "+validationErrors+" Unable to Update Data for Member ID :"+memberIdentifire.getMemberId());
			returnValue=false;
		}
		else
		{
			logger.info("insertExternalId : validationErrors is null inserting data for "+memberIdentifire.getMemberId());
			memberDAO.insertMemberIdentifier(memberIdentifire);
		}
		return returnValue;
	}
	
	
	public boolean updateExternalId(MemberIdentifier memberIdentifire)
	{
		String validationErrors = null; 
		boolean returnValue = true;
		
		try
		{
			validationErrors=CDSOUtils.validate(memberIdentifire, CDSConstants.DEFAULT_PROFILE_NAME, true);
			
		} catch (IOException e) {
			logger.error("createOrUpdateMemberInfo :",e);
		}
		
		
		if(validationErrors != null){
			logger.error("Validation Error : "+validationErrors+" Unable to Update Data for Member ID :"+memberIdentifire.getMemberId());
			returnValue=false;
		}
		else
		{
			logger.info("updateExternalId : validationErrors is null inserting data for "+memberIdentifire.getMemberId());
			memberDAO.updateMemberIdentifier(memberIdentifire);
		}
		
		return returnValue;
		
	}
	
	public boolean inserUpdateEmailAddress(Email emailAddress , boolean insertUpdateFlg)
	{

		String validationErrors = null; 
		boolean returnValue = true;
		
		try
		{
			/*validationErrors=CDSOUtils.validate(emailAddress, CDSConstants.DEFAULT_PROFILE_NAME, true);
			if(validationErrors != null){
				logger.info("Validation Error : "+validationErrors+" Unable to Update Data for Member ID :"+emailAddress.getMemberId());
				returnValue=false;
			}
			else
			{*/
				
				if(insertUpdateFlg)
				{
					memberDAO.insertEmailAddress(emailAddress);
				}
				else
				{
					memberDAO.updateEmailAddress(emailAddress);
				}
				
			//}
		} catch (Exception e) {
			logger.info("EmailAddress Update?insert Exception  :",e);
		}
		
		return returnValue;
	}
	
	
	public void updateProfileInterest(List<CdsoProfileInterest> allInterset)
	{
	
		//try{
			List<CdsoProfileInterest> profileInterestInsertList = new ArrayList<CdsoProfileInterest>();
			List<CdsoProfileInterestDtl> profileInterestDtlInsertList = new ArrayList<CdsoProfileInterestDtl>();
			//int i=0;
			// Delete all record from profile_interest and profile_interest_dtm for profile_id and member_id
			if(allInterset != null && allInterset.size() >0){
				CdsoProfileInterest profileInterestIdForDelete = allInterset.get(0);
				memberDAO.deleteFromProfileInterestDTLForJanrain(profileInterestIdForDelete);
				logger.info("Delete cdsoProfileInterest MemberId :"+profileInterestIdForDelete.getMemberId()+" ProfileId :"+profileInterestIdForDelete.getProfileId());
				memberDAO.deleteProfileInterestForJanrain(profileInterestIdForDelete);
				
			}
			
			for(CdsoProfileInterest cdsoProfileInterest:allInterset)
			{
				/*if("photos".equals(cdsoProfileInterest.getInterestName())){
					logger.info("photo details");
				}
				logger.info("hit count :"+i+" "+cdsoProfileInterest.getInterestName());
				i++;*/
				if(cdsoProfileInterest.getProfileInterestDtl()!=null && cdsoProfileInterest.getProfileInterestDtl().size()>0)
				{
					BigInteger profileInterestId= sequenceNumberDAO.getNextSequenceNumber(new SequenceNumber(CDSConstants.PROFILE_INTEREST_ID_SEQ));
					cdsoProfileInterest.setPrifileInterestId(profileInterestId);
					profileInterestInsertList.add(cdsoProfileInterest);
				    
					for (CdsoProfileInterestDtl cdsoProfileInterestDtl : cdsoProfileInterest.getProfileInterestDtl()) {
						
						cdsoProfileInterestDtl.setProfileInteretId(profileInterestId);
						profileInterestDtlInsertList.add(cdsoProfileInterestDtl);
						
					}
					
				}
				
			}
			
			janrainJDBCTemplate.insertProfileInterestForJanrain(profileInterestInsertList);
			logger.info("profile interest inserted >>>>>>>>>>>>>>>>>>>>>>>");
			janrainJDBCTemplate.insertProfileInterestDtlForJanrain(profileInterestDtlInsertList);
			logger.info("profile interest DTL inserted >>>>>>>>>>>>>>>>>>>>>>>");
			/*memberDAO.inserUpdateProfileInterest(allInterset);*/
		//}catch(Exception e){
			//logger.info("updateProfileInterest >>>>>>>"+e);
		//}
		
	}
	
	@Transactional(rollbackFor={RuntimeException.class,WebApplicationException.class})
	public void updatePhoneNumber(List<PhoneNumber> phoneNumbers) 
	{
	//	boolean isSuccess=true;
		
		//memberDAO.phoneNumberDelete(phoneNumberInsertDelete);
		janrainJDBCTemplate.deletePhoneNumberForJanrain(phoneNumbers);
		logger.info("All Phone Number Deleted in table, total deleted data Number "+phoneNumbers.size());
		logger.info("Insertig new data in phoneNumber table for member Id  ");
		//logger.info("Total Inseret will be for "+phoneNumberInsertDelete.getPhoneNumberList().size());
		try{
			memberDAO.insertPhoneNumberForJanrain(phoneNumbers);
		}catch(Exception e){
			logger.error("Janrain updatePhoneNumber:"+e);
			new RuntimeException(e);
		}
	}
	
	
	public boolean insertUpdateMemberIdentifire(
			List<MemberIdentifier> memberIndInsert,
			List<MemberIdentifier> memberIndUpdate) {
	
		boolean retrunValue=true;
		MemberIdentifier memberIdentifierInsert =  new MemberIdentifier();
		MemberIdentifier memberIdentifierUpdate =  new MemberIdentifier();
		try{
			if(memberIndInsert.size()>0)
			{
				memberIdentifierInsert.setMemberIndList(memberIndInsert);
				memberDAO.insertMemberIdentifierJanrain(memberIdentifierInsert);
			}
			if(memberIndUpdate.size()>0)
			{
				/*memberIdentifierUpdate.setMemberIndList(memberIndInsert);
				//memberDAO.insertMemberIdentifierJanrain(memberIdentifierUpdate);
				for(MemberIdentifier memInd : memberIndUpdate)
				{
					updateExternalId(memInd);
				}*/
				janrainJDBCTemplate.updateExternalIdsJanrain(memberIndUpdate);
				
			}
		}
		catch(Exception exx)
		{
			logger.error("insertUpdateMemberIdentifire :Error in Member Identifier inser/Update"+retrunValue+"Exception is "+exx);
			retrunValue = false;
			
		}
		return retrunValue;
	}

	
	
	public static void main(String args[]) throws AuthenticationException, ClassNotFoundException, SQLException
	{
		
			
		
		Date localTime = new Date(); 
    	DateFormat converter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    	   
        //getting GMT timezone, you can get any timezone e.g. UTC
        converter.setTimeZone(TimeZone.getTimeZone("GMT"));
       
        logger.info("local time : " + localTime);;
        logger.info("time in GMT :(Janrain Last time stamp) " + converter.format(localTime));
        String ss1 = converter.format(localTime)+ " +0000";
        
		Result allMemberInfo=null;
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
	
			final TimeZone utc = TimeZone.getTimeZone("UTC");
			dateFormatter.setTimeZone(utc);
			Calendar cal = GregorianCalendar.getInstance(utc);
			Timestamp todayTimeStamp = new Timestamp(cal.getTimeInMillis());
	
			//System.out.println(dateFormatter.format(todayTimeStamp));

			
		
		    JanrainServiceHelper hepler = new JanrainServiceHelper();

			StringBuilder janrainEndPointUrl=new StringBuilder();
			//System.out.println("UTC Time Stamp  : "+ CDSOUtils.formatDateDBDate(dao.getCurrentDBTime())+"+0000");
		    //janrainEndPointUrl.append("https://api.ccnag.com/janrain"+"/entity.find");
			janrainEndPointUrl.append("https://coca-cola.janraincapture.com"+"/entity.find");
		    
		    
		    int firstRes = 1;
		   // for(int i=0;i<10000;i++)
		  //  {
		    try {
		    	HttpClient httpclient = new DefaultHttpClient();
		    	HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("client_id", "saw8gd3sf9xj36axg8d5x7mqmfn4yfrv"));
		        nameValuePairs.add(new BasicNameValuePair("client_secret", "a4r7drq3fjgbrnp3ewzuqjmwkewdeefx"));
		        nameValuePairs.add(new BasicNameValuePair("type_name", "user"));
		       //nameValuePairs.add(new BasicNameValuePair("attributes", "[\"uuid\" , \"communicationOpts\" ,\"mailingAddress\",\"familyName\",\"profiles.domain\",\"profiles.identifier\",\"profiles.id\",\"profiles.profile.preferredUsername\"]"));
		        nameValuePairs.add(new BasicNameValuePair("attributes", "[\"uuid\"]"));
		        
		        
		        //nameValuePairs.add(new BasicNameValuePair("first_result",String.valueOf(firstRes)));
		        
		        //UsernamePasswordCredentials creds = new UsernamePasswordCredentials("ibm-cds","jyceckPX39y8");
		        //Header bs = new BasicScheme().authenticate(creds, httppost);
		       // httppost.addHeader("Proxy-Authorization", bs.getValue());
		        
		        
		       // String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:kT8duWdD7xJh").getBytes("UTF-8"));
		       
		        
		      //  httppost.addHeader("AUTHORIZATION","Basic "+encoded);
		        
		        
		        logger.info("First Resullt count "+firstRes);
		        nameValuePairs.add(new BasicNameValuePair("max_results", "10000"));
		       // nameValuePairs.add(new BasicNameValuePair("filter", "lastUpdated>'2014-10-20 11:10:41.378436+0000' and lastUpdated<'2014-10-17 07:33:41.378436+0000'"));
		                                                                         // 2014-11-07 10:23:05.128
		        nameValuePairs.add(new BasicNameValuePair("filter", "lastUpdated>'2016-02-13 00:00:04.000396 +0000' and lastUpdated<'2016-02-13 03:00:04.000396 +0000'"));
		      //  String uuid = "d7d1a94a/-0fbf/-4a4d/-81ca/-9d6d9e7678a";
		        //String uuid = "1222";
		        //nameValuePairs.add(new BasicNameValuePair("filter", "'uuid'=\"d7d1a94a-0fbf-4a4d-81ca-9d6d9e7678a\""));
		       // nameValuePairs.add(new BasicNameValuePair("filter", "familyName ='Smithson'"));
		        //nameValuePairs.add(new BasicNameValuePair("filter", "Mobile ='111111'"));
		        
		        //nameValuePairs.add(new BasicNameValuePair("filter", "uuid ='eb9c8782-9ab3-409b-9ff4-653ce26f65d8'"));
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        logger.info("===================================OUTPUT Janrain=========================");
		        // Execute HTTP Post Request
		        String responseString="";
		        HttpResponse response = httpclient.execute(httppost);
		        if(response.getStatusLine().getStatusCode()==200)
		        {
		        	responseString = new BasicResponseHandler().handleResponse(response);
		        	
		        	logger.info(responseString);
		        	logger.info("===================================OUTPUT Janrain=========================");
					ObjectMapper objectMapper = new ObjectMapper();
					allMemberInfo = objectMapper.readValue(responseString, Result.class);
					List<Results> allUUID  = allMemberInfo.getResults();
					
					Connection conn = getConnection();
					File fileU = new File("c:\\UpdateOnly.txt");
					File fileN = new File("c:\\NewMemberOnly.txt");
					if(!fileU.exists())
					{
						fileU.createNewFile();
					}
					if(!fileN.exists())
					{
						fileN.createNewFile();
					}
					
					for(int i=0;i<allUUID.size();i++)
					{
						
						String newMember="";
						String updateMember="";
						Results rr = allUUID.get(i);
						String uuid = rr.getUuid();
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt.executeQuery("select member_id from cdsousr.member where uuid='"+uuid+"'");
						
						if(rs!=null)
						{
							while (rs.next()) {
								  String memID = rs.getString("member_id");
								  updateMember = memID+":"+uuid;
									 
								  BufferedWriter out = new BufferedWriter(new FileWriter(fileU,true));
								  out.write(updateMember);
								  out.write("\n");
								  out.close();
						         
						          
								  //System.out.println(memID + "\n");
								}
							
								rs.close();
						}
						else
						{
							
							System.out.println("RS NULL FOR MEMBER UUID : "+newMember);
							
								newMember = uuid;
							  
								 BufferedWriter out = new BufferedWriter(new FileWriter(fileN,true));
								  out.write(newMember);
								  out.write("\n");
								  out.close();
					         
						}
						stmt.close();
						
						
					}
					 
					conn.close();
		        }
		        else
		        {
		        	responseString = new BasicResponseHandler().handleResponse(response);
		        	logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode()+"||   "+responseString);
		        }
		        
		       // Files.write(Paths.get("C:\\cds\\files\\out00"+i+".txt"), responseString.getBytes());
		        firstRes=firstRes+1000;
		        //System.out.println("Loop Index "+i);
		        logger.info("==============================DONE==========================");
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//}
	}

	private static Connection  getConnection() throws ClassNotFoundException, SQLException
    {
       Class. forName ( "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver"  );
       Connection  connection =
               DriverManager.getConnection("jdbc:db2://10.124.117.10:60006/cdso","cdsoapi","cdsoapi");

       System. out .println( "From DAO, connection obtained " );
       return connection;
    }

	public boolean insertUpdateProfile(
			List<MemberDomainProfile> profileListInsert,
			List<MemberDomainProfile> profileListUpdate) {
		
		
		boolean returnValue=true;
		try{
			if(profileListInsert!=null && profileListInsert.size()>0)
			{
				MemberDomainProfile memberProfileInsert =  new MemberDomainProfile();
				memberProfileInsert.setDomainProfileList(profileListInsert);
				memberDAO.insertProfileForJanrainBatch(memberProfileInsert);
			}
			if(profileListUpdate!=null && profileListUpdate.size()>0)
			{
				/*for(MemberDomainProfile cdsProfile : profileListUpdate)
				{
					memberDAO.updateProfile(cdsProfile);
				}*/
				janrainJDBCTemplate.updateMemberDomainProfileJanrain(profileListUpdate);
			}
		}
		catch(Exception exx)
		
		{
			returnValue=false;
			logger.info("Exception in domain profile Insert/Update ",exx);
		}
		
		return returnValue; 
		
	}


	private String getJanrainAttr(String attrStr)
	{
		StringBuffer returnValue = new StringBuffer();

		String[] attrStrArr= attrStr.split(",");
		returnValue.append("[");
		for(int i=0;i<attrStrArr.length;i++)
		{
			if(!"".equals(attrStrArr[i]) )
			{
				if(i!=0)
				{
					returnValue.append(",");
				}
			}
			returnValue.append("\""+attrStrArr[i]+"\"");
		}
		returnValue.append("]");

		
		
		return returnValue.toString();
				
	}
	
	
	
}
