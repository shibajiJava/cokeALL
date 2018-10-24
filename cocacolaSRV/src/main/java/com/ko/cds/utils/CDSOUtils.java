package com.ko.cds.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AnnotationsConfigurer;
import net.sf.oval.configuration.xml.XMLConfigurer;
import net.sf.oval.localization.context.ResourceBundleValidationContextRenderer;
import net.sf.oval.localization.message.ResourceBundleMessageResolver;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ko.cds.dao.common.SequenceNumberDAO;
import com.ko.cds.jmxImpl.JmxConfigurationProperty;
import com.ko.cds.report.metrics.MetricsLogger;
import com.ko.cds.service.helper.BunchBallActivityAsynService;
import com.ko.cds.service.helper.UpdateJanrainForOptsTask;
import com.ko.cds.service.helper.UpdateJanrainPostOptsTask;
import com.ko.cds.service.helper.UpdateJanrainTask;

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
	private static final String ERROR_MSG_SYS = "System Error in Generic Validation ";
	private static final String ERROR_MSG_MAXL = "Max length error";
	private static final String DEFAULT_PROFILE_NAME = "default";
	private static final String ENVORONMENT_KEY="CDS_Evvironment";
    private static final String JANRAIN_API_KEY="Janrain_API_update";
    private static final String JANRAIN_API_DELETE="Janrain_API_delete";
    private static final String JANRAIN_API_REPLACE="Janrain_API_replace";
    private static final String JANRAIN_API_FIND="Janrain_API_find";
    private static final String JANRAIN_URL_KEY="Janrain_url";
    private static final String JANRAIN_CLIENT_ID_KEY="Janrain_client_id";
    private static final String JANRAIN_SECRET_KEY="Janrain_client_secret";
    private static final String TYPE_NAME="type_name";
	private static final String CONFIGURATION_FILE_LOCATION = "resources/ConfigurationProp/configuration.properties";
	public static Map<String, String> returnMap = new HashMap<String, String>();
	public static Map<String, String> returnMapUpdatable = new HashMap<String, String>();
	public static Logger logger = LoggerFactory.getLogger(CDSOUtils.class);
	
	private static File validationFile = new File( CDSOUtils.class.getClassLoader().getResource("resources/validation/oval-config.xml").getPath());
	private static XMLConfigurer xmlConfigurer;
	
	public static CopyOnWriteArrayList<MetricsLogger> metricsLoggerTaskList = new CopyOnWriteArrayList<MetricsLogger>();
	public static CopyOnWriteArrayList<UpdateJanrainTask> JanrainMemberUpdateTaskList = new CopyOnWriteArrayList<UpdateJanrainTask>();
	public static CopyOnWriteArrayList<UpdateJanrainForOptsTask> JanrainOptsTaskList = new CopyOnWriteArrayList<UpdateJanrainForOptsTask>();
	public static CopyOnWriteArrayList<UpdateJanrainPostOptsTask> JanrainPostOptsTaskList = new CopyOnWriteArrayList<UpdateJanrainPostOptsTask>();
	public static CopyOnWriteArrayList<BunchBallActivityAsynService> BunchBallActivityAsynList = new CopyOnWriteArrayList<BunchBallActivityAsynService>();

	
	
	static
	{
		try{
		xmlConfigurer = new XMLConfigurer(validationFile);
		}
		catch(IOException ioexx)
		{
			logger.error("oval Validation file not found",ioexx);
			
		}
	}

	@Autowired
	SequenceNumberDAO dao;
	/**
	 * It is used to create an error response and http status code
	 * 
	 * @param statusCode
	 * @param errorCode
	 * @param errorDesc
	 * @return
	 */
	public static Response createErrorResponse(int statusCode,
			String errorCode, String errorDesc) {

		CDSOError error = new CDSOError(errorCode, errorDesc);
		Response httpResponse = Response.status(statusCode).entity(error)
				.build();

		return httpResponse;
	}

	/***
	 * 
	 * Creates a response object with status code and entity object. if there is
	 * nothing to return object will be null
	 * 
	 * @param statusCode
	 * @param object
	 * @return
	 */
	public static Response createResponse(int statusCode, Object object) {
		Response httpResponse = null;
		if (object != null) {
			httpResponse = Response.status(statusCode).entity(object).build();
		} else {
			httpResponse = Response.status(statusCode).build();
		}

		return httpResponse;
	}

	/***
	 * Creates a OK response object with the entity object If there is nothing
	 * to return pass null value
	 * 
	 * @param object
	 * @return
	 */
	public static Response createOKResponse(Object object) {
		Response httpResponse = null;
		if (object != null) {
			httpResponse = Response.ok(object).build();
		} else {
			httpResponse = Response.ok().build();
		}
		return httpResponse;
	}

	/**
	 * This method validate a business object based on annotation
	 * 
	 * @param bo
	 * @return
	 */
	public static String validate(Object bo) {

		ResourceBundleMessageResolver resolver = (ResourceBundleMessageResolver) Validator
				.getMessageResolver();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"resources.validation.validation", Locale.getDefault());
		resolver.addMessageBundle(bundle);
		// Validator.setMessageResolver(resolver);

		Validator
				.setContextRenderer(new ResourceBundleValidationContextRenderer());
		Validator validator = new Validator();
		List<ConstraintViolation> violations = validator.validate(bo);

		StringBuffer sb = new StringBuffer();
		if (violations.size() > 0) {
			for (ConstraintViolation constraintViolation : violations) {
				logger.info("Object " + bo + " is invalid.");

				logger.info("Object " + constraintViolation.getMessage());
				sb.append(constraintViolation.getMessage());
				sb.append(" ");
			}
			return sb.toString();
		} else {
			return null;
		}

	}

	/**
	 * This method validate a business object based on annotation
	 * 
	 * @param bo
	 * @param profileName
	 * @return
	 * @throws IOException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static String validate(Object bo, String profileName,
			boolean isDefaultEnable) throws IOException {

		
		try {
			if(JmxConfigurationProperty.stringMaxLegth!=null)
			{
				Class cls = Class.forName(bo.getClass().getName());
				logger.info("Max String Length :"
						+ JmxConfigurationProperty.stringMaxLegth);
				Method[] allMethods = cls.getDeclaredMethods();
				for (Method m : allMethods) {
	
					if (m.getName().startsWith("get")) {
	
						if (m.getGenericReturnType().toString()
								.equals("class java.lang.String")) {
							String value = (String) m.invoke(bo, null);
							if (value != null) {
								if (value.length() > JmxConfigurationProperty.stringMaxLegth) {
	
									return ERROR_MSG_MAXL;
								}
							}
						}
					}
				}
			}
			else
			{
				logger.info("JmxConfigurationProperty.stringMaxLegth not set");
			}
		} catch (ClassNotFoundException classNotFnExx) {
			logger.error(
					"Error in Generic Validation : ClassNotFoundException ",
					classNotFnExx);
			return ERROR_MSG_SYS;
		} catch (IllegalAccessException illAccExx) {
			logger.error(
					"Error in Generic Validation : IllegalAccessException",
					illAccExx);
			return ERROR_MSG_SYS;
		} catch (IllegalArgumentException illArgExx) {
			logger.error(
					"Error in Generic Validation : IllegalArgumentException",
					illArgExx);
			return ERROR_MSG_SYS;
		} catch (InvocationTargetException invoTarExx) {
			logger.error(
					"Error in Generic Validation : InvocationTargetException",
					invoTarExx);
			return ERROR_MSG_SYS;
		}

		// TBD : change this in listener class support with auto update option
		
		Validator validator = new Validator(xmlConfigurer,
				new AnnotationsConfigurer());
		// Guard guard = new Guard(xmlConfigurer);

		ResourceBundleMessageResolver resolver = (ResourceBundleMessageResolver) Validator
				.getMessageResolver();
		ResourceBundle bundle = ResourceBundle.getBundle(
				"resources.validation.validation", Locale.getDefault());
		resolver.addMessageBundle(bundle);
		validator.disableAllProfiles();
		// guard.disableAllProfiles();
		if (isDefaultEnable) {
			// guard.enableProfile(DEFAULT_PROFILE_NAME);
			validator.enableProfile(DEFAULT_PROFILE_NAME);
		}
		if (profileName != null) {
			// guard.enableProfile(profileName);
			validator.enableProfile(profileName);
		}
		List<ConstraintViolation> violations = validator.validate(bo,
				profileName);

		StringBuffer sb = new StringBuffer();
		if (violations.size() > 0) {
			for (ConstraintViolation constraintViolation : violations) {
				logger.info("Object " + bo + " is invalid.");

				// If getCauses is not null
				if (constraintViolation.getCauses() != null) {
					for (int i = 0; i < constraintViolation.getCauses().length; i++) {
						sb.append(constraintViolation.getCauses()[i]
								.getMessage());
						sb.append(" ");
						logger.info("constraintViolation.getCauses()["
								+ i
								+ "].getMessage() : "
								+ constraintViolation.getCauses()[i]
										.getMessage());
					}
				} else {
					logger.info("Object " + constraintViolation.getMessage());
					sb.append(constraintViolation.getMessage());
					sb.append(" ");
				}
			}
			validator=null;
			return sb.toString();
		} else {
			validator=null;
			return null;
		}

	}

	public static String getCurrentUTCTimestamp() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");

		final TimeZone utc = TimeZone.getTimeZone("UTC");
		dateFormatter.setTimeZone(utc);
		
		Calendar cal = GregorianCalendar.getInstance(utc);
		Timestamp todayTimeStamp = new Timestamp(cal.getTimeInMillis());
		return dateFormatter.format(todayTimeStamp);
	}
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
	
	public static boolean updateConfigurtionPropertyForCreateMemberPoll(String key, String value) {
		boolean success = true;
		Properties prop = new Properties();
		try {

			logger.info("Configuration File Location "
					+ (getConfigurtionProperty()).get("janrainCreateMemberPropFilePath"));
			FileInputStream fosIn = new FileInputStream(
					(getConfigurtionProperty()).get("janrainCreateMemberPropFilePath"));

			prop.load(fosIn);
			fosIn.close();

			logger.info("Upate Prop File With Key : " + key + " Value :"
					+ value);
			prop.setProperty(key, value);
			FileOutputStream output = new FileOutputStream(
					(getConfigurtionProperty()).get("janrainCreateMemberPropFilePath"));
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
	
	/**
	 * @throws UnsupportedEncodingException 
	 * This Method is to update Janrain with UUID
	 * @param updatdeJson
	 * @param janrainUUID
	 * @return Map with response code , response string and exception 
	 * @throws  
	 */
	public static Map<String,String> updateCDSOIDtoJanrain(String updatdeJson, String janrainUUID) 
	{
		//System.out.println("updateCDSOIDtoJanrain METHOD ------------------------");
	    Map<String, String> returnMap= new HashMap<String, String>();
	    Map<String, String> configProp =  getConfigurtionProperty();
	    String apiName=configProp.get(JANRAIN_API_KEY);
        
        String janrainUrl = configProp.get(JANRAIN_URL_KEY);
        String environmrnt = configProp.get(ENVORONMENT_KEY);
        if(environmrnt!=null && janrainUUID!=null && updatdeJson!=null)
        {
        	//System.out.println("updateCDSOIDtoJanrain METHOD ------------------------");
            String responseString="";
            StringBuilder janrainEndPointUrl=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            janrainEndPointUrl.append(janrainUrl+apiName);
            
            HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
            
            String janrainClientId  =   configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
            String janrainSecretId  =   configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
            String typeName  =   configProp.get(TYPE_NAME);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
                nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
                nameValuePairs.add(new BasicNameValuePair("uuid", janrainUUID.trim())); 
                nameValuePairs.add(new BasicNameValuePair("type_name", typeName));
                nameValuePairs.add(new BasicNameValuePair("attributes", updatdeJson));
                logger.info("Update STR : "+updatdeJson);
                String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:jyceckPX39y8").getBytes("UTF-8"));
    	        
    	        
                httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
               
                HttpResponse response = httpclient.execute(httppost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    responseString = new BasicResponseHandler().handleResponse(response);
                    logger.info("===================================OUTPUT Janrain=========================");
                    logger.info(responseString);
                    logger.info("===================================OUTPUT Janrain=========================");
                    
                }
                else
                {
                    logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
                }
                returnMap.put("StatusCode", new Integer(response.getStatusLine().getStatusCode()).toString());
                returnMap.put("ResponseSTR", responseString);
                
            }
            
            catch (ClientProtocolException clientExx) {
                logger.error("Client Protocol Exception ",clientExx);
                returnMap.put("Exception", clientExx.getMessage());
                clientExx.printStackTrace();
               
            } catch (IOException ioExx) {
                logger.error("File Not found Expection ",ioExx);
                returnMap.put("Exception", ioExx.getMessage());
                ioExx.printStackTrace();
                
            }
            
           
            
        }
        else
        {
            logger.info("Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID+" updatdeJson: "+updatdeJson);
            returnMap.put("Exception", "Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID+" updatdeJson: "+updatdeJson);
        }
        
        return returnMap;
        
	}
	
	/**
	 * This Method is to Delete Member from Janrain with UUID
	 * @param janrainUUID
	 * @return Map with response code , response string and exception 
	 */
	public static Map<String,String> deleteCDSOMemberINJanrain(String janrainUUID)
	{
	    Map<String, String> returnMap= new HashMap<String, String>();
	    Map<String, String> configProp =  getConfigurtionProperty();
	    String apiName=configProp.get(JANRAIN_API_DELETE);
        
        String janrainUrl = configProp.get(JANRAIN_URL_KEY);
        String environmrnt = configProp.get(ENVORONMENT_KEY);
        if(environmrnt!=null && janrainUUID!=null)
        {
            String responseString="";
            StringBuilder janrainEndPointUrl=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            janrainEndPointUrl.append(janrainUrl+apiName);
            
            HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
            String janrainClientId  =   configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
            String janrainSecretId  =   configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
            String typeName  =   configProp.get(TYPE_NAME);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
                nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
                nameValuePairs.add(new BasicNameValuePair("uuid", janrainUUID.trim())); 
                nameValuePairs.add(new BasicNameValuePair("type_name", typeName));
                logger.info("UUID for Delete : "+janrainUUID);
                
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:jyceckPX39y8").getBytes("UTF-8"));
    	       
    	        
                httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                // Execute HTTP Post Request
               
                HttpResponse response = httpclient.execute(httppost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    responseString = new BasicResponseHandler().handleResponse(response);
                    logger.info("===================================OUTPUT Janrain=========================");
                    logger.info(responseString);
                    logger.info("===================================OUTPUT Janrain=========================");
                    
                }
                else
                {
                    logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
                }
                returnMap.put("StatusCode", new Integer(response.getStatusLine().getStatusCode()).toString());
                returnMap.put("ResponseSTR", responseString);
                
            } catch (ClientProtocolException clientExx) {
                logger.error("Client Protocol Exception ",clientExx);
                returnMap.put("Exception", clientExx.getMessage());
               
            } catch (IOException ioExx) {
                logger.error("File Not found Expection ",ioExx);
                returnMap.put("Exception", ioExx.getMessage());
                
            }
        }
        else
        {
            logger.info("Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
            returnMap.put("Exception", "Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
        }
        
        return returnMap;
        
	}
	
	
	/**
	 * This Method is to Find ExternalIds from Janrain with UUID
	 * @param janrainUUID
	 * @return Map with response code , response string and exception 
	 */
	public static Map<String,String> findExternalIdInfo(String janrainUUID)
	{
	    Map<String, String> returnMap= new HashMap<String, String>();
	    Map<String, String> configProp =  getConfigurtionProperty();
	    String apiName=configProp.get(JANRAIN_API_FIND);
        
        String janrainUrl = configProp.get(JANRAIN_URL_KEY);
        String environmrnt = configProp.get(ENVORONMENT_KEY);

        if(environmrnt!=null && janrainUUID!=null)
        {
            String responseString="";
            StringBuilder janrainEndPointUrl=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            janrainEndPointUrl.append(janrainUrl+apiName);
            
            HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
            String janrainClientId  =   configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
            String janrainSecretId  =   configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
            String typeName  =   configProp.get(TYPE_NAME);
            String externalIdInfo = "[\"externalIds.id\",\"externalIds.type\"]";
            String filterString = "uuid = '"+janrainUUID.trim() +"'";
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
                nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
                nameValuePairs.add(new BasicNameValuePair("filter", filterString)); 
                nameValuePairs.add(new BasicNameValuePair("type_name", typeName));
                nameValuePairs.add(new BasicNameValuePair("attributes", externalIdInfo));
                logger.info("UUID for Find : "+janrainUUID);
                
                String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:jyceckPX39y8").getBytes("UTF-8"));
    	        
    	        
                httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
               
                HttpResponse response = httpclient.execute(httppost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    responseString = new BasicResponseHandler().handleResponse(response);
                    logger.info("===================================OUTPUT Janrain=========================");
                    logger.info(responseString);
                    logger.info("===================================OUTPUT Janrain=========================");
                }
                else
                {
                    logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
                }
                returnMap.put("StatusCode", new Integer(response.getStatusLine().getStatusCode()).toString());
                returnMap.put("ResponseSTR", responseString);
                
            } catch (ClientProtocolException clientExx) {
                logger.error("Client Protocol Exception ",clientExx);
                returnMap.put("Exception", clientExx.getMessage());
               
            } catch (IOException ioExx) {
                logger.error("File Not found Expection ",ioExx);
                returnMap.put("Exception", ioExx.getMessage());
                
            }
        }
        else
        {
            logger.info("Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
            returnMap.put("Exception", "Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
        }
        
        return returnMap;
        
	}
	
	
	/**
	 * This Method is to Find ExternalIds from Janrain with UUID
	 * @param janrainUUID
	 * @return Map with response code , response string and exception 
	 */
	public static Map<String,String> findOptsInfo(String janrainUUID)
	{
	    Map<String, String> returnMap= new HashMap<String, String>();
	    Map<String, String> configProp =  getConfigurtionProperty();
	    String apiName=configProp.get(JANRAIN_API_FIND);
        
        String janrainUrl = configProp.get(JANRAIN_URL_KEY);
        String environmrnt = configProp.get(ENVORONMENT_KEY);

        if(environmrnt!=null && janrainUUID!=null)
        {
            String responseString="";
            StringBuilder janrainEndPointUrl=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            janrainEndPointUrl.append(janrainUrl+apiName);
            
            HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
            String janrainClientId  =   configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
            String janrainSecretId  =   configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
            String typeName  =   configProp.get(TYPE_NAME);
            String externalIdInfo = "[\"communicationOpts.id\",\"communicationOpts.optId\"]";
            String filterString = "uuid = '"+janrainUUID.trim() +"'";
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
                nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
                nameValuePairs.add(new BasicNameValuePair("filter", filterString)); 
                nameValuePairs.add(new BasicNameValuePair("type_name", typeName));
                nameValuePairs.add(new BasicNameValuePair("attributes", externalIdInfo));
                logger.info("UUID for Find : "+janrainUUID);
                
                String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:jyceckPX39y8").getBytes("UTF-8"));
    	        
    	        
                httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
               
                HttpResponse response = httpclient.execute(httppost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    responseString = new BasicResponseHandler().handleResponse(response);
                    logger.info("===================================OUTPUT Janrain=========================");
                    logger.info(responseString);
                    logger.info("===================================OUTPUT Janrain=========================");
                }
                else
                {
                    logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
                }
                returnMap.put("StatusCode", new Integer(response.getStatusLine().getStatusCode()).toString());
                returnMap.put("ResponseSTR", responseString);
                
            } catch (ClientProtocolException clientExx) {
                logger.error("Client Protocol Exception ",clientExx);
                returnMap.put("Exception", clientExx.getMessage());
               
            } catch (IOException ioExx) {
                logger.error("File Not found Expection ",ioExx);
                returnMap.put("Exception", ioExx.getMessage());
                
            }
        }
        else
        {
            logger.info("Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
            returnMap.put("Exception", "Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
        }
        
        return returnMap;
        
	}
	
	
	/**
	 * This Method is to Delete ExternalId from Janrain with UUID
	 * @param janrainUUID
	 * @return Map with response code , response string and exception 
	 */
	public static Map<String,String> deleteExternalIdFromJanrain(String janrainUUID,Number id)
	{
	    Map<String, String> returnMap= new HashMap<String, String>();
	    Map<String, String> configProp =  getConfigurtionProperty();
	    String apiName=configProp.get(JANRAIN_API_DELETE);
        
        String janrainUrl = configProp.get(JANRAIN_URL_KEY);
        String environmrnt = configProp.get(ENVORONMENT_KEY);
        if(environmrnt!=null && janrainUUID!=null)
        {
            String responseString="";
            StringBuilder janrainEndPointUrl=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            janrainEndPointUrl.append(janrainUrl+apiName);
            
            HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
            String janrainClientId  =   configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
            String janrainSecretId  =   configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
            String typeName  =   configProp.get(TYPE_NAME);
            String attributeString = "/externalIds#"+id;
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
                nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
                nameValuePairs.add(new BasicNameValuePair("uuid", janrainUUID.trim())); 
                nameValuePairs.add(new BasicNameValuePair("type_name", typeName));
                nameValuePairs.add(new BasicNameValuePair("attribute_name", attributeString));
                logger.info("UUID for Delete : "+janrainUUID +" and Id "+id);
                
                String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:jyceckPX39y8").getBytes("UTF-8"));
    	       
    	        
                httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
               
                HttpResponse response = httpclient.execute(httppost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    responseString = new BasicResponseHandler().handleResponse(response);
                    logger.info("===================================OUTPUT Janrain=========================");
                    logger.info(responseString);
                    logger.info("===================================OUTPUT Janrain=========================");
                    
                }
                else
                {
                    logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
                }
                returnMap.put("StatusCode", new Integer(response.getStatusLine().getStatusCode()).toString());
                returnMap.put("ResponseSTR", responseString);
                
            } catch (ClientProtocolException clientExx) {
                logger.error("Client Protocol Exception ",clientExx);
                returnMap.put("Exception", clientExx.getMessage());
               
            } catch (IOException ioExx) {
                logger.error("File Not found Expection ",ioExx);
                returnMap.put("Exception", ioExx.getMessage());
                
            }
        }
        else
        {
            logger.info("Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
            returnMap.put("Exception", "Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
        }
        
        return returnMap;
        
	}
	
	/**
	 * This Method is to Replace  CommunicationOpts from Janrain with UUID
	 * @param janrainUUID
	 * @return Map with response code , response string and exception 
	 */
	public static Map<String,String> replaceCommOptsFromJanrain(String janrainUUID,Number id,String janrainJson)
	{
	    Map<String, String> returnMap= new HashMap<String, String>();
	    Map<String, String> configProp =  getConfigurtionProperty();
	    String apiName=configProp.get(JANRAIN_API_REPLACE);
        
        String janrainUrl = configProp.get(JANRAIN_URL_KEY);
        String environmrnt = configProp.get(ENVORONMENT_KEY);
        if(environmrnt!=null && janrainUUID!=null)
        {
            String responseString="";
            StringBuilder janrainEndPointUrl=new StringBuilder();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            janrainEndPointUrl.append(janrainUrl+apiName);
            
            HttpPost httppost = new HttpPost(janrainEndPointUrl.toString());
            String janrainClientId  =   configProp.get(JANRAIN_CLIENT_ID_KEY+"_"+environmrnt);
            String janrainSecretId  =   configProp.get(JANRAIN_SECRET_KEY+"_"+environmrnt);
            String typeName  =   configProp.get(TYPE_NAME);
            String attributeString = "/communicationOpts#"+id;
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("client_id", janrainClientId));
                nameValuePairs.add(new BasicNameValuePair("client_secret", janrainSecretId));
                nameValuePairs.add(new BasicNameValuePair("uuid", janrainUUID.trim())); 
                nameValuePairs.add(new BasicNameValuePair("type_name", typeName));
                nameValuePairs.add(new BasicNameValuePair("attribute_name", attributeString));
                nameValuePairs.add(new BasicNameValuePair("value", janrainJson));
                logger.info("UUID for Replace : "+janrainUUID +" and Id "+ id +" Replace String " + janrainJson);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                String encoded = DatatypeConverter.printBase64Binary(("ibm-cds:jyceckPX39y8").getBytes("UTF-8"));
    	       
    	        
                httppost.addHeader("AUTHORIZATION","Basic "+encoded);
                HttpResponse response = httpclient.execute(httppost);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    responseString = new BasicResponseHandler().handleResponse(response);
                    logger.info("===================================OUTPUT Janrain=========================");
                    logger.info(responseString);
                    logger.info("===================================OUTPUT Janrain=========================");
                    
                }
                else
                {
                    logger.info("Unable to get data from Janrain server. Status Code : "+response.getStatusLine().getStatusCode());
                }
                returnMap.put("StatusCode", new Integer(response.getStatusLine().getStatusCode()).toString());
                returnMap.put("ResponseSTR", responseString);
                
            } catch (ClientProtocolException clientExx) {
                logger.error("Client Protocol Exception ",clientExx);
                returnMap.put("Exception", clientExx.getMessage());
               
            } catch (IOException ioExx) {
                logger.error("File Not found Expection ",ioExx);
                returnMap.put("Exception", ioExx.getMessage());
                
            }
        }
        else
        {
            logger.info("Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
            returnMap.put("Exception", "Null Value for environmrnt/janrainUUID/updatdeJson environmrnt: "+environmrnt+" janrainUUID: "+janrainUUID);
        }
        
        return returnMap;
        
	}
	public static Date parseDateString(String dateFormat,String date){
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Date returnDate = null;
		try {
			returnDate = formatter.parse(date);
		} catch (ParseException e) {
			logger.info("parseDateString "+date+" Error: "+e.getMessage());
		}
		return returnDate;
	}
	public static java.sql.Date convertToSQLDate(Date utilDate){
		java.sql.Date sqlDate = null; 
		if(utilDate != null){
			sqlDate=new java.sql.Date(utilDate.getTime());
		}
		return sqlDate;

	}
	
	public static String formatDateDBDate(String dbDate){
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp dt = Timestamp.valueOf(dbDate);
		return dateFormatter.format(dt);
	}
	public static List copyList(List sourceList, int startIndex, int endIndex ){
		List returnList = new ArrayList();
		//if(sourceList != null && sourceList.size()-1 >= startIndex &&  sourceList.size()-1 >=endIndex){
			for (int i=startIndex; i<endIndex; i++) {
				returnList.add(sourceList.get(i));
			}
		//}
		return returnList;
	}
}
