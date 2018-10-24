package com.ibm.app.services.netBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ibm.app.services.netBase.Util.NetBasetUtil;

@Component
public class NetBaseFile {

	final static Logger logger =  LoggerFactory.getLogger(NetBaseFile.class);
	
	 static Map<String, String> returnMap= new HashMap<String, String>();
	 String timeApiFile = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	 ArrayList<String> fileNameWithPath= new ArrayList<>() ;
	 File ErrorlogFile=null;
	 int noOfCall=0;
	 StringBuilder fileWriteStr = new StringBuilder();
	 StringBuilder fileWriteStrAuthors = new StringBuilder();
	 StringBuilder fileWriteStrGeoLoc = new StringBuilder();
	 StringBuilder fileWriteStrPhrases = new StringBuilder();
	  StringBuilder fileWriteStrConcepts= new StringBuilder();
	    StringBuilder fileWriteStrDoc= new StringBuilder();
	    StringBuilder fileWriteStrSent= new StringBuilder();
	    StringBuilder fileWriteStrOthers= new StringBuilder();
	    StringBuilder fileWriteStrJson= new StringBuilder();
	    boolean isOthersHeaderAdded =false,isMetricsHeaderAdded =false,isAuthorsHeaderAdded =false,isGeoLocHeaderAdded =false,
	    		isPhrasesHeaderAdded =false,isSentHeaderAdded =false,isDocHeaderAdded =false,isConceptHeaderAdded =false;
	    		
	public void  processAPI() throws UnsupportedEncodingException
	{
		fileNameWithPath= new ArrayList<>() ;
		fileWriteStrConcepts = new StringBuilder();
		fileWriteStrDoc = new StringBuilder();
		fileWriteStrSent = new StringBuilder();
		fileWriteStrAuthors = new StringBuilder();
		fileWriteStrGeoLoc = new StringBuilder();
		fileWriteStrPhrases = new StringBuilder();
		fileWriteStrOthers = new StringBuilder();
		fileWriteStrJson = new StringBuilder();
		isOthersHeaderAdded = false;
		isMetricsHeaderAdded = false;
		isAuthorsHeaderAdded = false;
		isGeoLocHeaderAdded = false;
		isPhrasesHeaderAdded = false;
		isSentHeaderAdded = false;
		isDocHeaderAdded = false;
		isConceptHeaderAdded = false;
		
		
		try{
		String timeApiFileWrite = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		returnMap=getConfigurtionProperty();
		 String filePath =  returnMap.get("outpurFileLocation");
		ErrorlogFile=new File(filePath+"NetBaseApiError_"+timeApiFile+".txt");
		ErrorlogFile.createNewFile();
		logger.info("File Created ");
		logger.info("MAP Value : === "+returnMap.get("outpurFileLocation"));
		
		String userNamePWD=returnMap.get("userNamePwd");
		//
		String encoded = DatatypeConverter
				.printBase64Binary((userNamePWD)
						.getBytes("UTF-8"));
		
		
		
		statup();
		String apiNameAll = returnMap.get("apiName");
		
		for(int i=0;i<apiNameAll.split(",").length;i++)
		{
			System.out.println("---------------------------------------Started------------------------------------------------------");
			//flushing the input stream every time for the new api call;
			fileWriteStr = new StringBuilder();
			fileWriteStrAuthors = new StringBuilder();
			  fileWriteStrGeoLoc = new StringBuilder();
			  fileWriteStrPhrases = new StringBuilder();
			  fileWriteStrOthers = new StringBuilder();
			  fileWriteStrJson= new StringBuilder();
			String apiName = (apiNameAll.split(",")[i]).split(";")[0];
			String APIUrl = (apiNameAll.split(",")[i]).split(";")[1];
			String paramList=returnMap.get(apiName);
			
			HttpPost httppost = new HttpPost(APIUrl);
			// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			httppost.addHeader("AUTHORIZATION", "Basic " + encoded);
			
			logger.info("apiNameAll Size "+apiNameAll.length());
			logger.info("apiName : "+apiName+" APIUrl :"+APIUrl);
			
			if(paramList!=null && paramList.length()>0)
			{
				String[] paramComb = paramList.split("#");
				for(int j=0;j<paramComb.length;j++)
				{
					String[] paramListArr = paramComb[j].split(";");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					HashMap<String, String> fixedParam= new HashMap<String, String>();
					List<TopicId> topicIds=null;
					for(int k=0;k<paramListArr.length;k++)
					{
						
						ArrayList<HashMap<String, String>> paramSet = new ArrayList<HashMap<String, String>>();
						if(paramListArr[k].split(":")[0].equals("topicIds") && paramListArr[k].split(":")[1].equals("?"))
						{
     						HttpClient httpclientTopicId = new DefaultHttpClient();
							String encodedTopicId = DatatypeConverter.printBase64Binary((userNamePWD).getBytes("UTF-8"));
							HttpPost httppostTopicId = new HttpPost("https://api.netbase.com/cb/insight-api/2/topics");
							httppostTopicId.addHeader("AUTHORIZATION", "Basic " + encodedTopicId);
							
							logger.info("Calling netBasse API services");
							topicIds =  callNetBaseAPITheameTopicId(httpclientTopicId,null,httppostTopicId,null);
							busySleep(120000);
							
						}
						else
						{
							
							fixedParam.put(paramListArr[k].split(":")[0], paramListArr[k].split(":")[1]);
							//nameValuePairs.add(new BasicNameValuePair(paramListArr[k].split(":")[0], paramListArr[k].split(":")[1]));
							//callNetBaseAPI(httpclient, apiName, httppost,nameValuePairs);
						}
						
						
						
					}
					ArrayList<List<NameValuePair>> nameValuePairList = createParamSet(topicIds,fixedParam);
					for(int indx=0;indx<nameValuePairList.size();indx++)
					{
						List<NameValuePair> paramValueList = nameValuePairList.get(indx);
						HttpClient httpclient = new DefaultHttpClient();
						//System.out.println(paramValueList);
						
						callNetBaseAPI(httpclient, apiName, httppost,paramValueList,timeApiFileWrite);
						logger.info("Going to sleep  2");
						busySleep(120000);
						//continue;
					}
					
					
				}
				
			}
			else
			{
				logger.info("Going to sleep 3");
				
				HttpClient httpclient = new DefaultHttpClient();
				
				callNetBaseAPI(httpclient, apiName, httppost,null,timeApiFileWrite);
				busySleep(40000);
			}
			
			for(int j=0;j<fileNameWithPath.size();j++)
			{
				
				logger.info(fileNameWithPath.get(j));
				writeFtp(fileNameWithPath.get(j));
			}
			ArrayList<String> fileNameWithPath= new ArrayList<>() ;
			
		}
		
			
		}
		catch(Exception exx)
		{
			//System.out.println(" Error occurd in code execution. error is :  "+exx.getMessage());
			logger.error("Exception Found :"+exx.getMessage(),exx);
			exx.printStackTrace();
		}
	}


	
	public static void busySleep(long wait)
	{
	  long waitNano=(wait*1000000);
	  long elapsed;
	  final long startTime = System.nanoTime() ;
	  do {
	    elapsed = System.nanoTime() - startTime;
	  } while (elapsed < waitNano);
	}


	private ArrayList<List<NameValuePair>> createParamSet(List<TopicId> topicIds,	HashMap<String, String> fixedParam) {
		
		ArrayList<List<NameValuePair>> returnList =  new ArrayList<List<NameValuePair>>();
		if(topicIds!=null)
		{
			
			for(TopicId topicid : topicIds)
			{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for ( Entry<String, String> entry : fixedParam.entrySet()) {
				    String key = entry.getKey();
				    String value = entry.getValue();
				    nameValuePairs.add(new BasicNameValuePair(key, value));
				    
				}
				nameValuePairs.add(new BasicNameValuePair("topicIds", topicid.getTopicId()));
				returnList.add(nameValuePairs);
			}
		}
		else
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for ( Entry<String, String> entry : fixedParam.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
			    nameValuePairs.add(new BasicNameValuePair(key, value));
			   
			}
			 returnList.add(nameValuePairs);
		}
		
		return returnList;
	}





	private void callNetBaseAPI(HttpClient httpclient, String apiName,
			HttpPost httppost, List<NameValuePair> nameValuePairs, String timeApiFileWrite)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException, HttpResponseException {
		String paramSet = "";
		try{
			noOfCall++;
			if(noOfCall==25)
			{
				logger.info("Going to sleep  1");
				//Thread.sleep(1000);
			}
			
			if(nameValuePairs!=null)
			{
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				paramSet=getParamString(apiName,nameValuePairs);
			}
			System.out.println("Name value pairs with the Query param==>"+nameValuePairs);
			//httppost.addHeader("AUTHORIZATION", "Basic " + encoded);
			HttpResponse response = httpclient.execute(httppost);
			//System.out.println(httpclient.toString());
			
			//response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String filePath =  returnMap.get("outpurFileLocation");
				logger.info("File path is : "+filePath);
				String responseString = new BasicResponseHandler().handleResponse(response);
				logger.info("responseString====================> : ");
				File logFileSentences=new File(filePath+apiName+"_Sentences_"+timeApiFileWrite+".txt");
				File logFileDocuments=new File(filePath+apiName+"_Documents_"+timeApiFileWrite+".txt");
				File logFileConcepts=new File(filePath+apiName+"_Concepts_"+timeApiFileWrite+".txt");
				File logFileJsons=new File(filePath+apiName+"_JSON_"+timeApiFileWrite+".txt");
				File logFileAuthors=new File(filePath+apiName+"_Authors_"+timeApiFileWrite+".txt");
				File logFilePhrases=new File(filePath+apiName+"_Phrases_"+timeApiFileWrite+".txt");
				File logFileGeoLoc=new File(filePath+apiName+"_Geolocation_"+timeApiFileWrite+".txt");
				File logFileOthers=new File(filePath+apiName+"_Others_"+timeApiFileWrite+".txt");
				File logFile=new File(filePath+apiName+timeApiFileWrite+".txt");
				
			    if("retrieveSentences".equals(apiName))
			    {
			    	paramSet=getParamString(apiName,nameValuePairs);
			    	logger.info("Response SRT : ");
			    	//logger.info(responseString);
			    	fileWriteStrConcepts.append(NetBasetUtil.writeConcepts(responseString,paramSet));
			    	fileWriteStrSent.append(NetBasetUtil.writeSentences(responseString,paramSet));
			    	fileWriteStrDoc.append(NetBasetUtil.writeDocuments(responseString,paramSet));
			    			//writeTxtFileRetrieveSentences(responseString,apiName);
			    	logger.info("responseString fileWriteStrConcepts");
			    	//System.out.println("responseString"+fileWriteStrSent.toString());
			    	//System.out.println("responseString"+fileWriteStrDoc.toString());
			    	
			    	 if(fileWriteStrConcepts!=null && fileWriteStrConcepts.length()>0)
					    {
			    		 if(!isConceptHeaderAdded){
			    			 String headerConcepts = "concepts|documentIdx|insight|sentiment|sentenceIdx|value|topicIds|themeIds\n";
				    		 fileWriteStrConcepts.insert(0, headerConcepts);
				    		 isConceptHeaderAdded = true;
			    		 }
			    		 
						    BufferedWriter writer = new BufferedWriter(new FileWriter(logFileConcepts,true));
						    writer.write (fileWriteStrConcepts.toString());
						    writer.close();		
						    fileNameWithPath.add(logFileConcepts.getPath());
						    fileWriteStrConcepts.setLength(0);
					    }
					   // logger.in
					   
					    if(fileWriteStrDoc!=null && fileWriteStrDoc.length()>0)
					    {
					    	logger.info("Document file has the content for the Documents..");
					    	if(!isDocHeaderAdded){
					    		String headerDoc= "documents|documentId|parentDocId|deleted|properties|parent_object_value|child_object_value|child_object_value_2|topicIds|themeIds|document_idx\n";
						    	fileWriteStrDoc.insert(0,headerDoc);
						    	isDocHeaderAdded= true;
						    	logger.info("documents header added..");
					    	}
					    	
						    BufferedWriter writer = new BufferedWriter(new FileWriter(logFileDocuments,true));
						    writer.write (fileWriteStrDoc.toString());
						    writer.close();		
						    fileNameWithPath.add(logFileDocuments.getPath());
						    logger.info("documents has been written to the file and the response string has value");
						    fileWriteStrDoc.setLength(0);
					    }
					   // logger.in
					   
					    if(fileWriteStrSent!=null && fileWriteStrSent.length()>0)
					    {
					    	if(!isSentHeaderAdded){
					    		String headerSent = "errorCode|totalCount|sentence|documentIdx|sentiment|hasNegativeSentiment|hasPositiveSentiment|properties|text|value|topicIds|themeIds|sentence_Idx\n";
						    	fileWriteStrSent.insert(0, headerSent);	
						    	isSentHeaderAdded = true;
					    	}
						    BufferedWriter writer = new BufferedWriter(new FileWriter(logFileSentences,true));
						    writer.write (fileWriteStrSent.toString());
						    writer.close();		
						    //fileNameWithPath.add(logFileJsons.getPath());
						    fileNameWithPath.add(logFileSentences.getPath());
						    fileWriteStrSent.setLength(0);
					    }
			    	
			    }
	
			    if("metricValues".equals(apiName))
			    {
			    	paramSet=getParamStringWithDele(apiName,nameValuePairs);
			    	fileWriteStr.append(NetBasetUtil.writeMetricsValue(responseString,paramSet));
			    	logger.info("responseString fileWriteStr===");
			    	if(!isMetricsHeaderAdded){
			    		String header= "endDate ~~ errorCode ~~ metrics.timeUnit ~~ metrics.columns ~~ metrics.dataset.seriesName ~~ metrics.dataset.set ~~ startDate~~topicIds~~themeIds~~metricSeries\n";
			    		fileWriteStr.insert(0, header);	
				    	isMetricsHeaderAdded = true;
			    	}
			    	BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));
				    writer.write (fileWriteStr.toString());
				    writer.close();	
				    fileNameWithPath.add(logFile.getPath());
				    fileWriteStr.setLength(0);
			    }
			    if("insightCount".equals(apiName))
			    {
			    	paramSet=getParamStringWithDele(apiName,nameValuePairs);
			    	if(responseString.contains("Authors")){
			    		fileWriteStrAuthors.append(NetBasetUtil.writeInsightCount(responseString,paramSet));
		    			//writeTxtFileInsightCount(responseString,apiName);
			    		if(!isAuthorsHeaderAdded){
			    			String header = "insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.authorName~~insights.dataset.set.domain~~insights.dataset.set.value~~errorCode~~topicIds~~themeIds~~categories\n";
			    			fileWriteStrAuthors.insert(0, header);	
			    			isAuthorsHeaderAdded = true;
			    		}
			    		 BufferedWriter writer = new BufferedWriter(new FileWriter(logFileAuthors,true));
						    writer.write (fileWriteStrAuthors.toString());
						    writer.close();	
						    fileWriteStrAuthors.setLength(0);
				    fileNameWithPath.add(logFileAuthors.getPath());
			    	}else if (responseString.contains("Phrases")){
			    		fileWriteStrPhrases.append(NetBasetUtil.writeInsightCount(responseString,paramSet));
			    		if(!isPhrasesHeaderAdded){
			    			String header = "insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.value~~insights.dataset.set.stemmedForm~~errorCode~~topicIds~~themeIds~~categories\n";
			    			fileWriteStrPhrases.insert(0, header);	
			    			isPhrasesHeaderAdded = true;
			    		}
			    		 BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePhrases,true));
						    writer.write (fileWriteStrPhrases.toString());
						    writer.close();	
				    fileNameWithPath.add(logFilePhrases.getPath());
				    fileWriteStrPhrases.setLength(0);
			    	}else if (responseString.contains("Geolocation")){
			    		fileWriteStrGeoLoc.append(NetBasetUtil.writeInsightCount(responseString,paramSet));
		    			//writeTxtFileInsightCount(responseString,apiName);
			    		if(!isGeoLocHeaderAdded){
			    			String header = "insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.id~~insights.dataset.set.value~~insights.dataset.set.geoType~~insights.dataset.set.parentId~~insights.dataset.set.isoName~~errorCode~~topicIds~~themeIds~~categories\n";
			    			fileWriteStrGeoLoc.insert(0, header);	
			    			isGeoLocHeaderAdded = true;
			    		}
			    		 BufferedWriter writer = new BufferedWriter(new FileWriter(logFileGeoLoc,true));
						    writer.write(fileWriteStrGeoLoc.toString());
						    writer.close();	
				    fileNameWithPath.add(logFileGeoLoc.getPath());
				    fileWriteStrGeoLoc.setLength(0);
			    	}else{
			    		fileWriteStrOthers.append(NetBasetUtil.writeInsightCount(responseString,paramSet));
		    			//writeTxtFileInsightCount(responseString,apiName);
			    		if(!isOthersHeaderAdded){
			    			String header = "insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.value~~errorCode~~topicIds~~themeIds~~categories\n";
			    			fileWriteStrOthers.insert(0, header);	
			    			isOthersHeaderAdded = true;
			    		}
			    		 BufferedWriter writer = new BufferedWriter(new FileWriter(logFileOthers,true));
						    writer.write(fileWriteStrOthers.toString());
						    writer.close();	
				    fileNameWithPath.add(logFileOthers.getPath());
				    fileWriteStrOthers.setLength(0);
			    	}
			    	
			    }

			   // logger.in
			   
			    if(responseString!=null && responseString.length()>0)
			    {
			    	fileWriteStrJson.append(responseString+"\n");
				    BufferedWriter writer = new BufferedWriter(new FileWriter(logFileJsons,true));
				    writer.write (fileWriteStrJson.toString());
				    writer.close();		
				    fileNameWithPath.add(logFileJsons.getPath());
				    fileWriteStrJson.setLength(0);
			    }
			   // logger.in
			   
			   
			    
			}
			else
			{
				//String filePath =  returnMap.get("outpurFileLocation");
				//System.out.println("File path is : "+response.getStatusLine().getStatusCode());
				//System.out.println("File path is : "+response.toString());
				
			    logger.error("API Name : "+apiName+"\n" +"Param Set "+nameValuePairs +"\n" +"Response Status :"+response.getStatusLine().getStatusCode()+"\n"+"Response String :"+response.toString());
			    BufferedWriter bw = new BufferedWriter (new FileWriter (ErrorlogFile, true));
			    bw.write("API Name : "+apiName+"\n" +"Param Set "+nameValuePairs +"\n" +"Response Status :"+response.getStatusLine().getStatusCode()+"\n"+"Response String :"+response.toString()+"\n");
			    bw.newLine();
			    bw.flush();
			    bw.close();
				//String responseString = new BasicResponseHandler().handleResponse(response);
				//System.out.println("responseString : "+responseString);
			}
			
		}
		catch(Exception exx)
		{
			logger.error("Exception Found :"+exx.getMessage(),exx);
			exx.printStackTrace();
			
			
		}
		
	}
	
	private String getParamString(String apiName,List<NameValuePair> nameValuePairs){
		String paramSet = "|topicIds|themeIds";
		if(apiName.equals("metricValues")){
			paramSet = "|topicIds|themeIds|metricSeries";
		}else if(apiName.equals("insightCount")){
			paramSet= "|topicIds|themeIds|categories";
		}
		for (NameValuePair nameValuePair : nameValuePairs) {
			if(nameValuePair.getName().equals("topicIds")){
				paramSet = paramSet.replace("topicIds", nameValuePair.getValue());
			}else if(nameValuePair.getName().equals("themeIds")){
				paramSet = paramSet.replace("themeIds", nameValuePair.getValue());
			}else if(nameValuePair.getName().equals("metricSeries")){
				paramSet = paramSet.replace("metricSeries", nameValuePair.getValue());
			}else if(nameValuePair.getName().equals("categories")){
				paramSet = paramSet.replace("categories", nameValuePair.getValue());
			}
		}
		//System.out.println(apiName+paramSet);
		return paramSet;
		
	}
	
	private String getParamStringWithDele(String apiName,List<NameValuePair> nameValuePairs){
		String paramSet = "~~topicIds~~themeIds";
		if(apiName.equals("metricValues")){
			paramSet = "~~topicIds~~themeIds~~metricSeries";
		}else if(apiName.equals("insightCount")){
			paramSet= "~~topicIds~~themeIds~~categories";
		}
		for (NameValuePair nameValuePair : nameValuePairs) {
			if(nameValuePair.getName().equals("topicIds")){
				paramSet = paramSet.replace("topicIds", nameValuePair.getValue());
			}else if(nameValuePair.getName().equals("themeIds")){
				paramSet = paramSet.replace("themeIds", nameValuePair.getValue());
			}else if(nameValuePair.getName().equals("metricSeries")){
				paramSet = paramSet.replace("metricSeries", nameValuePair.getValue());
			}else if(nameValuePair.getName().equals("categories")){
				paramSet = paramSet.replace("categories", nameValuePair.getValue());
			}
		}
		//System.out.println(apiName+paramSet);
		return paramSet;
		
	}


	private String getElementName(String methodName)
	{
		String elementName=null;
		elementName = methodName.substring(3,methodName.length());
		return elementName;
		
	}
	
	private String getTxtString(Object obj)
	{
		//obj.
		return null;
		
	}
	
	
	private String writeTxtFileInsightCount(String responseString,
			String apiName) throws JsonParseException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				return apiName;}

	
	private String writeTxtFileMetricValues(String responseString,
			String apiName) throws JsonParseException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				return apiName;}
	
	
	
	private String writeTxtFileRetrieveSentences(String responseString,String apiName) throws JSONException, JsonParseException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return apiName;}

	
	private String getStrValue(String prefix , String value)
	{
		if(value.contains("{") && value.contains("}"))
		{
			//value = value.substring(1,(value.length()-1));
			/*if(prefix.equals("documents"))
			{
				String [] prefixArr = value.split("{");
				prefixArr[0] = prefixArr[0].replace("{", "").replace("}", "");
				prefix = prefix+prefixArr[0];
			}*/
			value = value.replace("{", "").replace("}", "");
		}
		StringBuffer strBuff =  new StringBuffer();
		String [] elements =  value.split(",");
		for(int i=0;i<elements.length;i++)
		{
			if(elements[i].contains("\""))
			{
				elements[i] = elements[i].replace("\"", "");
			}
			strBuff.append(prefix+"|"+elements[i].split(":")[0]+"|"+elements[i].split(":")[1]+"\n");
		}
		return strBuff.toString();
	}




	private List<TopicId> callNetBaseAPITheameTopicId(HttpClient httpclient, String apiName,
			HttpPost httppost, List<NameValuePair> nameValuePairs)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException, HttpResponseException {
		
		List<TopicId> returnList = new ArrayList<TopicId>();
		if(nameValuePairs!=null)
		{
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		//httppost.addHeader("AUTHORIZATION", "Basic " + encoded);
		HttpResponse response = httpclient.execute(httppost);
		//response = httpclient.execute(httppost);
		if (response.getStatusLine().getStatusCode() == 200) {
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			ObjectMapper objectMapper = new ObjectMapper();
			TopicId[] themeIds= objectMapper.readValue(responseString, TopicId[].class);
			
			returnList =  Arrays.asList(themeIds);  
			
		    
		    
		}
		else
		{
			//String filePath =  returnMap.get("outpurFileLocation");
			//System.out.println("File path is : "+response.getStatusLine().getStatusCode());
			String responseString = new BasicResponseHandler().handleResponse(response);
			//System.out.println("responseString : "+responseString);
		}
		return returnList;
	}
	
	
	public static void statup()
	{
		String zipFileName = null;
		String fileLocation = returnMap.get("outpurFileLocation");
		 List<String> fileNameList = textFiles(fileLocation);
		 if(fileNameList!=null && fileNameList.size()>0)
		 {
			 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			   //get current date time with Date()
			   Date date = new Date();
			  // System.out.println(dateFormat.format(date));
			  
			   //get current date time with Calendar()
			   Calendar cal = Calendar.getInstance();
			   zipFileName= dateFormat.format(cal.getTime()).replaceAll("/", "_");
			
			 
		 }
		 
		 try {
			createZipFile(fileNameList,fileLocation+zipFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("File not Found Errror in start up",e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("I/O Exception in start up ",e);
		}
		 
	}
	
	


	private static void createZipFile(List<String> fileNameList,String zipFileName) throws FileNotFoundException, IOException {
		String fileLocation = returnMap.get("outpurFileLocation");
		FileOutputStream fos = new FileOutputStream(zipFileName+".zip");
		ZipOutputStream zos = new ZipOutputStream(fos);

		for(int i=0;i<fileNameList.size();i++)
		{
			String file1Name = fileNameList.get(i);
			addToZipFile(file1Name, zos);
			File file = new File(fileLocation+ file1Name);
			file.delete();
		}
		

		zos.close();
		fos.close();

	}

	public static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		if(!fileName.endsWith("null"))
		{
			logger.info("Writing '" + fileName + "' to zip file");
			String fileLocation = returnMap.get("outpurFileLocation");
			File file = new File(fileLocation+fileName);
			FileInputStream fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(fileName);
			zos.putNextEntry(zipEntry);
	
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
			//zos.finish();
			zos.closeEntry();
			fis.close();
		}
	}


	public static List<String> textFiles(String directory) {
		  List<String> textFiles = new ArrayList<String>();
		  File dir = new File(directory);
		  for (File file : dir.listFiles()) {
		    if (file.getName().endsWith((".txt"))) {
		      textFiles.add(file.getName());
		    }
		  }
		  return textFiles;
		}
	
	public static Map<String, String> getConfigurtionProperty() {

		Properties prop = new Properties();
		try {

				//InputStream in = getClass().getResourceAsStream("c://netBase.properties");
				File file = new File("//opt//project//netBase.properties");
				//File file = new File("c://netBase.properties");
				FileInputStream fileInput = new FileInputStream(file);
				prop.load(fileInput);
				fileInput.close();
				Enumeration<Object> e = prop.keys();

				while (e.hasMoreElements()) {
					String param = (String) e.nextElement();
					returnMap.put(param, prop.getProperty(param));
				}
			
		} catch (IOException ioe) {

			ioe.printStackTrace();
			
		}

		return returnMap;
	}
	
	public void  writeFtp(String fileLocation) throws SocketException, IOException
	{
		try {
			String server = returnMap.get("sftpLocation");
	        int port = 21;
	        String user = returnMap.get("sftpUser");
	        String pass = returnMap.get("sftpPWD");
	        FTPClient ftpClient = new FTPClient();
	        
	        ftpClient.connect(server, port);
	        ftpClient.login(user, pass);
	        ftpClient.enterLocalPassiveMode();
	        ftpClient.enterLocalActiveMode();
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        
	        File apiFile = new File(fileLocation);
	        boolean done = ftpClient.storeFile(apiFile.getName(), new FileInputStream(apiFile));
	        if(done)
	        {
	        	logger.info("File write done in FTP");
	        }
		}
		catch(Exception exx)
		{
			logger.error("Error in writeFtp 1 for File : "+fileLocation,exx);
		}
        
        
	}
	
	/*public void  writeFtp(String fileLocation) throws FileNotFoundException
	{
		JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(returnMap.get("sftpUser") , returnMap.get("sftpLocation"), 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(returnMap.get("sftpPWD"));
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            File apiFile = new File(fileLocation);
            //sftpChannel.cd("..");
            //sftpChannel.cd("..");
            //sftpChannel.cd("Netbase");
           // System.out.println("Ftping this file "+ apiFile.getName());
            logger.info("Ftping this file "+ apiFile.getName());
            sftpChannel.put(new FileInputStream(apiFile), apiFile.getName());
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
        	logger.error("Error in writeFtp 1",e);
            //e.printStackTrace();  
        } catch (SftpException e) {
        	logger.error("Error in writeFtp 2",e);
        }
	}*/
	
	
	public static void main(String args[]) throws IOException
	{
		//topics
		
		
		/*for(int i=0;i<10;i++)
		{
			File logFile=new File("C:\\netBaseOutput\\tttt.json");
			//File file = new File("/home/erik/glassfish3/"+selectedMss+".ini");

		    BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));
		    writer.write ("Shibaji "+String.valueOf(i)+"\n");
		    System.out.println("===========>> "+logFile.getPath());
		    //Close writer
		    writer.close();		
		}*/
		
		
		NetBaseFile cc =  new NetBaseFile(); 
		cc.processAPI();
		/*System.out.println("=========================");
		NetBaseFile.busySleep(35000);
		System.out.println("=========================");*/
	}
	
}
