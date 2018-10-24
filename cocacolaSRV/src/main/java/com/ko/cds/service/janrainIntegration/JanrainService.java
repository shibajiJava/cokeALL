package com.ko.cds.service.janrainIntegration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.GET;

import org.apache.http.client.ClientProtocolException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.survey.SurveyDAO;
import com.ko.cds.pojo.janrainIntegration.Activities;
import com.ko.cds.pojo.janrainIntegration.Addresses;
import com.ko.cds.pojo.janrainIntegration.Books;
import com.ko.cds.pojo.janrainIntegration.Cars;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterest;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterestDtl;
import com.ko.cds.pojo.janrainIntegration.Children;
import com.ko.cds.pojo.janrainIntegration.Clients;
import com.ko.cds.pojo.janrainIntegration.CommunicationOpt;
import com.ko.cds.pojo.janrainIntegration.Emails;
import com.ko.cds.pojo.janrainIntegration.ExternalIds;
import com.ko.cds.pojo.janrainIntegration.Food;
import com.ko.cds.pojo.janrainIntegration.Heroes;
import com.ko.cds.pojo.janrainIntegration.InterestedInMeeting;
import com.ko.cds.pojo.janrainIntegration.Interests;
import com.ko.cds.pojo.janrainIntegration.JobInterests;
import com.ko.cds.pojo.janrainIntegration.Languages;
import com.ko.cds.pojo.janrainIntegration.LanguagesSpoken;
import com.ko.cds.pojo.janrainIntegration.LegalAcceptance;
import com.ko.cds.pojo.janrainIntegration.LookingFor;
import com.ko.cds.pojo.janrainIntegration.MailingAddress;
import com.ko.cds.pojo.janrainIntegration.Movies;
import com.ko.cds.pojo.janrainIntegration.Music;
import com.ko.cds.pojo.janrainIntegration.Organizations;
import com.ko.cds.pojo.janrainIntegration.PhoneNumberJanrain;
import com.ko.cds.pojo.janrainIntegration.Pphotos;
import com.ko.cds.pojo.janrainIntegration.Profile;
import com.ko.cds.pojo.janrainIntegration.Profiles;
import com.ko.cds.pojo.janrainIntegration.Result;
import com.ko.cds.pojo.janrainIntegration.Results;
import com.ko.cds.pojo.janrainIntegration.ShippingAddress;
import com.ko.cds.pojo.janrainIntegration.Sports;
import com.ko.cds.pojo.janrainIntegration.Tags;
import com.ko.cds.pojo.janrainIntegration.TurnOffs;
import com.ko.cds.pojo.janrainIntegration.TurnOns;
import com.ko.cds.pojo.janrainIntegration.TvShows;
import com.ko.cds.pojo.janrainIntegration.Urls;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Client;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.member.Email;
import com.ko.cds.pojo.member.LegalAcceptances;
import com.ko.cds.pojo.member.MemberDomainProfile;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.pojo.survey.OptPreference;
import com.ko.cds.request.survey.OptsRequest;
import com.ko.cds.response.survey.GetOptsResponse;
import com.ko.cds.utils.CDSOUtils;
@Component

public class JanrainService {

	private static final Logger logger =  LoggerFactory.getLogger("schedularTask");
    @Autowired
    private JanrainServiceHelper JanrainServiceHepler;
    @Autowired
    private SurveyDAO surveyDAO;
    
    
    private static final String ADDRESS_TYPE_MELLING="Mailing";
    private static final String ADDRESS_TYPE_SHIPPING="Shipping";
    private static final String FILTER_DATE_TIME = "lastpoolingDateTime";
    private static final String FILTER_DATE_TIME_OF_LAST_RUN = "lastOfLastpoolingDateTime";
   private static Map<String, String> returnProfileObjectMaper = new HashMap<String, String>();
    private static final String MAPPER_FILE_LOCATION = "resources/ProfileObjectMutatorMapper.properties";
    private static final String DATE_FORMAT_WITHOUT_TIMEZONE ="yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd HH:mm:ss.SSSSSSZ";
    private static final String TIMEZONE="GMT";
    
   //CountDownLatch latch = new CountDownLatch(9);
    SimpleDateFormat janrainLastPollingDate ;
    
    
   	@GET
    public void updateMemberInfoFJanrain() 
    {
    	try
    	{
    		logger.info("======================================== Starting of Janrain Schedular in updateMemberInfoFJanrain ===============================");
    		Calendar calendar = Calendar.getInstance();
    		java.util.Date now = calendar.getTime();
    		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
    		logger.info("Start Time Stamp "+currentTimestamp);
    		// Janrain calling for all member info data from server 
    		List<Result> results =  JanrainServiceHepler.getMemberInfo();
    		
    		
    		for(Result result:results)
    		{
    		
    			logger.info("1.2 Data size in per List : "+result.getResult_count());
	    		List<Results> filteredResult=null;
	    		
	    		if(result!=null && result.getResults().size()>0)
	    		{
	    			logger.info("List Size : "+result.getResults().size());
	    			Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
	    			String  lastpoolingDateTimeStr = (CDSOUtils.getConfigurtionPropertyUpdatable()).get(FILTER_DATE_TIME);
	    			if(lastpoolingDateTimeStr!=null)
	    			{
	    				janrainLastPollingDate = new SimpleDateFormat(DATE_FORMAT_WITH_TIMEZONE, Locale.US);   				
	    				filteredResult=result.getResults();
	    				if(filteredResult!=null && filteredResult.size()>0)
	    				{
	    					int recordCount=0;
	    					double totalTimeTaken=0;
	    					for(Results janrainMemberinfo :filteredResult )
	    					{
	    						try{
		    						logger.info("Janrain all for UUID "+janrainMemberinfo.getUuid());
		    						MemberInfo memberinfo= JanrainServiceHepler.getMemberByUUID(janrainMemberinfo.getUuid().trim());
		    						if(memberinfo!=null)
		    						{
		    							recordCount++;
		    							long start = System.nanoTime();
		    							janrainToCdsoUpdate(janrainMemberinfo, memberinfo);
		    							long end = System.nanoTime();
		    					        long elapsedTime = end - start;
		    					        double seconds = (double) elapsedTime / 1000000000.0;
		    					        totalTimeTaken=totalTimeTaken+seconds;
		    					        logger.info("===========Time Taken by memberId ============> "+memberinfo.getMemberId()+" time"+String.valueOf(seconds));
		    						}
		    						else
		    						{
		    							logger.info("No Data Found for Member UUID :"+janrainMemberinfo.getUuid());
		    						}
	    						}catch(MyBatisSystemException mybatiesExx)
	    				    	{
	    							
	    							//mybatiesExx.printStackTrace();
	    				    		logger.error("updateMemberInfoFJanrain  "+ mybatiesExx);
	    				    	}catch(RuntimeException Ex)
	    				    	{
	    				    		logger.error("updateMemberInfoFJanrain :",Ex);
	    				    		//logger.error("DB Error of Data  "+ Ex);
	    				    	}
	    						
	    					}
	    					logger.info("time took to process"+ recordCount +"records - "+totalTimeTaken+" secs");
	    				}
	    				else
	    				{
	    					logger.info("Filter size null");
	    				}
	    			}
	    			
	    			
	    		}
	    		else
	    		{
	    			logger.info("getMemberInfo is null or List Size 0");
	    		}
    		}
    		
    		
    		
    	}
    	
    	catch(ClientProtocolException cpExx)
    	{
    		logger.error("ClientProtocolException : Error in Janrain Service call "+ cpExx);
    	}
    	catch(IOException ioExx)
    	{
    		logger.error("IOException : Configuration file not Found "+ ioExx);
    	}
    	catch(Exception exx)
    	{
    		logger.error("Exception : Configuration file not Found "+ exx);
    		exx.printStackTrace();
    	}
    	
    	logger.info("Update for Prop File ");
    	updateInsertPoolerTime();
    	/*Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());*/
		
		logger.info("======================================== End of Janrain Schedular ===============================");
		/*logger.info("End Time Stamp "+currentTimestamp);*/
    	
    }

   	@Transactional
	private void janrainToCdsoUpdate(final Results janrainMemberinfo,
			final MemberInfo memberinfo) {
   		ExecutorService service = Executors.newFixedThreadPool(9);
   		
		if(memberinfo!=null)
		{
			try{
				
				
				//update Address
				logger.info("=========> calling update member Address");
				@SuppressWarnings("unchecked")
				final Future futureUpdateAddress = service.submit(new Callable() {
                    public Object call() throws Exception {
                    	updateMemberAddress(memberinfo.getMemberId(), memberinfo.getAddresses(),janrainMemberinfo.getMailingAddress(),janrainMemberinfo.getShippingAddress());
                        return null;
                    }
                });
				
				@SuppressWarnings("unchecked")
				final Future futureEmail = service.submit(new Callable() {
                    public Object call() throws Exception {
                    	updateEmails(memberinfo.getMemberId(),janrainMemberinfo,janrainMemberinfo.getEmails(),memberinfo.getJanrainEmailAddress(),janrainMemberinfo.getEmailVerified());
                        return null;
                    }
                });
				
				
				
				logger.info("=========> calling update memeber Info ");
				@SuppressWarnings("unchecked")
				final Future futureMemberInfo = service.submit(new Callable() {
                    public Object call() throws Exception {
                    	updateMemberInfo(memberinfo,janrainMemberinfo);
                        return null;
                    }
                });
				
				
				
				// update opts 
				OptsRequest optsRequest = new OptsRequest();
				optsRequest.setMemberId(memberinfo.getMemberId());
				final GetOptsResponse getOptsResponse = surveyDAO.getOpts(optsRequest);
				logger.info("=========> calling update for opts ");
				@SuppressWarnings("unchecked")
				final Future futureOpts = service.submit(new Callable() {
                    public Object call() throws Exception {
                    	if(getOptsResponse!= null){
                    		updateCommOpts(janrainMemberinfo.getCommunicationOpts(),getOptsResponse.getOpts(),memberinfo.getMemberId());
                    	}else{
                    		updateCommOpts(janrainMemberinfo.getCommunicationOpts(),null,memberinfo.getMemberId());
                    	}
                        return null;
                    }
                });
				
				// update externalIds
				if(janrainMemberinfo.getProfiles()!=null && janrainMemberinfo.getProfiles().size()>0)
				{
					@SuppressWarnings("unchecked")
					final Future futureProfile = service.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateProfiles(janrainMemberinfo.getProfiles(), memberinfo.getProflies(), memberinfo.getMemberId());
	                        return null;
	                    }
	                });
					
				}
				
		    	// update externalIds
				if(janrainMemberinfo.getExternalIds()!=null && janrainMemberinfo.getExternalIds().size()>0)
				{
					@SuppressWarnings("unchecked")
					final Future futureProfile = service.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateExternalIds(janrainMemberinfo.getExternalIds(), memberinfo.getMemberIdentifiers(), memberinfo.getMemberId());
	                        return null;
	                    }
	                });
					
				}
				logger.info("=========> calling update EXT ID ");
				
				logger.info("=========> calling update CLIENT ID ");
				// update client
				if(janrainMemberinfo.getClients()!=null && janrainMemberinfo.getClients().size()>0)
				{
					@SuppressWarnings("unchecked")
					final Future futureClient= service.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateClients(janrainMemberinfo.getClients(), memberinfo.getClients(), memberinfo.getMemberId());
	                        return null;
	                    }
	                });
					
				}
				logger.info("=========> calling update Legal Acceptance");
				// update externalIds
				if(janrainMemberinfo.getLegalAcceptances()!=null && janrainMemberinfo.getLegalAcceptances().size()>0)
				{
					@SuppressWarnings("unchecked")
					final Future futureLegalacceptance= service.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateLegalAcceptance(janrainMemberinfo.getLegalAcceptances(), memberinfo.getLegalAcceptances(), memberinfo.getMemberId());
	                        return null;
	                    }
	                });
					
				}
				//update phone numbers 
				if(janrainMemberinfo.getPhoneNumbers()!=null && janrainMemberinfo.getPhoneNumbers().size()>0)
				{
					@SuppressWarnings("unchecked")
					final Future futurePhoneNumbers= service.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updatePhoneNumbers(janrainMemberinfo.getPhoneNumbers(),memberinfo.getPhoneNumbers(), memberinfo.getMemberId());
	                        return null;
	                    }
	                });
					
				}
				//update profile interest
				/*if(janrainMemberinfo.getProfiles()!= null && janrainMemberinfo.getProfiles().size()>0)
				{
					logger.info("Calling updateProfileInterest>>>>>>>>>>>>");
					@SuppressWarnings("unchecked")
					final Future futureProfileInterest= service.submit(new Callable() {
	                    public Object call() throws Exception {
	                    	updateProfileInterest(janrainMemberinfo.getProfiles(), memberinfo);
	                        return null;
	                    }
	                });
					
				}*/
				/*
				if(janrainMemberinfo.getProfiles()!= null && janrainMemberinfo.getProfiles().size()>0)
				{
					
	                   updateProfileInterest(janrainMemberinfo.getProfiles(), memberinfo);
	                     
					
				}*/
			
				//latch.await();
				logger.info("Update complited for member id "+memberinfo.getMemberId()+" UUID "+janrainMemberinfo.getUuid() +" ");
			}
			catch(Exception exx)
			{
				logger.error("Exception in update user data : "+memberinfo.getMemberId()+" UUID "+janrainMemberinfo.getUuid(),exx);
			}
			finally
			{
				service.shutdown();
			}
			
			
		}
	}
    private void updateProfileInterest(List<Profiles> janrainProfiles,MemberInfo memberinfo){
    	//update Member Profile 
		//read prop file to get the mapper for all profile object
		getConfigurtionPropertyUpdatable();
		HashMap<String,List<Object>> customJanrainProfileMap = new HashMap<String,List<Object>> ();
		for(Profiles profiles:janrainProfiles)
		{
			CdsoProfileInterest cdsoProfileInterest = new CdsoProfileInterest();
			customJanrainProfileMap = populateListForProfile(profiles,customJanrainProfileMap);
			cdsoProfileInterest.setProfileId(profiles.getProfile().getId());
			cdsoProfileInterest.setProfileId(memberinfo.getMemberId());
			if(customJanrainProfileMap!=null && customJanrainProfileMap.size()>0)
			{
				
				List<CdsoProfileInterest> cdsoProfileInterestList= generateDataForJanrainProfile(profiles.getId(),customJanrainProfileMap,memberinfo.getMemberId());
				if(cdsoProfileInterestList!=null && cdsoProfileInterestList.size()>0)
				{
					JanrainServiceHepler.updateProfileInterest(cdsoProfileInterestList);
				}
			}
		}
    }
 
	private List<CdsoProfileInterest> generateDataForJanrainProfile(
			
			BigInteger profileId, HashMap<String, List<Object>> customJanrainProfile , BigInteger memId)  {
		
		logger.info("-------------------------------------------------------------------------------------------");
		logger.info("-------------------------------------------------------------------------------------------");
		CdsoProfileInterestDtl cdsoProfileInterestDtl=null;
		List<CdsoProfileInterest> allProfileInterest  = new ArrayList<CdsoProfileInterest>();
		//List<CdsoProfileInterestDtl> CdsoProfileInterestDtlList=null;
		try{
			for ( String key : customJanrainProfile.keySet() ) {
				//(key)customJanrainProfile.get(key);
				
			/*	if(key.equals("com.ko.cds.pojo.janrainIntegration.Pphotos") )
				{
					System.out.println("");
				}*/
				
				List<String[]> allMethidName=  generateMethodList(key);
				
				for(String[] getterMertidSet : allMethidName)
				{
					/*CdsoProfileInterest cdsoProfileInterest = new CdsoProfileInterest();
					cdsoProfileInterest.setMemberId(memId);
					cdsoProfileInterest.setProfileId(profileId);
					cdsoProfileInterest.setInterestName(getterMertidSet[0]);
					*/
					/*if(getterMertidSet[0].equals("displayName"))
					{
						System.out.println("");
					}*/
					List<Object> profileObjectList = customJanrainProfile.get(key);
					
					if(profileObjectList!=null && profileObjectList.size()>0)
					{ 
						
						
						for(Object profileObject : profileObjectList)
						{   
							CdsoProfileInterest cdsoProfileInterest = new CdsoProfileInterest();
							cdsoProfileInterest.setMemberId(memId);
							cdsoProfileInterest.setProfileId(profileId);
							cdsoProfileInterest.setInterestName(getterMertidSet[0]);
							List<CdsoProfileInterestDtl> cdsoProfileInterestDtlList =  new ArrayList<CdsoProfileInterestDtl>();
							Class cls = Class.forName(profileObject.getClass().getName());
							Method[] allMethods = cls.getDeclaredMethods();
							//invoke getId method to set janrainInterestId if available
							try{
							Method getIdMethod=cls.getDeclaredMethod("getId", null);
							 Object id = getIdMethod.invoke(profileObject, null);
							 
							if(id != null){
								if(String.class.isInstance(id)){
									cdsoProfileInterest.setJanrainInterestId(new BigInteger((String)id));
								}else{
								cdsoProfileInterest.setJanrainInterestId(new BigInteger(id.toString()));
								}
							}
							
							}catch(NoSuchMethodException e){
								logger.info(" getId method does not exist for "+cls.getClass().getName());
							}catch (java.lang.ClassCastException e) {
								logger.info(" getId method can not be cast "+cls.getClass().getName());
							}
							for (Method m : allMethods) {
								
								if (m.getName().equals("get"+getterMertidSet[1])) {
									
									cdsoProfileInterestDtl = new CdsoProfileInterestDtl();
									String value = (String) m.invoke(profileObject, null);
									cdsoProfileInterestDtl.setInterestValue(value);
									logger.info("Intetest Name "+cdsoProfileInterest.getInterestName() +" With Value "+cdsoProfileInterestDtl.getInterestValue());
									break;
								} /*else if (getterMertidSet[2] !=null && m.getName().startsWith("get"+getterMertidSet[2])) {
			
									String value = (String) m.invoke(profileObject, null);
									//cdsoProfileInterest.setProfileId(new BigInteger(value));
									cdsoProfileInterest.setProfileId(new BigInteger("5771713"));
									//break;
								}*/
								
							}
							if(cdsoProfileInterestDtl!=null && cdsoProfileInterestDtl.getInterestValue()!=null)
							{
								//logger.info("Intetest Name "+cdsoProfileInterest.getInterestName() +" With Value "+cdsoProfileInterestDtl.getInterestValue());
								cdsoProfileInterestDtlList.add(cdsoProfileInterestDtl);
							}
							else
							{
								//logger.info("Intetest Name "+cdsoProfileInterest.getInterestName() +" With Value "+cdsoProfileInterestDtl.getInterestValue());
							}
							if(cdsoProfileInterestDtlList.size()>1)
							{
								logger.info("DTL Size is more than 0 : "+cdsoProfileInterestDtlList.size() + "For interest : "+cdsoProfileInterest.getInterestName());
							}
							cdsoProfileInterest.setProfileInterestDtl(cdsoProfileInterestDtlList);
							allProfileInterest.add(cdsoProfileInterest);
						}
						//logger.info("Dtl list Size : "+CdsoProfileInterestDtlList.size());
					}
					/*
					if(CdsoProfileInterestDtlList.size()>1)
					{
						logger.info("DTL Size is more than 0 : "+CdsoProfileInterestDtlList.size() + "For interest : "+cdsoProfileInterest.getInterestName());
					}
					cdsoProfileInterest.setProfileInterestDtl(CdsoProfileInterestDtlList);
					allProfileInterest.add(cdsoProfileInterest);*/
				}
				
			}
		}
		catch(Exception exx)
		{
			exx.printStackTrace();
		}
		
		
		return allProfileInterest;
	}
	
	
	private List<String[]> generateMethodList(String key) {
		
		List<String[]> allMetdos = new ArrayList<String[]>();
		if(returnProfileObjectMaper.containsKey(key))
		{
			String[] methodList  =  returnProfileObjectMaper.get(key).split("!");
			for(int i=0;i<methodList.length;i++)
			{
				String allMethod = methodList[i];
				String[] seperateMethod=allMethod.split(":");
				allMetdos.add(seperateMethod);
			}
		}
		else
		{
			logger.info("Key Not found in Hash Map :"+key);
			//throw new NullPointerException("key Not found in Hash Map");
			
		}
		return allMetdos;
		
	}

	public static Map<String, String> getConfigurtionPropertyUpdatable() {
		
		Properties prop = new Properties();
		try {

			
			
			InputStream in = (JanrainService.class.getClassLoader()
						.getResourceAsStream(MAPPER_FILE_LOCATION));
			//InputStream in = new FileInputStream("ProfileObjectMutatorMapper");

			prop.load(in);
			in.close();
			Enumeration<Object> e = prop.keys();

			while (e.hasMoreElements()) {
				String param = (String) e.nextElement();
				returnProfileObjectMaper.put(param, prop.getProperty(param));
			}

		} catch (IOException ioe) {

			ioe.printStackTrace();
			logger.error("Error in File read", ioe);
		}

		return returnProfileObjectMaper;
	}

	public void updatePhoneNumbers(List<PhoneNumberJanrain> phoneNumbers,
			List<PhoneNumber> phoneNumbersCDS, BigInteger memberId)  {
		logger.info("Updating Phone Number ");
		List<PhoneNumber> phoneNumberList = new ArrayList<PhoneNumber>();
		for(PhoneNumberJanrain phoneNumberJanrain : phoneNumbers)
		{
			PhoneNumber phoneNumber = new PhoneNumber();
			phoneNumber.setCountryCode(phoneNumberJanrain.getCountryCode());
			phoneNumber.setMemberId(memberId);
			phoneNumber.setPhoneNumber(phoneNumberJanrain.getValue());
			phoneNumber.setPhoneType(phoneNumberJanrain.getType());
			phoneNumber.setPrimaryIndicator("Y");;
			try
			{
				if(phoneNumberJanrain.getDateVerified()!=null)
				{
					SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
					phoneNumber.setStatusCode("Active");
					java.util.Date date = sdf.parse(phoneNumberJanrain.getDateVerified());
			    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
			    	phoneNumber.setVerified(timestamp);
				}
			}
			catch(Exception exx )
			{
				logger.error("Phone Number Verified Date Exception ",exx);
			}
			phoneNumberList.add(phoneNumber);
		}
		logger.info("Calling update for Phone Number for Member ID : "+memberId);
		JanrainServiceHepler.updatePhoneNumber(phoneNumberList);
		//latch.countDown();
	}

	public void updateLegalAcceptance(List<LegalAcceptance> janrainLegalAcceptanceList, List<LegalAcceptances> cdsLeagalAcceptanceList,BigInteger memberId) throws ParseException{
   		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
   		for (LegalAcceptance janrainLegalAcceptances : janrainLegalAcceptanceList) {
   			LegalAcceptances legalAcceptance = new LegalAcceptances();
   			legalAcceptance.setClientId(janrainLegalAcceptances.getClientId());
   			legalAcceptance.setId(janrainLegalAcceptances.getId());
   			legalAcceptance.setType(janrainLegalAcceptances.getType());
   			legalAcceptance.setLegalAcceptanceId(janrainLegalAcceptances.getLegalAcceptanceId());
   			legalAcceptance.setAccepted(janrainLegalAcceptances.getAccepted());
   			if(janrainLegalAcceptances.getDateAccepted()!=null)
   			{
	   			java.util.Date date = sdf.parse(janrainLegalAcceptances.getDateAccepted());
		    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
	   			legalAcceptance.setDateAccepted(timestamp);
   			}
   			legalAcceptance.setMemberId(memberId);
   			boolean flag = true;
   			for (LegalAcceptances cdsLegalAcceptances : cdsLeagalAcceptanceList) {
   				if(janrainLegalAcceptances.getLegalAcceptanceId().equals(cdsLegalAcceptances.getLegalAcceptanceId())){
   					flag = false;
   					break;
   				}
   			}
   			JanrainServiceHepler.insertUpdateLegalAcceptance(legalAcceptance ,flag);
		}
   	//latch.countDown();
   		
   	}
   	
	/**
	 * This method is to update member profile 
	 * @param janrainProfileList
	 * @param cdsProfileList
	 * @param memberId
	 */
   	public void updateProfiles(List<Profiles> janrainProfileList, List<MemberDomainProfile> cdsProfileList, BigInteger memberId){
   		
   		boolean isInsert=true;
   		List<MemberDomainProfile> profileListInsert =  new ArrayList<MemberDomainProfile>();
   		List<MemberDomainProfile> profileListUpdate =  new ArrayList<MemberDomainProfile>();
   		for (Profiles janrainProfile : janrainProfileList) {
   			MemberDomainProfile profile = new MemberDomainProfile();
   			if(janrainProfile.getDomain()!=null)
   			{
   				String[] domainNameArr = janrainProfile.getDomain().split("\\.");
   				if(domainNameArr.length==2)
   				{
   					profile.setDomainName(domainNameArr[0].toUpperCase());
   				}
   				else 
				{
   					profile.setDomainName(janrainProfile.getDomain().toUpperCase());	
				}
   				
   			}
   			profile.setIdentifier(janrainProfile.getIdentifier());
   			profile.setMemberId(memberId);
   			profile.setProfileId(janrainProfile.getId());
   			profile.setDisplayName(janrainProfile.getProfile().getPreferredUsername());
   			boolean flag = true;
   			for (MemberDomainProfile memberDomainProfile : cdsProfileList) {
   				if((janrainProfile.getId().equals(memberDomainProfile.getProfileId())) && memberId.equals(memberDomainProfile.getMemberId()) ){
   					flag = false;
   					break;
   				}
   			}
   			if(flag)
   			{
   				profileListInsert.add(profile);
   			}
   			else
   			{
   				profileListUpdate.add(profile);
   			}
   		
		}
   		isInsert = JanrainServiceHepler.insertUpdateProfile(profileListInsert , profileListUpdate);
   		logger.info("DB Update Status in updateProfiles : "+isInsert);
   		//latch.countDown();
   		
   		
   	}
   	
    public void updateClients(List<Clients> janrainClientList, List<Client> cdsClientList, BigInteger memberId) throws ParseException{
    	logger.debug("Upading Client >>>>>>>>>>>>>>>>>>>>");
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
    	List<Client> janrainInsertClientList = new ArrayList<>();
    	List<Client> janrainUpdateClientList = new ArrayList<>();
    	for (Clients janrainclient : janrainClientList) {
    		
    		Client client = new Client();
    		client.setChannel(janrainclient.getChannel());
    		client.setClientId(janrainclient.getClientId());
    		logger.debug(" Client first login :"+janrainclient.getFirstLogin());
    		if(janrainclient.getFirstLogin() != null){
	    		java.util.Date date = sdf.parse(janrainclient.getFirstLogin());
		    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
	    		client.setFirstLoginDtm(timestamp);
    		}
    		logger.debug(" Client Last login :"+janrainclient.getLastLogin());
    		if(janrainclient.getLastLogin() != null){
    			java.util.Date date = sdf.parse(janrainclient.getLastLogin());
    			java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
    			client.setLastLoginDtm(timestamp);
    		}
	    	
	    	client.setIdent(janrainclient.getId());
	    	client.setMemberId(memberId);
    		boolean flag = true;
    		for (Client cdsClient : cdsClientList) {
    			if(janrainclient.getClientId().equals(cdsClient.getClientId())){
    				flag = false;
    				break;
    			}
    		}
    		if(flag){
				
				janrainInsertClientList.add(client);
				//memberDAO.insertClient(cdsClient);
			}else{
				janrainUpdateClientList.add(client);
				//memberDAO.updateClient(cdsClient);
			}
    			
		}
    	JanrainServiceHepler.insertUpdateClient(janrainInsertClientList ,janrainUpdateClientList);
    	//latch.countDown();
    }
    
    /**
     * This method is to update Member Address
     * @param memberId
     * @param memberAddres
     * @param mailingAddress
     * @param shippingAddress
     * @throws Exception
     */
    public void updateMemberAddress(BigInteger memberId, List<Address> memberAddres,MailingAddress mailingAddress, ShippingAddress shippingAddress) throws Exception
    {
    	 
    	boolean isUpdate;
    	List<Address> memberListInsert = new ArrayList<Address>();
    	List<Address> memberListUpdate = new ArrayList<Address>();
    	boolean isMellingUpdate=true;
    	boolean isShippingUpdate=true;
    	if(memberAddres!=null && memberAddres.size()>0)
    	{
    		for(Address address: memberAddres)
    		{
    			if(address.getAddressType().equals(ADDRESS_TYPE_MELLING))
    			{
    				isMellingUpdate = false;
    				address.setCity(mailingAddress.getMunicipality());
    				address.setCountry(mailingAddress.getCountry());
    				address.setPostalCode(mailingAddress.getPostalCode());
    				address.setState(mailingAddress.getAdministrativeArea());
    				address.setStreetAddress2(mailingAddress.getStreetName2());
    				address.setStreetAddress1(mailingAddress.getStreetName1());
    				memberListUpdate.add(address);
	    			//JanrainServiceHepler.updateMemberAddressDAO(address);
    			}
    			
    			if(address.getAddressType().equals(ADDRESS_TYPE_SHIPPING))
    			{
    				isShippingUpdate = false;
    				address.setCity(shippingAddress.getMunicipality());
    				address.setCountry(shippingAddress.getCountry());
    				address.setPostalCode(shippingAddress.getPostalCode());
    				address.setState(shippingAddress.getAdministrativeArea());
    				address.setStreetAddress2(shippingAddress.getStreetName2());
    				address.setStreetAddress1(shippingAddress.getStreetName1());
    				memberListUpdate.add(address);
	    			//JanrainServiceHepler.updateMemberAddressDAO(address);
    			}
    		}
    	}
    	
    	
    	if(isMellingUpdate)
    	{
    		Address memAddressM = new Address();
			memAddressM.setMemberId(memberId); 
			memAddressM.setCity(mailingAddress.getMunicipality());
			memAddressM.setCountry(mailingAddress.getCountry());
			memAddressM.setPostalCode(mailingAddress.getPostalCode());
			memAddressM.setState(mailingAddress.getAdministrativeArea());
			memAddressM.setStreetAddress2(mailingAddress.getStreetName2());
			memAddressM.setStreetAddress1(mailingAddress.getStreetName1());
			
			memAddressM.setAddressType(ADDRESS_TYPE_MELLING);
			memberListInsert.add(memAddressM);
			//JanrainServiceHepler.insertMemberAddressDAO(memAddressM);
    	}
    	if(isShippingUpdate)
    	{
    		Address memAddress = new Address();
			memAddress.setMemberId(memberId);
			memAddress.setCity(shippingAddress.getMunicipality());
			memAddress.setCountry(shippingAddress.getCountry());
			memAddress.setPostalCode(shippingAddress.getPostalCode());
			memAddress.setState(shippingAddress.getAdministrativeArea());
			memAddress.setStreetAddress2(shippingAddress.getStreetName2());
			memAddress.setStreetAddress1(shippingAddress.getStreetName1());
			memAddress.setAddressType(ADDRESS_TYPE_SHIPPING);
			memberListInsert.add(memAddress);
			//JanrainServiceHepler.insertMemberAddressDAO(memAddress);
    	}
    	
    	isUpdate = JanrainServiceHepler.inserUpdateEmailAddress(memberListInsert,memberListUpdate); 
    	logger.info("DB Update Status in Address For member ID  : "+ memberId+"   "+isUpdate);
    	//latch.countDown();
    	
    }
    
    /**
     * This method is to update Member Info 
     * @param memberInfo
     * @param result
     * @throws Exception
     */
    public void updateMemberInfo(MemberInfo memberInfo , Results result) throws Exception
    {
    	boolean isUpdate=true;
    	if(result.getCreated()!=null)
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
	    	
	    	java.util.Date date = sdf.parse(result.getCreated());
	    	java.sql.Date dateSql = new java.sql.Date(date.getTime());
	    	memberInfo.setCreated(dateSql);
    	}
    	memberInfo.setAccountStatus(result.getAccountStatus());
    	memberInfo.setAuthStatus(result.getAccountStatus());
    	memberInfo.setBirthday(result.getBirthday());
    	memberInfo.setDisplayName(result.getDisplayName());
    	memberInfo.setFirstName(result.getGivenName());
    	memberInfo.setLastName(result.getFamilyName());
    	if("male".equalsIgnoreCase(result.getGender()))
    	{
    		memberInfo.setGenderCode("M");
    	}
    	else if("female".equalsIgnoreCase(result.getGender()))
    	{
    		memberInfo.setGenderCode("F");
    	}
    	memberInfo.setLanguageCode(result.getLanguage());
    	memberInfo.setMiddleName(result.getMiddleName());
    	memberInfo.setEmailAddress(result.getEmail());
    	memberInfo.setAboutMe(result.getAboutMe());
    	memberInfo.setDeleteReason(result.getDeleteReason());
    	memberInfo.setOriginatingClient(result.getOriginatingClient());
    	memberInfo.setCountry(result.getResidency().getCountry());
    	if(result.getDeactivatedDate()!=null)
    	{
    		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
	    	java.util.Date date = sdf.parse(result.getDeactivatedDate());
	    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
	    	memberInfo.setDeactivatedDate(timestamp);
	    	
    	}
    	isUpdate = JanrainServiceHepler.updateMemberInfo(memberInfo);
    	logger.info("DB Update Status in MemberInfo For member ID  : "+ memberInfo.getMemberId()+"    "+isUpdate);
    	//latch.countDown();
    }
    
    /**
     * This Method is to update Email address
     * @param memberId
     * @param janrainResult
     * @param janRainEmailDAO
     * @param emailCdso
     * @param emailVerified
     * @throws ParseException
     */
	private void updateEmails(BigInteger memberId,Results janrainResult, List<Emails> janRainEmailDAO,
			List<Email> emailCdso, String emailVerified) throws ParseException {
		// TODO Auto-generated method stub
		
		//java.util.Date date;
		boolean isTrue=true;
     	boolean inserUpdateFlg = true;
    	Email emailDAO =null;
    	
    	if(emailCdso!=null && emailCdso.size()>0)
    	{
    		emailDAO = emailCdso.get(0);
			emailDAO.setEmail(janrainResult.getEmail());
			if(janrainResult.getEmailVerified()!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
		    	java.util.Date date = sdf.parse(janrainResult.getEmailVerified());
		    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		    	emailDAO.setEmaiVerifiedDt(timestamp);
		    	emailDAO.setStatusCode("VALID");
		    	emailDAO.setValidInd("Y");
			}
			else
			{
				emailDAO.setStatusCode("INVALID");
				emailDAO.setValidInd("N");
			}
			emailDAO.setPrimaryIndicator("Y");    
			inserUpdateFlg = false;
			
    	}
    	else
    	{
    		emailDAO = new Email();
			emailDAO.setMemberId(memberId);
			
			emailDAO.setEmail(janrainResult.getEmail());
			if(janrainResult.getEmailVerified()!=null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
		    	java.util.Date date = sdf.parse(janrainResult.getEmailVerified());
		    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		    	emailDAO.setEmaiVerifiedDt(timestamp);
		    	emailDAO.setStatusCode("VALID");
		    	emailDAO.setValidInd("Y");
			}
			else
			{
				emailDAO.setStatusCode("INVALID");
				emailDAO.setValidInd("N");
			}
			emailDAO.setPrimaryIndicator("Y");    
			inserUpdateFlg = true;
    	}
    	
    	
    	
    	if(emailDAO!=null)
    	{
    		isTrue = JanrainServiceHepler.inserUpdateEmailAddress(emailDAO,inserUpdateFlg);
    		
    	}
    	
    	logger.info("DB Update Status in updateEmails For member ID  : "+ memberId+"   "+isTrue);
    	//latch.countDown();
	}
    
  /**
   * This method is to update com opts 
   * @param janrainCommOptList
   * @param cdsLegaAcceptancesList
   * @param memId
   * @throws Exception
   */
    
    //update comm Opt
    private void updateCommOpts(List<CommunicationOpt> janrainCommOptList ,List<OptPreference>  cdsLegaAcceptancesList,BigInteger memId) throws Exception
    {
    	boolean isTrue=true;
    	List<CommunicationOptIn> communicationOptListInsert =  new ArrayList<CommunicationOptIn>();
    	List<CommunicationOptIn> communicationOptListUpdate =  new ArrayList<CommunicationOptIn>();
    	CommunicationOptIn communicationOptIn = null;
    	boolean isIsertUpdate=true;
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
    	java.util.Date date;
		try 
		{
			
			for(CommunicationOpt janrainCommOpts : janrainCommOptList)
			{
				communicationOptIn = new CommunicationOptIn();
				if(janrainCommOpts.getDateAccepted()!=null)
				{
					date = sdf.parse(janrainCommOpts.getDateAccepted());
			    	java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
			    	communicationOptIn.setAcceptedDate(timestamp);
				}
		    	
		    	communicationOptIn.setMemberId(memId);
		    	communicationOptIn.setClientId(janrainCommOpts.getClientId());
		    	//Date dateInsert= new java.util.Date();
		    	//communicationOptIn.setInsertDateJanrain(new Timestamp(dateInsert.getTime())); 
		    	communicationOptIn.setFormat(janrainCommOpts.getFormat());
		    	communicationOptIn.setSchedulePreference(janrainCommOpts.getSchedulePreference());
		    	communicationOptIn.setType(janrainCommOpts.getType());
		    	if(janrainCommOpts.isAccepted())
		    	{
		    		communicationOptIn.setOptedInIndicator("true");
		    	}
		    	else
		    	{
		    		communicationOptIn.setOptedInIndicator("false");
		    	}
		    	/*
		    	if(cdsLegaAcceptancesList!=null)
		    	{
			    	for(OptPreference optPreference : cdsLegaAcceptancesList)
			    		
			    	{
			    		
			    		if(optPreference.getCommunicationTypeName() !=null && janrainCommOpts.getOptId().equals(optPreference.getCommunicationTypeName()))
						{
			    			
			    			isIsertUpdate=false;
			    			break;
			    			
						}
			    		
			    	}
			    	if(!isIsertUpdate)
			    	{
			    		communicationOptIn.setCommunicationTypeName(janrainCommOpts.getOptId());
			    		communicationOptListUpdate.add(communicationOptIn);
			    	}
			    	else
			    	{
			    		communicationOptIn.setCommunicationTypeName(janrainCommOpts.getOptId());
			    		communicationOptListInsert.add(communicationOptIn);
			    	}
		    	}
		    	else
		    	{
		    		communicationOptIn.setCommunicationTypeName(janrainCommOpts.getOptId());
		    		communicationOptListInsert.add(communicationOptIn);
		    	}*/
		    	
		    	
		    	
		    	
		    	//isIsertUpdate=true;
		    	communicationOptIn.setCommunicationTypeName(janrainCommOpts.getOptId());
	    		communicationOptListInsert.add(communicationOptIn);

			}
			
			
			isTrue = JanrainServiceHepler.updateInsertCommunicationOpts(communicationOptListInsert,communicationOptListUpdate);
	    	
		} catch (ParseException parExx) {
			// TODO Auto-generated catch block
			logger.error("updateCommOpts : Exception in time convertion :",parExx);
		}
		
		logger.info("DB Update Status in Comm Opts For Member ID  : "+memId +"  "+isTrue);
		//latch.countDown();
			
    }
    
    //update memberIdentifire or externalIds 
    /**
     * This method is to update External Ids 
     * @param janrainLegaAcceptancesList
     * @param cdsLegaAcceptancesList
     * @param memId
     */
    private void updateExternalIds(List<ExternalIds> janrainLegaAcceptancesList ,List<MemberIdentifier>  cdsLegaAcceptancesList,BigInteger memId)
    {
    	boolean isTrue=true;
    	List<MemberIdentifier> memberIndInsert = new ArrayList<MemberIdentifier>();
    	List<MemberIdentifier> memberIndUpdate = new ArrayList<MemberIdentifier>();
    	boolean inserUpdate=true;
    	MemberIdentifier memberIdentifire=null;
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
    	java.util.Date date;
    	try 
		{
	    	for(ExternalIds externalId:janrainLegaAcceptancesList)
	    	{
	    		/*java.sql.Timestamp timestamp = null;
	    		if(externalId.getLastUpdatedTime()!=null && externalId.getLastUpdatedTime().length()==0)
	    		{
		    		date = sdf.parse(externalId.getLastUpdatedTime());
					
			    	timestamp = new java.sql.Timestamp(date.getTime());
	    		}*/
	    		/*
	    		if(externalId.getType().equals(CDSConstants.JANRAIN_TYPE_CDS)){
	    			continue;
	    		}*/
		    	memberIdentifire=new MemberIdentifier();
		    	if(cdsLegaAcceptancesList!=null && cdsLegaAcceptancesList.size()>0)
		    	{
		    		for(MemberIdentifier memberIdentifier:cdsLegaAcceptancesList)
		    		{
		    			if(memberIdentifier.getDomainName()!=null  &&  externalId.getType().equals(memberIdentifier.getDomainName()) )
		    			{
		    				memberIdentifire.setDomainName(externalId.getType());
		    				memberIdentifire.setMemberId(memId);
		    				memberIdentifire.setUserId(externalId.getValue());
		    				//how to map status code
		    				memberIdentifire.setStatusCode(memberIdentifier.getStatusCode());
		    				//memberIdentifire.setInsertDt(memberIdentifier.getInsertDt());
		    				/*if(timestamp!=null)
		    				{
		    					memberIdentifire.setUpdateDt(timestamp);
		    				}*/
		    				
		    				
		    				
		    				inserUpdate=true;
		    				break;
		    			}
		    			else
		    			{
		    				
		    				inserUpdate=false;
		    			}
		    		}
		    	}
		    	else
		    	{
		    		inserUpdate=false;
		    	}
	    		
	    		if(inserUpdate)
	    		{
	    			memberIndUpdate.add(memberIdentifire);
	    			//JanrainServiceHepler.updateExternalId(memberIdentifire);
	    		}
	    		else
	    		{
	    			memberIdentifire.setDomainName(externalId.getType());
					memberIdentifire.setMemberId(memId);
					memberIdentifire.setUserId(externalId.getValue());
					memberIdentifire.setStatusCode("Active");
					//memberIdentifire.setInsertDt(timestamp);
					//memberIdentifire.setUpdateDt(timestamp);
					//JanrainServiceHepler.insertExternalId(memberIdentifire);
					memberIndInsert.add(memberIdentifire);
	    		}
	    		inserUpdate=true;
	    		
	    	}
	    	
	    	isTrue = JanrainServiceHepler.insertUpdateMemberIdentifire(memberIndInsert,memberIndUpdate);
		}
    	catch(Exception parExx)
    	{
    		//logger.info("Exception in time convertion :");
    	    logger.error("updateExternalIds : Exception in time convertion :",parExx);
    	}
    	logger.info("DB Update Status updateExternalIds for Member Id :"+ memId+"  "+isTrue);
    	//latch.countDown();
    }
    
   
    /**
     * This method is to update the pooler time stamp in prop file 
     */
    private void updateInsertPoolerTime()
    {
    	 String lastUpdateTime = (CDSOUtils.getConfigurtionPropertyUpdatable()).get(FILTER_DATE_TIME);
    	 Date localTime = new Date(); 
    	 DateFormat converter = new SimpleDateFormat(DATE_FORMAT_WITH_TIMEZONE , Locale.US);
    	   
         //getting GMT timezone,  e.g. UTC
         converter.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
       
         logger.info("local time : " + localTime);;
         logger.info("time in GMT :(Janrain Last time stamp) " + converter.format(localTime));
         try {
         CDSOUtils.updateConfigurtionProperty("lastpoolingDateTime",converter.format(localTime));
         
         //update last to last update time 
         
         
         SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
     	 Date lstToLastUpdateDate = new Date();
     	 
     	lstToLastUpdateDate = sdf.parse(lastUpdateTime);
		 java.sql.Timestamp timestamp = new java.sql.Timestamp(lstToLastUpdateDate.getTime());
    	 CDSOUtils.updateConfigurtionProperty(FILTER_DATE_TIME_OF_LAST_RUN,JanrainServiceHepler.presentTimeStmp);
		} catch (ParseException e) {
			logger.error("updateInsertPoolerTime :"+e);
		}
		
    	 
    	 
    }
    
    
    private HashMap<String, List<Object>> populateListForProfile(
			Profiles janrainPfofileinfo,
			HashMap<String, List<Object>> customJanrainProfile) {
		
		
		/*//adding followrs 
		List<Object> followersList=new ArrayList<Object>();
		for(Profiles profiles : janrainMemberinfo.getProfiles())
		{
			for(Followers followers : profiles.getFollowers())
			{
				followersList.add(followers);
				
			}
			
		}
		if(followersList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Followers", followersList);
		}
		
		//customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Followers", followersList);
		
		// adding following
		List<Object> followingList=new ArrayList<Object>();
		for(Profiles profiles : janrainMemberinfo.getProfiles())
		{
			for(Following following : profiles.getFollowing())
			{
				followingList.add(following);
				
			}
			
		}
		if(followingList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Following", followingList);
		}
		
		// adding friends
		List<Object> friendsList=new ArrayList<Object>();
		for(Profiles profiles : janrainMemberinfo.getProfiles())
		{
			for(Friends friends : profiles.getFriends())
			{
				friendsList.add(friends);
				
			}
			
		}
		if(friendsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Friends", friendsList);
		}
		*/
		
		//adding about me 
		List<Object> aboutMeList=new ArrayList<Object>();
		Profile profile = janrainPfofileinfo.getProfile();
		aboutMeList.add(profile);
		if(aboutMeList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Profile", aboutMeList);
		}
		
		// adding activities
		List<Object> activitiesList=new ArrayList<Object>();
		for(Activities activities :janrainPfofileinfo.getProfile().getActivities() )
		{
			activitiesList.add(activities);
		}
		if(activitiesList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Activities", activitiesList);
		}
	
		// adding Address 
		List<Object> addressList=new ArrayList<Object>();
		
			//Activities activities = profiles.getProfile().getActivities();
		for(Addresses addresses :janrainPfofileinfo.getProfile().getAddresses() )
		{
			addressList.add(addresses);
		}

		
		if(addressList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Addresses", addressList);
		}
		
		
		//adding bodyType 
		List<Object> bodyTypeList=new ArrayList<Object>();
		bodyTypeList.add(janrainPfofileinfo.getProfile().getBodyType());
		if(bodyTypeList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.BodyType", bodyTypeList);
		}
		
		
		//adding books 
		List<Object> bookList=new ArrayList<Object>();
		for(Books books : janrainPfofileinfo.getProfile().getBooks())
		{
			bookList.add(books);
		}
		if(bookList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Books", bookList);
		}
		
		//adding cars 
		List<Object> carList=new ArrayList<Object>();
		for(Cars car : janrainPfofileinfo.getProfile().getCars())
		{
			carList.add(car);
		}
		if(carList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Cars", carList);
		}
		
		// adding children
		List<Object> childrenList=new ArrayList<Object>();
		for(Children children : janrainPfofileinfo.getProfile().getChildren())
		{
			childrenList.add(children);
		}
		if(childrenList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Children", childrenList);
		}
		
		// adding currentLocation
		List<Object> currentLocationList=new ArrayList<Object>();
		currentLocationList.add(janrainPfofileinfo.getProfile().getCurrentLocation());
		
		if(currentLocationList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.CurrentLocation", currentLocationList);
		}
		
		
		//adding phone
		
		
		
		
		// adding email  
		List<Object> emailsList=new ArrayList<Object>();
		for(Emails email : janrainPfofileinfo.getProfile().getEmails())
		{
			emailsList.add(email);
		}
		if(emailsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Emails", emailsList);
		}
		
		
		// adding Food  
		List<Object> foodList=new ArrayList<Object>();
		for(Food food : janrainPfofileinfo.getProfile().getFood())
		{
			foodList.add(food);
		}
		if(foodList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Food", foodList);
		}
		
		
		// adding Hero  
		List<Object> heroesList=new ArrayList<Object>();
		for(Heroes heroes : janrainPfofileinfo.getProfile().getHeroes())
		{
			heroesList.add(heroes);
		}
		if(heroesList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Heroes", heroesList);
		}
		
		//adding InterestedInMeeting
		List<Object> interestedInMeetingList=new ArrayList<Object>();
		for(InterestedInMeeting interestedInMeeting : janrainPfofileinfo.getProfile().getInterestedInMeeting())
		{
			interestedInMeetingList.add(interestedInMeeting);
		}
		if(interestedInMeetingList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.InterestedInMeeting", interestedInMeetingList);
		}
		
		//add Interests
		List<Object> interestsList=new ArrayList<Object>();
		for(Interests interests : janrainPfofileinfo.getProfile().getInterests())
		{
			interestsList.add(interests);
		}
		if(interestsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Interests", interestsList);
		}
		
		
		//add JobInterests
		List<Object> jobInterestsList=new ArrayList<Object>();
		for(JobInterests jobInterests : janrainPfofileinfo.getProfile().getJobInterests())
		{
			jobInterestsList.add(jobInterests);
		}
		
		if(jobInterestsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.JobInterestsList", jobInterestsList);
		}
		
		//add Languages
		List<Object> languagesList=new ArrayList<Object>();
		
		for(Languages languages : janrainPfofileinfo.getProfile().getLanguages())
		{
			languagesList.add(languages);
		}
			
		
		if(languagesList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Languages", languagesList);
		}
		
		//add LanguagesSpoken
		List<Object> languagesSpokenList=new ArrayList<Object>();
		
		for(LanguagesSpoken languagesSpoken : janrainPfofileinfo.getProfile().getLanguagesSpoken())
		{
			languagesSpokenList.add(languagesSpoken);
		}
		
		if(languagesSpokenList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.LanguagesSpoken", languagesSpokenList);
		}
		
		//add LookingFor
		List<Object> lookingforList=new ArrayList<Object>();
		
		for(LookingFor lookingFor : janrainPfofileinfo.getProfile().getLookingFor())
		{
			lookingforList.add(lookingFor);
		}
			
		
		if(lookingforList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.LookingFor", lookingforList);
		}
		
		//add Movies
		List<Object> moviesList=new ArrayList<Object>();
		
		for(Movies movies : janrainPfofileinfo.getProfile().getMovies())
		{
			moviesList.add(movies);
		}
			
		
		if(moviesList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Movies", moviesList);
			
		}
		
		//add <Music>
		
		List<Object> musicList=new ArrayList<Object>();
		
		for(Music music : janrainPfofileinfo.getProfile().getMusic())
		{
			musicList.add(music);
		}
			
		
		if(musicList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Music", musicList);
		}
		
		//add name 
		List<Object> nameList=new ArrayList<Object>();
		nameList.add(janrainPfofileinfo.getProfile().getName());
		if(nameList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Name", nameList);
		}
		
		//add Organizations
		List<Object> organizationsList=new ArrayList<Object>();
		
		for(Organizations organizations : janrainPfofileinfo.getProfile().getOrganizations())
		{
			organizationsList.add(organizations);
		}
		
		if(organizationsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Organizations", organizationsList);
		}
		
		//add PhoneNumbers 
		/*List<Object> phoneNumbersList=new ArrayList<Object>();
		for(Profiles profiles : janrainMemberinfo.getProfiles())
		{
			for(PhoneNumbers phoneNumbers : profiles.getProfile().getPhoneNumbers())
				phoneNumbersList.add(phoneNumbers);
		}
		if(phoneNumbersList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.PhoneNumbers", phoneNumbersList);
		}
		*/
		
		//add Pphotos
		List<Object> pphotosList=new ArrayList<Object>();
		
		for(Pphotos pphotos : janrainPfofileinfo.getProfile().getPhotos())
		{
			pphotosList.add(pphotos);
		}
		
		if(pphotosList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Pphotos", pphotosList);
		}
		
		//add Sports
		List<Object> sportsList=new ArrayList<Object>();
		
		for(Sports sports : janrainPfofileinfo.getProfile().getSports())
		{
			sportsList.add(sports);
		}
		
		if(sportsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Sports", sportsList);
		}
		
		//add Tags
		List<Object> tagList=new ArrayList<Object>();
		
		for(Tags tags : janrainPfofileinfo.getProfile().getTags())
		{
			tagList.add(tags);
		}
		
		if(tagList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Tags", tagList);
		}
		
		//add TurnOffs
		List<Object> turnOffsList=new ArrayList<Object>();
		
		for(TurnOffs turnOffs : janrainPfofileinfo.getProfile().getTurnOffs())
		{
			turnOffsList.add(turnOffs);
		}
		
		
		if(turnOffsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.TurnOffs", turnOffsList);
		}
		
		//add TurnOns
		List<Object> turnOnsList=new ArrayList<Object>();
		
		for(TurnOns turnOns : janrainPfofileinfo.getProfile().getTurnOns())
		{
			turnOnsList.add(turnOns);
		}
			
		
		if(turnOnsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.TurnOns", turnOnsList);
		}
		
		//add TvShows
		List<Object> tvShowsList=new ArrayList<Object>();
		
		for(TvShows tvShows : janrainPfofileinfo.getProfile().getTvShows())
		{
			tvShowsList.add(tvShows);
		}
			
		
		if(tvShowsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.TvShows", tvShowsList);
		}
		
		//add Urls
		List<Object> urlsList=new ArrayList<Object>();
		
		for(Urls urls : janrainPfofileinfo.getProfile().getUrls())
		{
			urlsList.add(urls);
		}
			
		
		if(urlsList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Urls", urlsList);
		}
		
		// add provider
		List<Object> providerList=new ArrayList<Object>();
		
		providerList.add(janrainPfofileinfo);
		customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Profiles", providerList);
		
		
		
		/*List<Object> followersList=new ArrayList<Object>();
		
		for(Followers followers : janrainPfofileinfo.getFollowers())
		{
			followersList.add(followers);
			
		}
			
		
		if(followersList.size()>0)
		{
			customJanrainProfile.put("com.ko.cds.pojo.janrainIntegration.Followers", followersList);
		}*/
		
		//janrainMemberinfo.getProfiles().
		return customJanrainProfile;
	}
    
    
    
}
