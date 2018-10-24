package com.ko.cds.filter.mockResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ko.cds.utils.CDSOUtils;

@Component
@Path("/cds/v1")
public class MockResponseService {
	
	public static Map<String, String> returnMap = new HashMap<String, String>();
	private static final String CONFIGURATION_FILE_LOCATION = "resources/ConfigurationProp/mockResponse.properties";
	
	public static Logger logger = LoggerFactory.getLogger(MockResponseService.class);
	
	@GET
	@Path("/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response generateMockResponseForGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@POST
	@Path("/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response generateMockResponseForPost(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@GET
	@Path("/points/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response generateMockResponseForPintsGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@POST
	@Path("/points/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response generateMockResponseForPintsPost(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@GET
	@Path("/config/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response generateMockResponseForConfigGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@POST
	@Path("/session/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespSessionGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@GET
	@Path("/member/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespMemberGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@POST
	@Path("/member/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespMemberPost(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	@POST
	@Path("/member/update/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespMemberUpdateIdGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}

	@GET
	@Path("/search/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespSearchSessionGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
		
	@GET
	@Path("/history/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespCodeRedemptionHistoryGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	@GET
	@Path("/csrTicket/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespCSRTicketGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	@POST
	@Path("/csrTicket/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespCSRTicketPost(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	@GET
	@Path("/member/csr/mockResponse")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getMockRespCSRMemberInfoGet(@QueryParam(value = "apiServiceName") String apiServiceName){
		return CDSOUtils.createOKResponse(getMockRespFromFile(apiServiceName));
	}
	
	private String getMockRespFromFile(String key){
		logger.info("Picking the Mock Response From the Properties File for API => " + key);
		//picking from properties file
				Map<String, String> configProp =  getMockResponseJsonProperty();
				if(configProp.containsKey(key)){
					return configProp.get(key);
				}else{
					throw new WebApplicationException("HTTP 405 Method Not Allowed");
				}
	}
	
	public static Map<String, String> getMockResponseJsonProperty() {

		Properties prop = new Properties();
		try {
			if (returnMap != null || returnMap.size() == 0) {
				InputStream in = (MockResponseService.class.getClassLoader()
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


}
