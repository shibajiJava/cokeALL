package com.ibm.ko.cds.service.janrainIntegration;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.ko.cds.pojo.janrainIntegration.Result;
import com.ibm.ko.cds.util.CDSOUtils;
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
	private static final String JANRAIN_API_MANAGER = "janranPassword";
	
	private static final Logger logger = LoggerFactory.getLogger("janrainforETL_schedularTask");
	
	private static final Logger logger1 = LoggerFactory.getLogger("schedular_Task");
	public static String presentTimeStmp="";
	
	public String getMemberInfo() throws ClientProtocolException, IOException
	{
		logger.info("----------------------------------------------LOG TEST -------------------------------------------------------------------");
		logger1.info("-----------------------------------------------------LOG TEST-------------------------------------------------------------");
		List<Result> allMemberInfoList = new ArrayList<Result>(); 
		Result allMemberInfo=null;
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String apiName=configProp.get(JANRAIN_API_KEY);
		String janrainUrl = configProp.get(JANRAIN_URL_KEY);
		String environmrnt = configProp.get(ENVORONMENT_KEY);
		String entityType = configProp.get(ENTITY_TYPE_NAME);
		final String fileLOcation= configProp.get(JANRAIN_FILE_LOCATION).replace("#",getUTCTimeForJanrainJsonFile());
		final String resultFileLocation = configProp.get("janrainResultFileLocation").replace("#",getUTCTimeForJanrainJsonFile());
		final String profileFileLocation = configProp.get("janrainProfileFileLocation").replace("#",getUTCTimeForJanrainJsonFile());
		
		int firstResult=0;
		String maxResult="1000";
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
			StringBuffer  responseString = new StringBuffer().append("");
			
		    try {
		        // Add your data
		    	//ExecutorService service = Executors.newFixedThreadPool(1);
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
			        
			        nameValuePairs.add(new BasicNameValuePair("attributes", getJanrainAttr(configProp.get(JANRAIN_ATTRIBUTE+"_"+environmrnt))));
			        
		        	httpclient = new DefaultHttpClient();
		        	httppost = new HttpPost(janrainEndPointUrl.toString());
		        	String encoded = DatatypeConverter.printBase64Binary((configProp.get(JANRAIN_API_MANAGER+"_"+environmrnt)).getBytes("UTF-8"));
				        
				    httppost.addHeader("AUTHORIZATION","Basic "+encoded);
			        nameValuePairs.add(new BasicNameValuePair("first_result",String.valueOf(firstResult)));
			        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			        // Execute HTTP Post Request
			        presentTimeStmp = getUTCTimeForJanrain();
			        //setting the timour value 
			        HttpParams params = httpclient.getParams();
			        HttpConnectionParams.setConnectionTimeout(params, 30000);
			        HttpConnectionParams.setSoTimeout(params, 300000);
			        HttpResponse response = httpclient.execute(httppost);
			        //logger.info(" response =>"+ response);
			        if(response.getStatusLine().getStatusCode()==200)
			        {
			        	responseString.delete(0, responseString.length());
			        	responseString.setLength(0);
			        	responseString.append(new BasicResponseHandler().handleResponse(response));
			        	//final String responseString = new BasicResponseHandler().handleResponse(response);
			        	logger.info("===================================OUTPUT Janrain=========================");
			        	//Write JSON in file 
			        	writeJanrainJSONtoFile("\n"+responseString.toString(),fileLOcation);
                    	parseJanrainToCSV(responseString.toString(),resultFileLocation,profileFileLocation);
                    	/*
			        	@SuppressWarnings("unchecked")
						final Future futureUpdateAddress = service.submit(new Callable() {
		                    public Object call() throws Exception {
		                    	writeJanrainJSONtoFile("\n"+responseString.toString(),fileLOcation);
		                    	parseJanrainToCSV(responseString.toString(),resultFileLocation,profileFileLocation);
		                        return null;
		                    }
		                });*/
			        	
						ObjectMapper objectMapper = new ObjectMapper();
						//System.out.println("Value is : "+responseString);
						
						allMemberInfo = objectMapper.readValue(responseString.toString(), Result.class);
						logger.info("1. data getting form Janrain : "+allMemberInfo.getResult_count());
						
						allMemberInfoList.add(allMemberInfo);
						//continutBreak=false;
						
						if(null == allMemberInfo.getResult_count()){
							logger.error("Unable to get data from Janrain server. Status Code : "+responseString);
							continutBreak=false;
						}
						if((allMemberInfo.getResult_count().intValue())<Integer.valueOf(maxResult))
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
			        	logger.error("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
			        }
		        }while(continutBreak);
		       //service.shutdown();
		        //service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		        
		    } catch (ClientProtocolException clientExx) {
		    	//logger.info(responseString);
		    	logger.error("getMemberInfo : Client Protocol Exception ",clientExx);
		       
		    } catch (IOException ioExx) {
		    	//logger.info(responseString);
		    	logger.error("getMemberInfo :File Not found Expection ",ioExx);
		        
		    } 
		    catch (Exception exx) {
		    	//logger.info(responseString);
		    	logger.error("getMemberInfo :Exception Occured ",exx);
		    	exx.printStackTrace();
		    }
		    
		}else{
			logger.info("Environmet key is not set , hence Not able to call Janrain,  List size is 0");
		}
		
		logger.info("Total List size Obtained from Janrian is "+ allMemberInfoList.size());
		String files = fileLOcation+","+ resultFileLocation +","+profileFileLocation;
		return files;
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


	private void parseJanrainToCSV(String responseString,String resultFileLocation,String profileFileLocation){
			try {
				CDSOUtils.jsonToCSV(responseString,resultFileLocation,profileFileLocation);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	private String getUTCTimeForJanrainJsonFile() {
		Date localTime = new Date(); 
    	DateFormat converter = new SimpleDateFormat("yyyyMMdd-HHmmss");
        converter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return converter.format(localTime);
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
