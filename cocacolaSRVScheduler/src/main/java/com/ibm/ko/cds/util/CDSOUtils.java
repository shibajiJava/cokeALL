package com.ibm.ko.cds.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 
 * This is a Utility class
 * 
 * @author IBM
 * 
 * 
 */
public class CDSOUtils {
	public static final String ERROR_CODE_KEY = "errorCode";
	public static final String ERROR_DESC_KEY = "errorDesc";
	private static final String CONFIGURATION_FILE_LOCATION = "resources/ConfigurationProp/configuration.properties";
	public static Map<String, String> returnMap = new HashMap<String, String>();
	public static Map<String, String> returnMapUpdatable = new HashMap<String, String>();
	public static Logger logger = LoggerFactory.getLogger(CDSOUtils.class);

	/**
	 * This Method is read Application Configuration form Prop file
	 * 
	 * @return map<key,value>
	 */
	public static Map<String, String> getConfigurtionProperty() {

		Properties prop = new Properties();
		try {
			if (returnMap != null || returnMap.size() == 0) {
				InputStream in = (CDSOUtils.class.getClassLoader()
						.getResourceAsStream(CONFIGURATION_FILE_LOCATION));

				prop.load(in);
				in.close();
				Enumeration<Object> e = prop.keys();

				while (e.hasMoreElements()) {
					String param = (String) e.nextElement();
					returnMap.put(param, prop.getProperty(param));
				}
			}
		} catch (IOException ioe) {

			ioe.printStackTrace();
			logger.error("Error in File read", ioe);
		}

		return returnMap;
	}

	/**
	 * This Method is read Application Configuration form Prop file
	 * 
	 * @return Map
	 */
	public static Map<String, String> getConfigurtionPropertyUpdatable() {

		Properties prop = new Properties();
		try {

			InputStream in = new FileInputStream((getConfigurtionProperty()).get("runtimePropFilePath"));

			prop.load(in);
			in.close();
			Enumeration<Object> e = prop.keys();

			while (e.hasMoreElements()) {
				String param = (String) e.nextElement();
				returnMapUpdatable.put(param, prop.getProperty(param));
			}

		} catch (IOException ioe) {

			ioe.printStackTrace();
			logger.error("Error in File read", ioe);
		}

		return returnMapUpdatable;
	}

	/**
	 * This method is to update key-value in configuration prop file
	 * 
	 * @param key
	 * @param value
	 * @return boolean
	 */
	public static boolean updateConfigurtionProperty(String key, String value) {
		boolean success = true;
		Properties prop = new Properties();
		try {

			logger.info("Configuration File Location "
					+ (getConfigurtionProperty()).get("runtimePropFilePath"));
			FileInputStream fosIn = new FileInputStream(
					(getConfigurtionProperty()).get("runtimePropFilePath"));

			prop.load(fosIn);
			fosIn.close();

			logger.info("Upate Prop File With Key : " + key + " Value :"
					+ value);
			prop.setProperty(key, value);
			FileOutputStream output = new FileOutputStream(
					(getConfigurtionProperty()).get("runtimePropFilePath"));
			prop.store(output, "This is overwrite file");
			output.flush();
			output.close();

		} catch (IOException ioe) {
			success = false;
			ioe.printStackTrace();
			logger.error("Error in File read", ioe);
		}

		return success;
	}
	
	public static String getUTCTimeForJanrainJsonFile() {
		Date localTime = new Date(); 
    	DateFormat converter = new SimpleDateFormat("yyyyMMdd-HHmmss");
        converter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return converter.format(localTime);
	}
	
	public static void jsonToCSV(String sCurrentLine ,String resultFileLocation,String profileFileLocation) throws JSONException{
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String text = sCurrentLine;
		text = text.replaceAll("\n", "");
		//System.out.println("text => "+text);
		JSONObject json = new JSONObject(text);
		JSONArray results = json.getJSONArray("results");
        ////System.out.println(CDL.toString(new JSONArray(json.get("results").toString())));
		
		for(int i=0; i<results.length();i++){
			StringBuilder resultString = new StringBuilder();
			StringBuilder profilesString = new StringBuilder();
			String uuid = results.getJSONObject(i).getString("uuid");
			//System.out.println("-----------> "+uuid);
			JSONObject objectInArrayResults = results.getJSONObject(i);
		    String[] elementNamesResults = JSONObject.getNames(objectInArrayResults);
		    for (String elementNameResult : elementNamesResults)
		    	{
		    	String valueResults = objectInArrayResults.getString(elementNameResult);
		    	valueResults = valueResults.replaceAll("\n", "");
		    	if(elementNameResult.equals("clients") || elementNameResult.equals("emails") || elementNameResult.equals("phoneNumbers")|| elementNameResult.equals("statuses") || 
		    			elementNameResult.equals("photos") || elementNameResult.equals("post_login_confirmation") || elementNameResult.equals("legalAcceptances") || 
		    			elementNameResult.equals("communicationOpts") || elementNameResult.equals("externalIds") ){
		    			JSONArray arrayInResults = (JSONArray)(results.getJSONObject(i).getJSONArray(elementNameResult));
		    			for (int m = 0; m < arrayInResults.length();  m++)
		 				{
		    				JSONObject objectInArrayfollowers = arrayInResults.getJSONObject(m);
			    			String id = arrayInResults.getJSONObject(m).getString("id");
	   				    	String[] elementNamesInArrayResults = JSONObject.getNames(objectInArrayfollowers);
	   				    	for (String elementNameInArrayResults : elementNamesInArrayResults)
	   				    	{
			   				    if(!elementNameInArrayResults.equals("id")){
			   				    	String valueInArrayResults = objectInArrayfollowers.getString(elementNameInArrayResults);
			   				    	valueInArrayResults = valueInArrayResults.replaceAll("\n", "");
			   				    	//System.out.println(uuid+"|"+elementNameResult+"|"+elementNamefollowers+"|"+ valuefollowers);
			   				    	resultString.append(uuid+"|"+elementNameResult+"|id|"+id+"|"+elementNameInArrayResults+"|"+ valueInArrayResults+"\n");
			   				    }
	   				    	}
		 				}
		    			if(arrayInResults.length() ==0 ){
		    				resultString.append(uuid+"|"+elementNameResult+"|id|null|NULL|NULL\n");
			    		}
		    		}else if(elementNameResult.equals("mailingAddress") || elementNameResult.equals("residency") || elementNameResult.equals("shippingAddress")){
		    			if(!results.getJSONObject(i).get(elementNameResult).equals(null)){
		    				JSONObject objectInResults = (JSONObject)(results.getJSONObject(i).getJSONObject(elementNameResult));
	   				    	String[] elementNamesInObjectResults = JSONObject.getNames(objectInResults);
	   				    	for (String elementNameInObjectResults : elementNamesInObjectResults)
	   				    	{
	   				    		String valueInObjectResults = objectInResults.getString(elementNameInObjectResults);
	   				    		valueInObjectResults = valueInObjectResults.replaceAll("\n", "");
	   				    		//System.out.println(uuid+"|"+elementNameResult+"|"+elementNameaccessCredentials+"|"+ valueaccessCredentials);
	   				    		resultString.append(uuid+"|"+elementNameResult+"|"+elementNameInObjectResults+"|"+ valueInObjectResults+"\n");
	   				    	}
		    			}
		    		}
			 //for profiles
		    	else if(elementNameResult.equals("profiles")){
		    		JSONArray profiles =  (JSONArray)(results.getJSONObject(i).getJSONArray(elementNameResult));
   				    for (int j = 0; j < profiles.length();  j++)
	 				{
   				    	JSONObject objectInArray = profiles.getJSONObject(j);
   				    	String profileId = profiles.getJSONObject(j).getString("id");
   				    	String[] elementNamesProfiles = JSONObject.getNames(objectInArray);
   				    	//System.out.printf("%d ELEMENTS IN CURRENT OBJECT:\n", elementNamesProfiles.length);
   				    	for (String elementNameProfile : elementNamesProfiles)
   				    	{
   				    		String valueProfiles = objectInArray.getString(elementNameProfile);
   				    		valueProfiles = valueProfiles.replaceAll("\n", "");
   				    		if(elementNameProfile.equals("followers") || elementNameProfile.equals("following") || elementNameProfile.equals("friends")){
   				    			JSONArray arrayInProfile = (JSONArray)(profiles.getJSONObject(j).getJSONArray(elementNameProfile));
   				    			for (int k = 0; k < arrayInProfile.length();  k++)
   				 				{
   				    				JSONObject objectInArrayProfile = arrayInProfile.getJSONObject(k);
   				    				String id = arrayInProfile.getJSONObject(k).getString("id");
   			   				    	String[] elementNamesInArrayProfile = JSONObject.getNames(objectInArrayProfile);
   			   				    	for (String elementNameInArrayProfile : elementNamesInArrayProfile)
   			   				    	{
   			   				    		if(!elementNameInArrayProfile.equals("id")){
   			   				    		String valueInArrayProfile = objectInArrayProfile.getString(elementNameInArrayProfile);
   			   				    		valueInArrayProfile = valueInArrayProfile.replaceAll("\n", "");
   			   				    		//System.out.println(uuid+"|"+elementNameResult+"|"+elementNameProfile+"|"+elementNamefollowers+"|"+ valuefollowers);
   			   				    		profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|id|"+id+"|"+elementNameInArrayProfile+"|"+ valueInArrayProfile+"|NULL\n");
   			   				    		}
   			   				    	}
   				 				}
   				    			if(arrayInProfile.length() ==0 ){
   				    				profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|id|null|NULL|NULL|NULL\n");
   				    			}
   				    		}
   				    		else if(elementNameProfile.equals("accessCredentials")){
   				    			if(!profiles.getJSONObject(j).get(elementNameProfile).equals(null)){
   				    				JSONObject objectInArraProfile = (JSONObject)(profiles.getJSONObject(j).getJSONObject(elementNameProfile));
   			   				    	String[] elementNamesInObjectArrayProfile = JSONObject.getNames(objectInArraProfile);
   			   				    	for (String elementNameInObjectProfile : elementNamesInObjectArrayProfile)
   			   				    	{
   			   				    		String valueInObjectProfile = objectInArraProfile.getString(elementNameInObjectProfile);
   			   				    		valueInObjectProfile = valueInObjectProfile.replaceAll("\n", "");
   			   				    		//System.out.println(uuid+"|"+elementNameResult+"|"+elementNameProfile+"|"+elementNameaccessCredentials+"|"+ valueaccessCredentials);
   			   				    		profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+elementNameInObjectProfile+"|"+ valueInObjectProfile+"|NULL|NULL|NULL\n");
   			   				    	}
   				    			}else{
   				    				profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|NULL|NULL|NULL|NULL|NULL\n");
				    			}
   				    		}
   				    		else if(elementNameProfile.equals("profile")){
   				    				JSONObject profile = (JSONObject)(profiles.getJSONObject(j).getJSONObject(elementNameProfile));
   				   				    	String[] elementNamesProfile = JSONObject.getNames(profile);
   				   				    	for (String elementNameInnerProfile : elementNamesProfile)
   				   				    	{
   				   				    		String valueProfile = profile.getString(elementNameInnerProfile);
   				   				    		valueProfile = valueProfile.replaceAll("\n", "");
   				   				    		//for array type
	   				   				    	if(elementNameInnerProfile.equals("tvShows")|| elementNameInnerProfile.equals("lookingFor") || elementNameInnerProfile.equals("activities") || elementNameInnerProfile.equals("food") || elementNameInnerProfile.equals("emails") || elementNameInnerProfile.equals("phoneNumbers") || elementNameInnerProfile.equals("tags")
	   				   				    		|| elementNameInnerProfile.equals("relationships") || elementNameInnerProfile.equals("quotes") || elementNameInnerProfile.equals("children") || elementNameInnerProfile.equals("accounts") || elementNameInnerProfile.equals("photos") || elementNameInnerProfile.equals("turnOffs") || elementNameInnerProfile.equals("sports")
	   				   				    		|| elementNameInnerProfile.equals("ims") || elementNameInnerProfile.equals("movies") || elementNameInnerProfile.equals("urls") || elementNameInnerProfile.equals("organizations") || elementNameInnerProfile.equals("turnOns") || elementNameInnerProfile.equals("languages") || elementNameInnerProfile.equals("music") || elementNameInnerProfile.equals("cars") 
	   				   				    		|| elementNameInnerProfile.equals("addresses") || elementNameInnerProfile.equals("interests") || elementNameInnerProfile.equals("books") || elementNameInnerProfile.equals("interestedInMeeting") || elementNameInnerProfile.equals("jobInterests")	|| elementNameInnerProfile.equals("heroes") || elementNameInnerProfile.equals("languagesSpoken") || elementNameInnerProfile.equals("tvShows") || elementNameInnerProfile.equals("pets")){
	 			   				    			JSONArray arrayListInInnerProfile = (JSONArray)(profile.getJSONArray(elementNameInnerProfile));
	 			   				    			for (int l = 0; l < arrayListInInnerProfile.length();  l++)
	 			   				 				{
	 			   				    				JSONObject objectInArrayInInnerProfile = arrayListInInnerProfile.getJSONObject(l);
	 			   				    				String id = arrayListInInnerProfile.getJSONObject(l).getString("id");
	 			   			   				    	String[] elementNamesInInnerProfile = JSONObject.getNames(objectInArrayInInnerProfile);
	 			   			   				    	for (String elementNameInInnerProfile : elementNamesInInnerProfile)
	 			   			   				    	{
		 			   			   				    	if(!elementNameInInnerProfile.equals("id")){
	 			   			   				    		String valueInInnerProfile = objectInArrayInInnerProfile.getString(elementNameInInnerProfile);
	 			   			   				    		valueInInnerProfile = valueInInnerProfile.replaceAll("\n", "");
	 			   			   				    		profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|id|"+id+"|"+elementNameInInnerProfile+"|"+ valueInInnerProfile+"\n");
		 			   			   				    	}
	 			   			   				    	}
	 			   				 				}
		 			   				    		if(arrayListInInnerProfile.length() == 0 ){
		 			   				    			profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|id|null|NULL|NULL\n");
	 			   				    			}
	 			   				    		}
	   				   				    	else if(elementNameInnerProfile.equals("name") || elementNameInnerProfile.equals("currentLocation") || elementNameInnerProfile.equals("bodyType"))
	   				   				    	{
		   				   				    	if(!profile.get(elementNameInnerProfile).equals(null)){
		   				   				    		JSONObject objectsInInnerProfile = (JSONObject)(profile.getJSONObject(elementNameInnerProfile));
			     			   				    	String[] elementNamesInObjectInnerProfile = JSONObject.getNames(objectsInInnerProfile);
			     			   				    	for (String elementNameInObjectInnerProfile : elementNamesInObjectInnerProfile)
			     			   				    	{
			     			   				    		String valueInObjectInnerProfile = objectsInInnerProfile.getString(elementNameInObjectInnerProfile);
			     			   				    		valueInObjectInnerProfile = valueInObjectInnerProfile.replaceAll("\n", "");
			     			   				    		//System.out.println(uuid+"|"+elementNameResult+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|"+elementNameaccessCredentials+"|"+ valueaccessCredentials);
			     			   				    		profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|"+elementNameInObjectInnerProfile+"|"+ valueInObjectInnerProfile+"|NULL|NULL\n");
			     			   				    	}
		   				   				    	}else{
		   				   				    		profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|NULL|NULL|NULL|NULL\n");
			   				   				    }
	   				   				    	}else{
	   				   				    		//System.out.println(uuid+"|"+elementNameResult+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|"+ valueProfile);
	   				   				    		profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+elementNameInnerProfile+"|"+ valueProfile+"|NULL|NULL|NULL\n");
	   				   				    	}
   				   				    	}
				    		}else if(elementNameProfile.equals("id")){
				    			profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|NULL|NULL|NULL|NULL|NULL|NULL\n");
				    		}else {
   				    		 //System.out.println(uuid+"|"+elementNameResult+"|"+elementNameProfile+"|"+ valueProfiles);
   				    		 profilesString.append(uuid+"|"+elementNameResult+"|id|"+profileId+"|"+elementNameProfile+"|"+ valueProfiles+"|NULL|NULL|NULL|NULL\n");
   				    		}
   				       
   				    	}
	 				}
		    }else{
		    	//System.out.println(uuid+"|"+elementNameResult+"|"+valueResults);
		    	if(!elementNameResult.equals("uuid")){
		    	resultString.append(uuid+"|"+elementNameResult+"|"+valueResults+"\n");
		    	}
		    }
		 }
		    writeJanrainJSONtoFile(resultString.toString(),resultFileLocation);
			writeJanrainJSONtoFile(profilesString.toString(),profileFileLocation);
	  }   
		
	}
	
	public static void writeJanrainJSONtoFile(String responseString,String fileLocation) {
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
}
