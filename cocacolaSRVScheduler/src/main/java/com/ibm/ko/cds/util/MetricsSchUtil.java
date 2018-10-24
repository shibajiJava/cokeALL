package com.ibm.ko.cds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameMappingStrategy;

import com.ibm.ko.cds.pojo.metrics.CsvToTimerBean;

@Component
public class MetricsSchUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(MetricsSchUtil.class);
	
	private static final String CONFIGURATION_FILE_LOCATION = "resources/ConfigurationProp/configuration.properties";
	
	/*
	 * This Method will
	 * read the file from a location
	 * get all the files in that location
	 * delete all the files with .processed extension
	 */
	public static void deteleProcessedFiles(){
		String fileLocation = MetricsSchUtil.getPropValues("movedFileLocation");
		File processedDir = new File(fileLocation);
		File[]   files = processedDir.listFiles();
		
		logger.info("Number of files present in the location => "+files.length);
		if(processedDir.length() == 0){
			logger.error("There are no files in the Moved folder to be deleted...");
		}else{
			boolean isFileDeleted = false;
			for(File file : files){  
	            String filePath = file.getAbsolutePath();  
	            String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());  
	            if("processed".equals(fileExtenstion)){  
	            	isFileDeleted = deleteFile(filePath);
	                if(isFileDeleted){
	                	logger.info("File '"+ filePath +"' is deleted");
	                }else{
	                	logger.error("Error deleting the file " + filePath);
	                }
	            }            
	        }
		}
	}
	
	/*
	 * This Method will
	 * read the file from a location
	 * get all the files in that location
	 * delete all the files with .processed extension
	 */
	public static void deteleSuccessFiles(){
		String fileLocation = MetricsSchUtil.getPropValues("successFileLocation");
		File processedDir = new File(fileLocation);
		File[]   files = processedDir.listFiles();
		
		logger.info("Number of files present in the location => "+files.length);
		if(processedDir.length() == 0){
			logger.error("There are no files in the processed folder to be deleted...");
		}else{
			boolean isFileDeleted = false;
			for(File file : files){  
	            String filePath = file.getAbsolutePath();  
	            String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());  
	            if("processed".equals(fileExtenstion)){  
	            	isFileDeleted = deleteFile(filePath);
	                if(isFileDeleted){
	                	logger.info("File '"+ filePath +"' is deleted");
	                }else{
	                	logger.error("Error deleting the file " + filePath);
	                }
	            }            
	        }
		}
	}
	public static void deteleErroneousFiles(){
		String fileLocation = MetricsSchUtil.getPropValues("errorFileLocation");
		File processedDir = new File(fileLocation);
		File[]   files = processedDir.listFiles();
		
		logger.info("Number of files present in the location => "+files.length);
		if(processedDir.length() == 0){
			logger.error("There are no files in the Error folder to be deleted...");
		}else{
			boolean isFileDeleted = false;
			for(File file : files){  
	            String filePath = file.getAbsolutePath();  
	            String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());  
	            if("error".equals(fileExtenstion)){  
	            	isFileDeleted = deleteFile(filePath);
	                if(isFileDeleted){
	                	logger.info("File '"+ filePath +"' is deleted");
	                }else{
	                	logger.error("Error deleting the file " + filePath);
	                }
	            }            
	        }
		}
	}
	/*
	 * This method will  parse the csv and form a Java Bean using the Open CSV API.
	 * return list of java Bean.
	 */
	public static List<CsvToTimerBean> parseCSVToBeanList(File file) throws IOException {
        
		List<CsvToTimerBean> timer = new ArrayList<CsvToTimerBean>();
		timer = null;
		try {
			//for file with no header.
			/*ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
			strat.setType(CsvToTimerBean.class);
			String[] columns = new String[] {"apiName","requestStTime","requestEndTime","excutionTime","count","requestStatus","max","mean","min","stddev","p50","p75","p95","p98","p99","p999","mean_rate","m1_rate","m5_rate","m15_rate"}; // the fields to bind do in your JavaBean
			strat.setColumnMapping(columns);

			CsvToBean<CsvToTimerBean> csvToBean = new CsvToBean<CsvToTimerBean>();
			CSVReader reader = new CSVReader(new FileReader(file));
			timer = csvToBean.parse(strat, reader);*/
			
			//for file with hearder
    	HeaderColumnNameMappingStrategy<CsvToTimerBean> beanStrategy = new HeaderColumnNameMappingStrategy<CsvToTimerBean>();
        beanStrategy.setType(CsvToTimerBean.class);
         
        CsvToBean<CsvToTimerBean> csvToBean = new CsvToBean<CsvToTimerBean>();
        CSVReader reader = new CSVReader(new FileReader(file));
        
        timer = csvToBean.parse(beanStrategy, reader);
        reader.close();
        System.out.println(timer);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Error while parsing the csv file");
			MetricsSchUtil.renameFile(file,"Error");
			file.delete();
		}
        return timer;
    }
	
	
	public static boolean  deleteFile(String fileName){
		File file = new File(fileName);
		boolean success = file.delete();
		return success;
	}
	
	public static String getPropValues(String key) throws WebApplicationException  {
		 
        String result = "";
        
		try {
			Properties prop = new Properties();
	        InputStream inputStream= new FileInputStream(MetricsSchUtil.class.getClassLoader().getResource((CONFIGURATION_FILE_LOCATION)).getPath());
	        prop.load(inputStream);
        	result = prop.getProperty(key);
		
		}catch (IOException e){
				e.printStackTrace();
				logger.error("Error While Reading the properties file.");
				throw new WebApplicationException("Error While Reading the properties file.");
		}
        // get the property value and print it out
        return result;
    }
	
	public static boolean renameFile(File file,String fileType){
		boolean success = false;
		if(file.exists()){
			if(fileType.equals("error")){
				String fileLocation = MetricsSchUtil.getPropValues("errorFileLocation");
				File errorFileLocation = new File(fileLocation);
				if(errorFileLocation.exists()){
					success = file.renameTo(new File(fileLocation+file.getName()+"."+fileType));
				}else{
					logger.error("Error File folder Location not found");
					throw new NullPointerException("Error File folder Location not found");
				}
				
			}else if (fileType.equals("processed")){
				String fileLocation = MetricsSchUtil.getPropValues("successFileLocation");
				File processsedFileLocation = new File(fileLocation);
				if(processsedFileLocation.exists()){
					success = file.renameTo(new File(fileLocation+file.getName()+"."+fileType));
				}else{
					logger.error("Processed file Folder location not found");
					throw new NullPointerException("Processed file Folder location not found");
				}
				
			}
			
		}else{
			logger.error(".csv file is not present to be renamed.");
		}
		return success;
	}
	
	private static void createNewFileEvery10Min(){
		
		String fileTimeStamp = "-06-19-2014-1724";
    	//creating this file for the first time....
    	File file = new File("c:/cds/banshri"+fileTimeStamp+".csv");
	 

    	logger.info("Old file Name -->"+file.getName());
   	if (file.exists() && file.length()>0){
		  //parse the file timestamp 
   		logger.info("file exists..and it has data also....");
		  String fileName= file.getName();
		  String t = fileName.substring(fileName.indexOf("-"),fileName.length()-4);
		  logger.info("date form the file"+ t);
		  //get the calander date 
		  	DateFormat dateFormat = new SimpleDateFormat("-MM-dd-yyyy-HHmm");
			Calendar cal = Calendar.getInstance();
			String currentTimeStamp = dateFormat.format(cal.getTime());
			logger.info("currentTimeStamp==>"+currentTimeStamp);
			//get the - 10 min timstamp
	        cal.add(Calendar.MINUTE, -10);
	        
	        String oldTimestamp = dateFormat.format(cal.getTime());
	        logger.info("oldTimestamp==>" + oldTimestamp);
		  //compare the two dates
	        if(oldTimestamp.equals(t)){
	        	//System.out.println("old file with 10 min old exists");
	        	//move the existing file to another location
	        	file.renameTo(new File("c:/cds/moved/"+file.getName()));
	        	file.delete();
	        	 //create new file if the old file is in system > 10 min.
	        	file = new File("c:/cds/banshri"+currentTimeStamp+".csv");
	        	//count=1;
	        }
	        logger.info("new file Name ---> "+file.getName());
		  
	  }
	}
}
