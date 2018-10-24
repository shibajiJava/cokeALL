package com.ibm.ko.cds.service.janrainIntegration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.ws.rs.GET;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.ko.cds.util.CDSOUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Component
public class JanrainService {

	private static final Logger logger =  LoggerFactory.getLogger("janrainforETL_schedularTask");
    @Autowired
    private JanrainServiceHelper JanrainServiceHepler;
    
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
   		String fileLOcation = null;
    	try
    	{
    		fileLOcation = JanrainServiceHepler.getMemberInfo();
    		//System.out.println(fileLOcation);
    	}catch(Exception e){
    		e.printStackTrace();
    		logger.error("Error occured during the Jarain getMemberInfo execution");
    	}	
    	updateInsertPoolerTime();
    	//FTP the json file to ETL location
    	try {
    		StringTokenizer st = new StringTokenizer(fileLOcation,",");  
    	     while (st.hasMoreTokens()) { 
    	    	 String file = st.nextToken();
    	    	 File fileName = new File(file);
    	    	 if(fileName.isFile()){
    	    		 writeFtp(file);
    	    	 }
    	     }
		} catch (FileNotFoundException e) {
			logger.error("Error while FTPing the file to the ETL location. ");
		}
    }

   	public static void  writeFtp(String fileLocation) throws FileNotFoundException
	{
		try {
    			String server = CDSOUtils.getConfigurtionProperty().get("sftpLocation");
    	        int port = 21;
    	        String user = CDSOUtils.getConfigurtionProperty().get("sftpUser");
    	        String pass = CDSOUtils.getConfigurtionProperty().get("sftpPWD");
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
        	
        	
           /* session = jsch.getSession(CDSOUtils.getConfigurtionProperty().get("sftpUser") ,CDSOUtils.getConfigurtionProperty().get("sftpLocation"), 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(CDSOUtils.getConfigurtionProperty().get("sftpPWD"));
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            File apiFile = new File(fileLocation);
            if(CDSOUtils.getConfigurtionProperty().get("CDS_Evvironment").equalsIgnoreCase("TEST")){
            	sftpChannel.cd("Test");
            }
            //sftpChannel.
            sftpChannel.put(new FileInputStream(apiFile), apiFile.getName());
            sftpChannel.exit();
            session.disconnect();
            logger.info("File => "+fileLocation+" Successfully transfered to the FTP location");
        } catch (JSchException e) {
            e.printStackTrace();  
            logger.error("JSch error Occured"+e.getMessage());
        } catch (SftpException e) {
            e.printStackTrace();
            logger.error("STF error Occured"+e.getMessage());
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}*/
	} 
	public static Map<String, String> getConfigurtionPropertyUpdatable() {
		Properties prop = new Properties();
		try {
			InputStream in = (JanrainService.class.getClassLoader()
						.getResourceAsStream(MAPPER_FILE_LOCATION));
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
         
         SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_WITHOUT_TIMEZONE);
     	 Date lstToLastUpdateDate = new Date();
     	 
     	lstToLastUpdateDate = sdf.parse(lastUpdateTime);
		 java.sql.Timestamp timestamp = new java.sql.Timestamp(lstToLastUpdateDate.getTime());
    	 CDSOUtils.updateConfigurtionProperty(FILTER_DATE_TIME_OF_LAST_RUN,JanrainServiceHepler.presentTimeStmp);
		} catch (ParseException e) {
			logger.error("updateInsertPoolerTime :"+e);
		}
    }
    
    public static void main(String args[]){
    	String fileLocation = "c:/cds/janrainForETL/JanrainJSON_20150528-104150.txt";
    	try {
			writeFtp(fileLocation);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
