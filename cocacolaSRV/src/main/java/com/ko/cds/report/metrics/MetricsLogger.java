package com.ko.cds.report.metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.codahale.metrics.Timer;
import com.ko.cds.report.metrics.pojo.TimerBeanToCsv;
import com.ko.cds.utils.CDSOUtils;

public class MetricsLogger  implements Runnable {
	
	 private static final Logger logger = LoggerFactory.getLogger(MetricsLogger.class);
	 private static  int count = 1; 
	 private static File file = null;
	 private static Date fileDate = null;
	 private static HashMap<String,Long> timerHashMap = new HashMap<String, Long>();
	 
	 private String startTime;
	 private String endTime;
	 private String executionTime;
	 private String requestStatus;
	 private String apiName;
	// private Iterator iterator;

	 public MetricsLogger(String startTime,String endTime,String executionTime,String requestStatus,String apiName)
	 {
		 this.startTime=startTime;
		 this.endTime=endTime;
		 this.executionTime=executionTime;
		 this.requestStatus=requestStatus;
		 this.apiName=apiName;
		 //this.iterator=iterator;
	 }
	 
	 
	 @Override
		public void run() {
			
			
		 	logTimerInfoWithoutMetrics(startTime,endTime,executionTime,requestStatus,apiName);
			
			
		}
	 
	 public void logTimerInfoWithoutMetrics(String startTime,String endTime,String executionTime,String requestStatus,String apiName){
		 List<TimerBeanToCsv> timerBeanList = new ArrayList<TimerBeanToCsv>();
		 TimerBeanToCsv timerBean = new TimerBeanToCsv();
		 timerBean.setApiName(apiName);
		 timerBean.setExcutionTime(executionTime);
			timerBean.setRequestStTime(startTime);
			timerBean.setRequestEndTime(endTime);
			timerBean.setRequestStatus(requestStatus);
			timerBeanList.add(timerBean);	
			writeCSVData(timerBeanList);
		 
	 }
	 
	 
	 public static  void logTimerInfo(String startTime,String endTime,String executionTime,String requestStatus){
    	 //List<TimerBeanToCsv> timerBeanList = new ArrayList<TimerBeanToCsv>();
    	 List<TimerBeanToCsv> timerBeanList = new ArrayList<TimerBeanToCsv>();
    	 for (Iterator iterator =   MetricsListener.REGISTRY.getTimers().entrySet().iterator(); iterator.hasNext();) {
    		 synchronized (iterator) {
				
			
        	Entry<String, Timer> entry = (Entry<String, Timer>) iterator.next();
				Timer timer = entry.getValue();
				TimerBeanToCsv timerBean = new TimerBeanToCsv();
				//metric values are in system.nanoSeconds We need to convert them to seconds
				if(timer.getCount()>0 && !timerHashMap.containsKey(entry.getKey())){
					timerHashMap.put(entry.getKey(), timer.getCount());
					timerBean.setApiName(entry.getKey());
					timerBean.setCount(timer.getCount());
					timerBean.setMean_rate(timer.getMeanRate());
					timerBean.setM1_rate(timer.getOneMinuteRate());
					timerBean.setM5_rate(timer.getFiveMinuteRate());
					timerBean.setM15_rate(timer.getFifteenMinuteRate());
					timerBean.setMax(timer.getSnapshot().getMax()/1000000000.0);
					timerBean.setMin(timer.getSnapshot().getMin()/1000000000.0);
					timerBean.setMean(timer.getSnapshot().getMean()/1000000000.0);
					timerBean.setStddev(timer.getSnapshot().getStdDev()/1000000000.0);
					timerBean.setMedian(timer.getSnapshot().getMedian()/1000000000.0);	
					timerBean.setP75(timer.getSnapshot().get75thPercentile()/1000000000.0);
					timerBean.setP95(timer.getSnapshot().get95thPercentile()/1000000000.0);
					timerBean.setP98(timer.getSnapshot().get98thPercentile()/1000000000.0);
					timerBean.setP99(timer.getSnapshot().get99thPercentile()/1000000000.0);
					timerBean.setP999(timer.getSnapshot().get999thPercentile()/1000000000.0);
					timerBean.setExcutionTime(executionTime);
					timerBean.setRequestStTime(startTime);
					timerBean.setRequestEndTime(endTime);
					timerBean.setRequestStatus(requestStatus);
					timerBeanList.add(timerBean);	
				}
				
				if((timer.getCount()>0 && timerHashMap.get(entry.getKey()) != timer.getCount())){
					timerBean.setApiName(entry.getKey());
					timerBean.setCount(timer.getCount());
					timerBean.setMean_rate(timer.getMeanRate());
					timerBean.setM1_rate(timer.getOneMinuteRate());
					timerBean.setM5_rate(timer.getFiveMinuteRate());
					timerBean.setM15_rate(timer.getFifteenMinuteRate());
					timerBean.setMax(timer.getSnapshot().getMax()/1000000000.0);
					timerBean.setMin(timer.getSnapshot().getMin()/1000000000.0);
					timerBean.setMean(timer.getSnapshot().getMean()/1000000000.0);
					timerBean.setStddev(timer.getSnapshot().getStdDev()/1000000000.0);
					timerBean.setMedian(timer.getSnapshot().getMedian()/1000000000.0);	
					timerBean.setP75(timer.getSnapshot().get75thPercentile()/1000000000.0);
					timerBean.setP95(timer.getSnapshot().get95thPercentile()/1000000000.0);
					timerBean.setP98(timer.getSnapshot().get98thPercentile()/1000000000.0);
					timerBean.setP99(timer.getSnapshot().get99thPercentile()/1000000000.0);
					timerBean.setP999(timer.getSnapshot().get999thPercentile()/1000000000.0);
					timerBean.setExcutionTime(executionTime);
					timerBean.setRequestStTime(startTime);
					timerBean.setRequestEndTime(endTime);
					timerBean.setRequestStatus(requestStatus);
					timerBeanList.add(timerBean);	
					timerHashMap.put(entry.getKey(), timer.getCount());
				}
				
			}
    	 }
        writeCSVData(timerBeanList);
	   }
	   
	  private static void writeCSVData(List<TimerBeanToCsv> timerBeans){
		  Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
		  SimpleDateFormat fileTimeStampFormatter = new SimpleDateFormat(configProp.get("fileTimeStampFormat"));
		  //making the file timestamp to be UTC
		  TimeZone utc = TimeZone.getTimeZone("UTC");
		  fileTimeStampFormatter.setTimeZone(utc);
		  
		  String fileTimeStamp =null;
	    	//creating this file for the first time....
	    	if(count == 1){
	    		Calendar cal = GregorianCalendar.getInstance(utc);
	    		fileDate = cal.getTime();
		    	fileTimeStamp = fileTimeStampFormatter.format(fileDate);
		    	//synchronized (cal) {
		    		file = new File(configProp.get("currentFileLocation")+configProp.get("fileName")+fileTimeStamp+".csv");
				//}
	    		
	    		count = 0;
			  }
	    	synchronized (file)
	    	{
		    	if (file.exists() && file.length()>0){
				  //get the calander date 
		    		Calendar cal = GregorianCalendar.getInstance(utc);
					//get the configurable time from the csv file 
					Integer val = Integer.parseInt(configProp.get("csvTimerSlot"));
					//get the - 10 min 
			        cal.add(Calendar.MINUTE, -val);
			        Date systemtimeReduced = cal.getTime();
			      
			        //compare the two dates
			        if(systemtimeReduced.after(fileDate)){
			        	//move the existing file to another location
			        	//move all the files from this location to moved folder left over files due to server restart or any other reason.
			        	//moveFiles();
			        	 //create new file if the old file is in system > 10 min.
			        	cal = GregorianCalendar.getInstance(utc);
			    		fileDate = cal.getTime();
			 	    	fileTimeStamp = fileTimeStampFormatter.format(fileDate);
			 	    	//synchronized (cal) {
			        	file = new File(configProp.get("currentFileLocation")+configProp.get("fileName")+fileTimeStamp+".csv");
			 	    	//}
			        }
			        logger.info("File Name where the data will be written ---> "+file.getName());
			  }
		        
			  	FileWriter csvFileWriter;
				try {
					csvFileWriter = new FileWriter(file,true);
			        CSVWriter csvWriter = new CSVWriter(csvFileWriter,',');
			        List<String[]> data  = toStringArray(timerBeans,file);
			        //synchronized (csvWriter) {
			        	csvWriter.writeAll(data);
					//}
			        
			        csvWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Exception While Writing the Timer data on to the CSV file");
					e.printStackTrace();
					
				}
	    	}
	    }
	  
	   private  static List<String[]> toStringArray(List<TimerBeanToCsv> timerBeans, File file) {
	        List<String[]> records = new ArrayList<String[]>();
	        //add header record t,count,max,mean,min,stddev,p50,p75,p95,p98,p99,p999,mean_rate,m1_rate,m5_rate,m15_rate,rate_unit,duration_unit
	        if(file.length()==0){
	        	records.add(new String[]{"apiName","requestStTime","requestEndTime","excutionTime","count","requestStatus","max","mean","min","stddev","p50",
	        			"p75","p95","p98","p99","p999","mean_rate","m1_rate","m5_rate","m15_rate"});
	        }
	        Iterator<TimerBeanToCsv> it = timerBeans.iterator();
	        while(it.hasNext()){
	            TimerBeanToCsv timerBean = it.next();
	            records.add(new String[]{timerBean.getApiName()
	            		,timerBean.getRequestStTime(),timerBean.getRequestEndTime(),timerBean.getExcutionTime(),
	            		String.valueOf(timerBean.getCount()),timerBean.getRequestStatus(),String.valueOf(timerBean.getMax()),String.valueOf(timerBean.getMean()),String.valueOf(timerBean.getMin()),String.valueOf(timerBean.getStddev()),String.valueOf(timerBean.getP50())
	            		,String.valueOf(timerBean.getP75()),String.valueOf(timerBean.getP95()),String.valueOf(timerBean.getP98()),String.valueOf(timerBean.getP99()),String.valueOf(timerBean.getP999())
	            		,String.valueOf(timerBean.getMean_rate()),String.valueOf(timerBean.getM1_rate()),String.valueOf(timerBean.getM5_rate()),String.valueOf(timerBean.getM15_rate())});
	        }
	        return records;
	    }
	   
	   private static  void moveFiles(){
		   Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
			String fileLocation = configProp.get("currentFileLocation");
			File currentDir = new File(fileLocation);
			File[]   files = currentDir.listFiles();
			logger.info("Number of files present in the current location => "+files.length);
			if(currentDir.length() == 0){
				logger.error("There are no files in the current folder to be moved for Processing...");
			}else{
				for(File file : files){  
		            String filePath = file.getAbsolutePath();  
		            String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());  
		            if("csv".equals(fileExtenstion)){
		            	file.renameTo(new File(configProp.get("movedFileLocation")+file.getName()));
		            	file.delete();
		        	}            
		        }
			}
		}

	

}
