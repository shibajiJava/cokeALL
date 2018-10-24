package com.ibm.app.services.netBase.Util;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.app.services.netBase.NetBaseFile;

public class NetBasetUtil {
	final static Logger logger =  LoggerFactory.getLogger(NetBasetUtil.class);
	static int counterDoc=0;
	static int counterSen=0;
	public static String writeInsightCount(String text,String paramSet){
		StringBuilder resultString = new StringBuilder();
		// TODO Auto-generated method stub
		 try {
	    			//System.out.println("insights~~insightGroup~~dataset~~insightType~~set~~name~~value~~errorCode");
	    			//resultString.append("insights.insightGroup ~~ insights.dataset.insightType ~~ insights.dataset.set.name ~~ insights.dataset.set.value ~~ errorCode\n");
	    			//System.out.println("text => "+text);
	    				JSONObject json = new JSONObject(text);
	    				String values= "";
	    				JSONObject jsonObj = new JSONObject(text);
	   				    	String[] jsonElementNames = JSONObject.getNames(jsonObj);
	   				    	for (String jsonSubElementNames : jsonElementNames)
	   				    	{
	   				    		values= "";
	   				    		if(jsonSubElementNames.equals("errorCode")){
	   				    			String subElementValues = jsonObj.getString(jsonSubElementNames);
	   				    			values = values+"~~"+subElementValues; 
			   				    }
	   				    		if(values !=null && values.length()>0){
	   				    			JSONArray insights = jsonObj.getJSONArray("insights");
	   				    			if(insights.length()>0){
	   				    				int hd = 0;
	   				    				for (int i = 0; i < insights.length(); i++) {
		   				    				JSONObject objectInArrayinsights = insights.getJSONObject(i);
		   			   				    	String[] elementNamesinsights = JSONObject.getNames(objectInArrayinsights);
		   			   				    	String valueinsights= "";
		   			   				    	String header= "";
		   			   				    	for (String elementNameinsights : elementNamesinsights)
		   			   				    	{
		   			   				    	 valueinsights= "";
		   			   				   if(elementNameinsights.equals("insightGroup")){
  		   			   				    	String valueIninsights = objectInArrayinsights.getString(elementNameinsights);
  		   			   				    	if(valueIninsights.equals("Phrases")){
  		   			   				    header = ("insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.value~~insights.dataset.set.stemmedForm~~errorCode\n");
  		   			   				    		hd=0;
  		   			   				    	}else if(valueIninsights.equals("Authors")){
  		   			   				    		header = ("insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.authorName~~insights.dataset.set.domain~~insights.dataset.set.value~~errorCode\n");
  		   			   				    		hd=0;
  		   			   				    	}else if(valueIninsights.equals("Geolocation")){
  		   			   				    		header = ("insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.id~~insights.dataset.set.value~~insights.dataset.set.geoType~~insights.dataset.set.parentId~~insights.dataset.set.isoName~~errorCode\n");
  		   			   				    		hd=0;
  		   			   				    	}else{
  		   			   				    		header = ("insights.insightGroup~~insights.dataset.insightType~~insights.dataset.set.name~~insights.dataset.set.value~~errorCode\n");
  		   			   				    	}
  		   			   				    		valueinsights = valueinsights+"~~"+valueIninsights; 
	   			   				    		}
	   			   				    	if(header != null && header.length() >0 && hd==0)
	   			   				    	{
	   			   				    		//resultString.append(header);
	   			   				    		hd++;
	   			   				    	}
			   			   				    	if(valueinsights !=null && valueinsights.length()>0){
			   		   				    			JSONArray dataSet = objectInArrayinsights.getJSONArray("dataset");
			   		   				    			if(dataSet.length()>0){
			   		   				    			for (int j = 0; j < dataSet.length(); j++) {
			   		   				    				JSONObject objectInArrayDataset = dataSet.getJSONObject(j);
			   		   			   				    	String[] elementNamesDataset = JSONObject.getNames(objectInArrayDataset);
			   		   			   				    	String valueDataSet= "";
			   		   			   				    	for (String elementNameDataset : elementNamesDataset)
			   		   			   				    	{
			   		   			   				    		valueDataSet= "";
			   		   			   				    		String valueSet = "";
			   		   			   				    		if(elementNameDataset.equals("insightType")){
					   		   			   				    	String valueinDataset = objectInArrayDataset.getString(elementNameDataset);
						   			   				    		valueDataSet = valueDataSet+"~~"+valueinDataset; 
					   			   				    		}
			   		   			   				    		if(valueDataSet != null && valueDataSet.length()>0){
						   		   				    			JSONArray Set = objectInArrayDataset.getJSONArray("set");
						   		   				    			if(Set.length()>0){
							   		   				    			for (int k = 0; k < Set.length(); k++) {
							   		   				    				JSONObject objectInArrayset = Set.getJSONObject(k);
							   		   			   				    	String[] elementNamesset = JSONObject.getNames(objectInArrayset);
							   		   			   				    	valueSet = "";
							   		   			   				String missingGeoValues = "~~geoType~~parentId~~isoName";
							   		   			   				String missingAuthorValues = "~~name~~authorName~~domain~~value";
					   		   			   				    	for (String elementNameset : elementNamesset)
					   		   			   				    	{
					   		   			   				    		if(elementNameset.equals("geoType") && valueDataSet.contains("Geolocation")){
					   		   			   				    			missingGeoValues = missingGeoValues.replace("geoType", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else if(elementNameset.equals("parentId") && valueDataSet.contains("Geolocation")){
					   		   			   				    			missingGeoValues = missingGeoValues.replace("parentId", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else if(elementNameset.equals("isoName") && valueDataSet.contains("Geolocation")){
					   		   			   				    			missingGeoValues = missingGeoValues.replace("isoName", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else if (elementNameset.equals("name") && valueDataSet.contains("Authors")){
					   		   			   				    			missingAuthorValues = missingAuthorValues.replace("name", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else if (elementNameset.equals("authorName") && valueDataSet.contains("Authors")){
					   		   			   				    			missingAuthorValues = missingAuthorValues.replace("authorName", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else if (elementNameset.equals("domain") && valueDataSet.contains("Authors")){
					   		   			   				    			missingAuthorValues = missingAuthorValues.replace("domain", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else if (elementNameset.equals("value") && valueDataSet.contains("Authors")){
					   		   			   				    			missingAuthorValues = missingAuthorValues.replace("value", objectInArrayset.getString(elementNameset));
					   		   			   				    		}else{
						   		   			   				    	String valueinset = objectInArrayset.getString(elementNameset);
							   			   				    		valueSet = valueSet+"~~"+valueinset;
					   		   			   				    		}
					   		   			   				    		
							   			   				    		
					   		   			   				    	}
					   		   			   				    	if(valueDataSet.contains("Geolocation")){
					   		   			   				    		missingGeoValues= missingGeoValues.replace("geoType", "null");
						   		   			   				    	missingGeoValues= missingGeoValues.replace("parentId", "null");
						   		   			   				    	missingGeoValues= missingGeoValues.replace("isoName", "null");
						   		   			   				    	
						   		   			   				    	valueSet = valueSet+missingGeoValues;
					   		   			   				    	}
					   		   			   				    	if(valueDataSet.contains("Authors")){
							   		   			   				   missingAuthorValues= missingAuthorValues.replace("name", "null");
							   		   			   				   missingAuthorValues= missingAuthorValues.replace("domain", "null");
							   		   			   				   missingAuthorValues= missingAuthorValues.replace("value", "null");
							   		   			   				   missingAuthorValues= missingAuthorValues.replace("authorName", "null");
					   		   			   				    	
						   		   			   				    	valueSet = valueSet+missingAuthorValues;
					   		   			   				    	}
					   		   			   				    	
							   		   			   				    		//System.out.println(values+"~~insights"+valueinsights+"~~"+elementNameinsights+valueDataSet+"~~set"+valueSet);
							   		   			   				    		String t = valueinsights+valueDataSet+valueSet+values+paramSet+"\n";
							   		   			   				    		if(t.startsWith("~~")){
							   		   			   				    			t = t.replaceFirst("~~", "");
							   		   			   				    		}
							   		   			   				    		resultString.append(t);
							   		   				    			}
						   		   				    			}else{
						   		   				    					//System.out.println(values+"~~insights"+valueinsights+"~~"+elementNameinsights+valueDataSet+"~~set~~NULL~~NULL");
						   		   				    				resultString.append(valueinsights+valueDataSet+"~~NULL~~NULL"+values+paramSet+"\n");
						   		   				    			}
						   		   				    			
						   		   				    		
						   			   				    	}
			   		   			   				    	}
			   		   				    			}
			   		   				    		 }else{
			   			   				    		//System.out.println(values+"~~insights"+valueinsights+"~~dataset~~NULL"+"~~set~~NULL~~NULL");
			   			   				    		resultString.append(valueinsights+"~~NULL~~NULL~~NULL"+values+paramSet+"\n");
			   			   				    	}
			   			   				     }
		   			   				    	}
										}
	   				    			}else{
	   				    				//System.out.println(values+"~~insights~~NULL~~dataset~~NULL"+"~~set~~NULL~~NULL");
	   				    				resultString.append("NULL~~NULL~~NULL~~NULL"+values+paramSet+"\n");
	   				    			}
	   				    		}
	   				    	}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return resultString.toString();
	}
	
	public static String writeMetricsValue(String text,String paramSet){		
		StringBuilder resultString = new StringBuilder();
		 try {
	    			//System.out.println("endDate~~errorCode~~startDate~~metrics~~columns~~timeUnit~~dataset~~seriesName~~set");
	    			//resultString.append("endDate ~~ errorCode ~~ metrics.timeUnit ~~ metrics.columns ~~ metrics.dataset.seriesName ~~ metrics.dataset.set ~~ startDate\n");
	    			//System.out.println("text => "+text);
	    				JSONObject json = new JSONObject(text);
	    				String valuesEndDate= "",values1="",valuesErrorcode ="";
	    				JSONObject jsonObj = new JSONObject(text);
	   				    	String[] jsonElementNames = JSONObject.getNames(jsonObj);
	   				    	String t = "endDate ~~ errorCode";
	   				    	for (String jsonSubElementNames : jsonElementNames)
	   				    	{
	   				    		
	   				    		if(jsonSubElementNames.equals("endDate")){
	   				    			String subElementValues = jsonObj.getString(jsonSubElementNames);
	   				    			valuesEndDate = subElementValues;
			   				    }
	   				    		if((jsonSubElementNames.equals("errorCode"))){
	   				    			String subElementValues = jsonObj.getString(jsonSubElementNames);
	   				    			valuesErrorcode = subElementValues;
			   				    }
	   				    		if(jsonSubElementNames.equals("startDate")){
	   				    			String subElementValues = jsonObj.getString(jsonSubElementNames);
	   				    			values1 = subElementValues;
			   				    }
	   				    		if(valuesEndDate !=null && valuesEndDate.length()>0 && valuesErrorcode !=null && valuesErrorcode.length()>0 && values1 !=null && values1.length()>0){
	   				    			JSONArray metrics = jsonObj.getJSONArray("metrics");
	   				    			if(metrics.length()>0){
	   				    				for (int i = 0; i < metrics.length(); i++) {
		   				    				JSONObject objectInArraymetrics = metrics.getJSONObject(i);
		   			   				    	String[] elementNamesMetrics = JSONObject.getNames(objectInArraymetrics);
		   			   				    	String valueinsights= "";
		   			   				    	for (String elementNameMetrics : elementNamesMetrics)
		   			   				    	{
			   			   				    	if(elementNameMetrics.equals("timeUnit")||elementNameMetrics.equals("columns")){
		   		   			   				    	String valueIninsights = objectInArraymetrics.getString(elementNameMetrics);
		   		   			   				    	valueinsights = valueinsights+valueIninsights+"~~";
			   			   				    		}
			   			   				    	if(valueinsights !=null && valueinsights.length()>0 && StringUtils.countMatches(valueinsights, "~~") ==2){
			   		   				    			JSONArray dataSet = objectInArraymetrics.getJSONArray("dataset");
			   		   				    			if(dataSet.length()>0){
			   		   				    			for (int j = 0; j < dataSet.length(); j++) {
			   		   				    				JSONObject objectInArrayDataset = dataSet.getJSONObject(j);
			   		   			   				    	String[] elementNamesDataset = JSONObject.getNames(objectInArrayDataset);
			   		   			   				    	String valueDataSet= "";
			   		   			   				    	for (String elementNameDataset : elementNamesDataset)
			   		   			   				    	{
			   		   			   				    		String valueSet = "";
			   		   			   				    		if(elementNameDataset.equals("seriesName")){
				   		   			   				    	String valueinDataset = objectInArrayDataset.getString(elementNameDataset);
					   			   				    		valueDataSet = valueinDataset; 
					   			   				    		}
			   		   			   				    		if(valueDataSet != null && valueDataSet.length()>0){
			   		   			   				    			
						   		   				    			String Set = objectInArrayDataset.getString("set");
						   		   				    			if(Set.length()>0){
						   		   			   				    	//System.out.println(values+"metrics"+valueinsights+"~~"+"dataset"+valueDataSet+"~~"+Set);
						   		   			   				    	resultString.append(valuesEndDate+"~~"+valuesErrorcode+"~~"+valueinsights+valueDataSet+"~~"+Set+"~~"+values1+paramSet+"\n");
						   		   				    			}else{
						   		   				    				//System.out.println(values+"metrics"+valueinsights+"~~"+"dataset"+valueDataSet+"~~NULL");
						   		   				    				resultString.append(valuesEndDate+"~~"+valuesErrorcode+"~~"+valueinsights+valueDataSet+"~~NULL~~"+values1+paramSet+"\n");
						   		   				    			}
						   		   				    			
						   		   				    		
						   			   				    	}
			   		   			   				    	}
			   		   				    			}
			   		   				    		 }else{
			   			   				    		//System.out.println(values+"metrics"+valueinsights+"~~dataset~~NULL"+"~~NULL");
		   			   				    		resultString.append(valuesEndDate+"~~"+valuesErrorcode+"~~"+valueinsights+"~~NULL~~NULL~~"+values1+paramSet+"\n");
			   			   				    	}
			   			   				     }
		   			   				    	}
										}
	   				    			}else{
	   				    				//System.out.println(values+"metrics~~NULL~~NULL~~dataset~~NULL"+"~~NULL");
	   				    				resultString.append(valuesEndDate+"~~"+valuesErrorcode+"~~"+"NULL~~NULL~~NULL~~NULL~~"+values1+paramSet+"\n");
	   				    			}
	   				    		}
	   				    	}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return resultString.toString();
	} 
	
	public static String writeSentences(String text,String paramSet){
		StringBuilder resultString = new StringBuilder();
		 try {
			 		
	    			//System.out.println("sentences~~documentIdx~~sentiment~~hasNegativeSentiment~~hasPositiveSentiment~~properties~~text~~value~~errorCode~~totalCount");
	    			//resultString.append("errorCode~~totalCount~~sentences~~documentIdx~~sentiment~~hasNegativeSentiment~~hasPositiveSentiment~~properties~~text~~value\n");
	    				String values= "",valuesTotCount ="";
	    				JSONObject jsonObj = new JSONObject(text);
	   				    	String[] jsonElementNames = JSONObject.getNames(jsonObj);
	   				    	for (String jsonSubElementNames : jsonElementNames)
	   				    	{
	   				    		if(jsonSubElementNames.equals("errorCode")){
	   				    			String subElementValues = jsonObj.getString(jsonSubElementNames);
	   				    			values = "\""+subElementValues+"\""; 
	   				    		}
	   				    		if(jsonSubElementNames.equals("totalCount")){
	   				    			String subElementValues = jsonObj.getString(jsonSubElementNames);
	   				    			valuesTotCount = "\""+subElementValues+"\""; 
	   				    		}
	   				    		if(values !=null && values.length()>0 && valuesTotCount !=null && valuesTotCount.length()>0 ){
	   				    			JSONArray sentences = jsonObj.getJSONArray("sentences");
	   				    			if(sentences.length()>0){
	   				    				for (int i = 0; i < sentences.length(); i++) {
	   				    					
		   				    				JSONObject objectInArraySentences = sentences.getJSONObject(i);
		   			   				    	String[] elementNamesSentences = JSONObject.getNames(objectInArraySentences);
		   			   				    	String valueSentences= "";
		   			   				    	String t = "|documentIdx|sentiment|hasNegativeSentiment|hasPositiveSentiment";
		   			   				    	for (String elementNameSentence : elementNamesSentences)
		   			   				    	{
			   			   				    	if(!elementNameSentence.equals("properties")){
		   		   			   				    	String valueIninsights = objectInArraySentences.getString(elementNameSentence);
		   		   			   				    	//valueSentences = valueSentences+"~~"+valueIninsights; 
		   		   			   				    	if(elementNameSentence.equals("documentIdx")){
			   			   				    		 t =  t.replace("documentIdx", "\""+valueIninsights+"\"");
			   			   				    		}else if(elementNameSentence.equals("hasNegativeSentiment")){
			   			   				    		 t =  t.replace("hasNegativeSentiment", "\""+valueIninsights+"\"");
			   			   				    		}else if(elementNameSentence.equals("hasPositiveSentiment")){
			   			   				    		 t =  t.replace("hasPositiveSentiment", "\""+valueIninsights+"\"");
			   			   				    		}else if(elementNameSentence.equals("sentiment")){
				   			   				    		 t =  t.replace("sentiment", "\""+valueIninsights+"\"");
			   			   				    		}
			   			   				    	valueSentences = t;
		   			   				    		}
		   			   				    		if(valueSentences.contains("hasNegativeSentiment")){
		   			   				    			valueSentences = valueSentences.replace("hasNegativeSentiment", "");
		   			   				    		}else if(valueSentences.contains("hasPositiveSentiment")){
		   			   				    			valueSentences = valueSentences.replace("hasPositiveSentiment", "");
				   				    			}else if(valueSentences.contains("sentiment")){
				   			   				    	valueSentences = valueSentences.replace("sentiment", "");
						   				    	}
			   			   				    	if(elementNameSentence.equals("properties")){
				   			   				    	
			   		   				    			JSONObject properties = objectInArraySentences.getJSONObject("properties");
				   		   				    		String[] elementNamesProperties = JSONObject.getNames(properties);
				   		   				    		String valueProperties= "";
		   		   			   				    	for (String elementNameProperties : elementNamesProperties)
		   		   			   				    	{
		   		   			   				    		String valueinset = properties.getString(elementNameProperties);
		   		   			   				    		valueinset=valueinset.replaceAll("\"", "'");
		   		   			   				    		/*if("".equals(valueProperties))
		   		   			   				    		{
		   		   			   				    			valueProperties="\"null\"";
		   		   			   				    		}*/
		   		   			   				    		if(!"null".equals(valueinset))
		   		   			   				    		{
		   		   			   				    			valueProperties = valueProperties+"\""+valueinset+"\"";
		   		   			   				    		}
		   		   			   				    		
					   			   				    	if(valueProperties.contains("\n")){
					   			   				    		valueProperties = valueProperties.replaceAll("\n", " ");
						   			   					}
		   		   			   				    	}
		   		   			   				    	if("".equals(valueProperties))
		   		   			   				    	{
		   		   			   				    			valueProperties ="null";
		   		   			   				    	}
	   		   			   				    		//System.out.println("sentences"+valueSentences+"~~"+elementNameSentence+"~~text"+valueProperties+"~~"+values);
	   		   			   				    		resultString.append(values+"|"+valuesTotCount+"|"+"\"sentences\""+valueSentences+"|"+"\""+elementNameSentence+"\""+"|\"text\"|"+valueProperties+paramSet+"|"+counterSen+"\n");
	   		   			   				    		counterSen++;
			   		   				    		}
			   			   				     }
		   			   				      }
										}else{
		   				    				//System.out.println("sentences~~NULL~~NULL~~NULL~~NULL"+"~~properties~~text~~NULL"+"~~"+values);
		   				    				resultString.append(values+"|"+valuesTotCount+"|"+"sentences|NULL|NULL|NULL|NULL"+"|properties|text|NULL"+paramSet+"\n");
		   				    			}
	   				    		}
	   				    	}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return resultString.toString();
	}
	
	public static String writeConcepts(String text,String paramSet){
		StringBuilder resultString = new StringBuilder();
		 try {
			 String Header= "";
	    			//System.out.println("concepts~~documentIdx~~insight~~sentenceIdx~~sentiment~~value");
	    			//resultString.append("concepts~~documentIdx~~insight~~sentiment~~sentenceIdx~~value\n");
	    				JSONObject jsonObj = new JSONObject(text);
	   				    	String[] jsonElementNames = JSONObject.getNames(jsonObj);
	   				    	for (String jsonSubElementNames : jsonElementNames)
	   				    	{
	   				    		if(jsonSubElementNames.equals("concepts")){
	   				    			JSONArray sentences = jsonObj.getJSONArray("concepts");
	   				    			if(sentences.length()>0){
	   				    				for (int i = 0; i < sentences.length(); i++) {
		   				    				JSONObject objectInArraySentences = sentences.getJSONObject(i);
		   			   				    	String[] elementNamesSentences = JSONObject.getNames(objectInArraySentences);
		   			   				    	String valueSentences= "";
		   			   				    	String t = "|documentIdx|insight|sentiment|sentenceIdx|value";
		   			   				    	for (String elementNameSentence : elementNamesSentences)
		   			   				    	{
			   			   				    	if(!elementNameSentence.equals("properties")){
		   		   			   				    	String valueIninsights = objectInArraySentences.getString(elementNameSentence);
		   		   			   				    	//valueSentences = valueSentences+"~~"+valueIninsights; 
				   		   			   				if(elementNameSentence.equals("documentIdx")){
				   			   				    		 t =  t.replace("documentIdx", "\""+valueIninsights+"\"");
				   			   				    		}else if(elementNameSentence.equals("insight")){
				   			   				    		 t =  t.replace("insight",  "\""+valueIninsights+"\"");
				   			   				    		}else if(elementNameSentence.equals("sentenceIdx")){
				   			   				    		 t =  t.replace("sentenceIdx",  "\""+valueIninsights+"\"");
				   			   				    		}else if(elementNameSentence.equals("sentiment")){
					   			   				    		 t =  t.replace("sentiment",  "\""+valueIninsights+"\"");
				   			   				    		}else if(elementNameSentence.equals("value")){
					   			   				    		 t =  t.replace("value",  "\""+valueIninsights+"\"");
				   			   				    		}
				   			   				    	valueSentences = t;
			   			   				    		}
			   			   				    		if(valueSentences.contains("value")){
			   			   				    			valueSentences = valueSentences.replace("value", "");
			   			   				    		}else if(valueSentences.contains("insight")){
			   			   				    			valueSentences = valueSentences.replace("insight", "");
					   				    			}else if(valueSentences.contains("sentiment")){
					   			   				    	valueSentences = valueSentences.replace("sentiment", "");
							   				    	}else if(valueSentences.contains("sentenceIdx")){
					   			   				    	valueSentences = valueSentences.replace("sentenceIdx", "");
							   				    	}	   		   			   				    	
	   		   			   				    		
			   			   				     }
		   			   				    	//System.out.println("concepts"+valueSentences);
		   			   				    	resultString.append("concepts"+valueSentences+paramSet+"\n");
		   			   				      }
										}else{
		   				    				//System.out.println("concepts~~NULL~~NULL~~NULL~~NULL~~NULL");
		   				    				resultString.append("concepts|NULL|NULL|NULL|NULL|NULL"+paramSet+"\n");
		   				    			}
	   				    			}
	   				    		}
		}catch (JSONException e) {
			e.printStackTrace();
		} 
	
		return resultString.toString();
	}

	public static String writeDocuments(String text,String paramSet){
		//logger.info("Going to write in the file with the Documents value");
		StringBuilder resultString = new StringBuilder();
		 try {
	    			//System.out.println("documents~~documentId~~parent_object_name~~parent_object_value~~child_object_value~~child_object_value_2");
	    			//resultString.append("documents~~documentId~~parentDocId~~deleted~~properties~~parent_object_value~~child_object_value~~child_object_value_2\n");
			 			
	    				JSONObject jsonObj = new JSONObject(text);
	   				    	String[] jsonElementNames = JSONObject.getNames(jsonObj);
	   				    	for (String jsonSubElementNames : jsonElementNames)
	   				    	{
	   				    		if(jsonSubElementNames.equals("documents")){
	   				    			JSONArray documents = jsonObj.getJSONArray("documents");
	   				    			if(documents.length()>0){
	   				    				//logger.info("Json documents value and will be written in the file.");
	   				    				for (int i = 0; i < documents.length(); i++) {
		   				    				JSONObject objectInArrayDocuments = documents.getJSONObject(i);
		   			   				    	String[] elementNamesDocuments = JSONObject.getNames(objectInArrayDocuments);
		   			   				    	String valueSentences= "";
		   			   				    	StringBuilder valueInProperties = new StringBuilder();
		   			   				    	String t = "|documentId|parentDocId|deleted";
		   			   				    	for (String elementNameDocument : elementNamesDocuments)
		   			   				    	{
		   			   				    		//logger.info("reading for element"+ elementNameDocument);
			   			   				    	if(!elementNameDocument.equals("properties")){
			   			   				    	String valueIninsights = objectInArrayDocuments.getString(elementNameDocument);
			   			   				    	if(elementNameDocument.equals("documentId")){
		   			   				    		 t =  t.replace("documentId", "\""+valueIninsights+"\"");
		   			   				    		}else if(elementNameDocument.equals("parentDocId")){
		   			   				    		 t =  t.replace("parentDocId", "\""+valueIninsights+"\"");
		   			   				    		}else if(elementNameDocument.equals("deleted")){
		   			   				    		 t =  t.replace("deleted", "\""+valueIninsights+"\"");
		   			   				    		}
			   			   				    	valueSentences = t;
			   			   				    	//logger.info("Value Sentences "+ elementNameDocument);
		   			   				    		}
			   			   				
		   			   				    		if(valueSentences.contains("deleted")){
		   			   				    			valueSentences = valueSentences.replace("deleted", "null");
		   			   				    		}if(valueSentences.contains("parentDocId")){
		   			   				    		valueSentences = valueSentences.replace("parentDocId", "null");
				   				    				}
							   			   			//if(valueSentences !=null && valueSentences.length()>0 && StringUtils.countMatches(valueSentences, "~~")==3){
							   			   				//logger.info("other than properties rest all values has been written in the file.");
							   				    		if(elementNameDocument.equals("properties")){
							   				    			JSONObject objectInArrayProperties = objectInArrayDocuments.getJSONObject(elementNameDocument);
						   			   				    	String[] elementNamesProperties = JSONObject.getNames(objectInArrayProperties);
						   			   				    	for (String elementNameProperties : elementNamesProperties)
						   			   				    	{
							   			   				    	if((elementNameProperties.equals("authorGeoInfo")||elementNameProperties.equals("mentionedUrls")) && objectInArrayProperties.getString(elementNameProperties) != "null"){
							   			   				    		JSONArray authorGeoInfo = objectInArrayProperties.getJSONArray(elementNameProperties);
									   				    			for (int k = 0; k < authorGeoInfo.length();  k++)
									   				 				{
									   				    				JSONObject objectInArrayauthorGeoInfo = authorGeoInfo.getJSONObject(k);
									   			   				    	String[] elementNamesauthorGeoInfo = JSONObject.getNames(objectInArrayauthorGeoInfo);
									   			   				    	for (String elementNameauthorGeoInfo : elementNamesauthorGeoInfo)
									   			   				    	{
									   			   				    		String valuefollowers = objectInArrayauthorGeoInfo.getString(elementNameauthorGeoInfo);
											   			   				    if(valuefollowers.contains("\n")){
											   			   						valuefollowers = valuefollowers.replaceAll("\n", " ");
											   			   					}
											   			   				    valuefollowers = valuefollowers.replace("\"", "");
									   			   				    		//System.out.println(jsonSubElementNames+valueSentences+"~~"+elementNameDocument+"~~"+elementNameProperties+"~~"+elementNameauthorGeoInfo+"~~"+ valuefollowers);
									   			   				    		//resultString.append(jsonSubElementNames+valueSentences+"~~"+elementNameDocument+"~~"+elementNameProperties+"~~"+elementNameauthorGeoInfo+"~~"+ valuefollowers+paramSet+"\n");
											   			   				    if("".equals(elementNameDocument.trim()))
											   			   				    {
											   			   				    	elementNameDocument="null";
											   			   				    }
												   			   				if("".equals(elementNameProperties.trim()))
											   			   				    {
												   			   				elementNameProperties="null";
											   			   				    }
												   			   			if(!valuefollowers.equals("null") && valuefollowers!=null)
												   			   			{
												   			   				valueInProperties.append("jsonSubElementNames|valueSentences"+"|\""+elementNameDocument+"\"|\""+elementNameProperties+"\"|\""+elementNameauthorGeoInfo+"\"|\""+ valuefollowers+"\""+paramSet+"|"+counterDoc+"\n");
												   			   			}
												   			   			else
												   			   			{
												   			   				valueInProperties.append("jsonSubElementNames|valueSentences"+"|\""+elementNameDocument+"\"|\""+elementNameProperties+"\"|\""+elementNameauthorGeoInfo+"\"|"+ valuefollowers+paramSet+"|"+counterDoc+"\n");
												   			   			}
									   			   				    	counterDoc++;
									   			   				    	}
									   				 				}
									   				    			if(authorGeoInfo.length() ==0 ){
									   				    				//System.out.println(jsonSubElementNames+valueSentences+"~~"+elementNameDocument+"~~"+elementNameProperties+"~~NULL~~NULL");
									   				    				//resultString.append(jsonSubElementNames+valueSentences+"~~"+elementNameDocument+"~~"+elementNameProperties+"~~NULL~~NULL"+paramSet+"\n");
									   				    				if("".equals(elementNameDocument.trim()))
										   			   				    {
										   			   				    	elementNameDocument="null";
										   			   				    }
											   			   				if("".equals(elementNameProperties.trim()))
										   			   				    {
											   			   					elementNameProperties="null";
										   			   				    }
									   				    				valueInProperties.append("jsonSubElementNames|valueSentences"+"|\""+elementNameDocument+"\"|\""+elementNameProperties+"\"|NULL|NULL"+paramSet+"|"+counterDoc+"\n");
									   				    				counterDoc++;
									   				    			}
							   			   				    	}else{
							   			   				    		String valueProperties = objectInArrayProperties.getString(elementNameProperties);
								   			   				    	
							   			   				    		//System.out.println(jsonSubElementNames+valueSentences+"~~"+elementNameDocument+"~~"+elementNameProperties+"~~"+ valueProperties+"~~NULL");
							   			   				    	//resultString.append(jsonSubElementNames+valueSentences+"~~"+elementNameDocument+"~~"+elementNameProperties+"~~"+ valueProperties+"~~NULL"+paramSet+"\n");
							   			   				    	if("".equals(elementNameDocument.trim()))
								   			   				    {
								   			   				    	elementNameDocument="null";
								   			   				    }
									   			   				if("".equals(elementNameProperties.trim()))
								   			   				    {
									   			   					elementNameProperties="null";
								   			   				    }
									   			   				if(!valueProperties.equals("null") && valueProperties!=null )
									   			   				{
									   			   					if(valueProperties.contains("\n")){
							   			   				    		 valueProperties = valueProperties.replaceAll("\n", " ");
									   			   					}
							   			   				    	 	valueProperties = valueProperties.replace("\"", "");
									   			   					valueInProperties.append("jsonSubElementNames|valueSentences"+"|\""+elementNameDocument+"\"|\""+elementNameProperties+"\"|\""+ valueProperties+"\"|NULL"+paramSet+"|"+counterDoc+"\n");
									   			   				}
									   			   				else
									   			   				{
									   			   					
									   			   					valueInProperties.append("jsonSubElementNames|valueSentences"+"|\""+elementNameDocument+"\"|\""+elementNameProperties+"\"|"+ valueProperties+"|NULL"+paramSet+"|"+counterDoc+"\n");
									   			   				}
							   			   				    	counterDoc++;
						   			   				    		}
						   			   				    	}
							   				    		}
							   			   			//}
			   			   				     }
		   			   				    	String f = valueInProperties.toString();
		   			   				    	f = f.replaceAll("jsonSubElementNames", jsonSubElementNames);
		   			   				    	if("".equals(valueSentences.trim()))
		   			   				    	{
		   			   				    		valueSentences="null";
		   			   				    	}
		   			   				    	valueSentences=valueSentences.substring(1, valueSentences.length()) ;
		   			   				    	f= f.replaceAll("valueSentences", valueSentences);
		   			   				    	
		   			   				    	f = f.replaceAll("parentDocId", "");
		   			   				    	resultString.append(f);
		   			   				      }
										}else{
		   				    				//System.out.println(jsonSubElementNames+"~~NULL~~NULL~~NULL~~NULL~~NULL");
		   				    				resultString.append(jsonSubElementNames+"|NULL|NULL|NULL|NULL|NULL"+paramSet+"\n");
		   				    			}
	   				    			}
	   				    		}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
			e.printStackTrace();
		} 
		 //logger.info("Response String from Utils=> "+resultString.toString());
		 return resultString.toString();
	}
}
