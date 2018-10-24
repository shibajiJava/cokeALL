package com.ko.cds.service.helper;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;



@Component
public class ServerHealthCheckServiceHelper {

	public static final Logger logger = LoggerFactory
			.getLogger(ServerHealthCheckServiceHelper.class);
	
	public String checkServerHealth() throws BadRequestException,RuntimeException{
		Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		String resourceWithParam=configProp.get("resourceWithParam");
		String endpoint = configProp.get("endpoint");
		String methodType = configProp.get("requestMethodType");
		StringBuilder serverEndPointUrl=new StringBuilder();

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		serverEndPointUrl.append(endpoint+resourceWithParam);
		
		HttpResponse response =null;
		try {
			if(methodType.equals("GET")){
				HttpGet httpReq = new HttpGet(serverEndPointUrl.toString());
				logger.info("calling the API for server health Check =>"+httpReq);
				response = httpclient.execute(httpReq);
			}else{
				HttpPost httpReq = new HttpPost(serverEndPointUrl.toString());
				logger.info("calling the API for server health Check =>"+httpReq);
				response = httpclient.execute(httpReq);
			}
			logger.info(response.toString());
			
			if(response.toString().contains("404")){
				logger.info("Rest API is not available");
				throw new BadRequestException(ErrorCode.SERVICE_NOT_FOUND,ErrorCode.GEN_SERVICE_NOT_AVAILABLE);
			}
			String responseAsString = EntityUtils.toString(response.getEntity());
			return responseAsString;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IO Exception Occured"+e.getMessage());
			logger.info("IO Exception Occured"+e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
}
