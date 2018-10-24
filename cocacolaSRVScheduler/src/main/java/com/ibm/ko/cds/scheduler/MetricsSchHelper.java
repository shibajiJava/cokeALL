package com.ibm.ko.cds.scheduler;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.ko.cds.dao.metrics.TimerDAO;
import com.ibm.ko.cds.pojo.metrics.CsvToTimerBean;
import com.ibm.ko.cds.pojo.metrics.MonitorParams;
import com.ibm.ko.cds.pojo.metrics.SnapshotBean;
import com.ibm.ko.cds.util.MetricsSchUtil;

@Component
public class MetricsSchHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(MetricsSchHelper.class);
	
	@Autowired
	TimerDAO timerDAO;
	
	/*
	 * This method will read the timer Bean and form the list of beans based on the API names and do the percentage calculation and other things 
	 * as per the business logic.
	 */
	private List<SnapshotBean> createSnapshot(List<CsvToTimerBean> timer,List<CsvToTimerBean> failureTimer,File file) throws IOException  {
		
		String fileName = file.getName();
		String snapshotTime = fileName.substring(fileName.indexOf("-")+1,fileName.lastIndexOf("."));
		SimpleDateFormat formatter = new SimpleDateFormat(MetricsSchUtil.getPropValues("fileTimeStampFormat"));
		Date date;
		List<SnapshotBean> list = new ArrayList<SnapshotBean>();
		try {
			date = formatter.parse(snapshotTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			snapshotTime = sdf.format(date);
			//System.out.println("SmapshotTime ==>"+snapshotTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error While Parsing the File Timestamp for Snapshot time creation.--"+e.getMessage()+" -- File Name ==>"+file.getAbsolutePath());
			MetricsSchUtil.renameFile(file,"error");
			file.delete();
			return null;
		}
		//get the data based on different different API...
		HashMap<String, List<CsvToTimerBean>> hashMapList = new HashMap<String, List<CsvToTimerBean>>();
		for (int i = 0; i < timer.size(); i++) {
			CsvToTimerBean b = timer.get(i);
			//forming the Key for the Map
			if(!hashMapList.containsKey(b.getApiName())){
				hashMapList.put(b.getApiName(), new ArrayList<CsvToTimerBean>());
			}
			//populating the list of beans for each of the API.
			if(hashMapList.containsKey(b.getApiName())){
				List<CsvToTimerBean> t = hashMapList.get(b.getApiName());
				t.add(b);
			}
		}
		
		List<MonitorParams> monitorParams = new ArrayList<MonitorParams>();
		monitorParams = readMonitoringParams();
		//record the success and Error count logic
		if(monitorParams != null && monitorParams.size()>0){
			for (int i = 0; i < monitorParams.size(); i++) {
				if(monitorParams.get(i).getParamName().equalsIgnoreCase("success")){
					SnapshotBean snapshotBean = new SnapshotBean();
					snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
					snapshotBean.setMonitoringRefId(monitorParams.get(i).getMonitoringId());
					snapshotBean.setApiName("SUCCESS RECORDS");
					snapshotBean.setMonitoringValue(String.valueOf(timer.size()));
					snapshotBean.setMonitoringName("ALL");
					list.add(snapshotBean);
				}
				else if(monitorParams.get(i).getParamName().equalsIgnoreCase("error")){
					SnapshotBean snapshotBean = new SnapshotBean();
					snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
					snapshotBean.setMonitoringRefId(monitorParams.get(i).getMonitoringId());
					snapshotBean.setApiName("ERROR RECORDS");
					snapshotBean.setMonitoringValue(String.valueOf(failureTimer.size()));
					snapshotBean.setMonitoringName("ALL");
					list.add(snapshotBean);
				}
			}
		}

		//do the percentage logic and dump the data in DB..
		//calculation for the percentage
		for (Iterator iterator = hashMapList.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, List<CsvToTimerBean>> entry = (Entry<String, List<CsvToTimerBean>>)  iterator.next();
			List<CsvToTimerBean> tmpList = entry.getValue();
			Integer lowerValue = tmpList.size();
			//changing the file Exponential values..
			for (int j = 0; j < tmpList.size(); j++) {
				if(tmpList.get(j).getExcutionTime().contains("E")){
					tmpList.get(j).setExcutionTime(new BigDecimal(tmpList.get(j).getExcutionTime()).toPlainString());
					//System.out.println("=====================>"+tmpList.get(j).getExcutionTime());
				}
/*				if(tmpList.get(j).getExcutionTime().contains("excutionTime")){
					System.out.println("removing the row from index "+ j);
					tmpList.remove(j);
					System.out.println("=====================>"+tmpList);
				}*/
			}
			/*System.out.println("api Name ==> " +entry.getKey());
			System.out.println("sample transaction time ==> " + tmpList.get(0).getExcutionTime());
			System.out.println("number of transaction ==> "+tmpList.size());
			*/
			CsvToTimerBean lastBeanValue = tmpList.get(tmpList.size()-1);
			//read the execution time <4seconds currently its in seconds
			if (null != monitorParams && monitorParams.size()>0){
			for (int i = 0; i < monitorParams.size(); i++) {

				if(monitorParams.get(i).getParamName().matches("[0-9.]+")){
					Integer upperValue = 0;
					try{
					for (int j = 0; j < tmpList.size(); j++) {
						if(tmpList.get(j).getExcutionTime().length()> 0 && Double.parseDouble(tmpList.get(j).getExcutionTime()) < Double.parseDouble(monitorParams.get(i).getParamName())){
							upperValue++;
						}
					}
				}catch (Exception e) {
					// TODO: handle exception
					logger.error("Error While Parsing the File .--"+e.getMessage()+" -- File Name ==>"+file.getAbsolutePath());
					e.printStackTrace();
					MetricsSchUtil.renameFile(file,"error");
					file.delete();
					return null;
				}
					
					Double percent  = (double) ((upperValue*100)/lowerValue);
					//System.out.println("number of transaction under "+monitorParams.get(i).getParamName()+"seconds ==> " +upperValue);
					//System.out.println("% of transaction under "+ monitorParams.get(i).getParamName() + " seconds ==> " + percent);
					
					SnapshotBean snapshotBean = new SnapshotBean();
					snapshotBean.setApiName(entry.getKey());
					snapshotBean.setSampleTransTime(tmpList.get(0).getExcutionTime());
					//snapshotBean.setMonitoringId(nextVal);
					snapshotBean.setMonitoringName(String.valueOf(percent));
					snapshotBean.setMonitoringRefId(monitorParams.get(i).getMonitoringId());
					snapshotBean.setMonitoringValue(String.valueOf(upperValue));
					snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
					list.add(snapshotBean);
				}else{
					//System.out.println("capturing for other params "+monitorParams.get(i).getParamName());
					boolean flag = false;
						SnapshotBean snapshotBean = new SnapshotBean();

						if(monitorParams.get(i).getParamName().equalsIgnoreCase("p95")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getP95() ? lastBeanValue.getP95() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("max")){

				              double maxExecutionTime = Double.parseDouble(((CsvToTimerBean)tmpList.get(0)).getExcutionTime());
				              for (int j = 0; j < tmpList.size(); j++) {
				                if ((((CsvToTimerBean)tmpList.get(j)).getExcutionTime() != null) && (((CsvToTimerBean)tmpList.get(j)).getExcutionTime().length() > 0) && 
				                  (maxExecutionTime < Double.parseDouble(((CsvToTimerBean)tmpList.get(j)).getExcutionTime()))) {
				                  maxExecutionTime = Double.parseDouble(((CsvToTimerBean)tmpList.get(j)).getExcutionTime());
				                }
				              }
				              snapshotBean.setApiName((String)entry.getKey());
				              snapshotBean.setSampleTransTime(((CsvToTimerBean)tmpList.get(0)).getExcutionTime());
				              snapshotBean.setMonitoringRefId(((MonitorParams)monitorParams.get(i)).getMonitoringId());
				              snapshotBean.setMonitoringName(String.valueOf(maxExecutionTime));
				              snapshotBean.setMonitoringValue(String.valueOf(tmpList.size()));
				              snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
				              list.add(snapshotBean);
				            
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("min")){

				              double minExecutionTime = Double.parseDouble(((CsvToTimerBean)tmpList.get(0)).getExcutionTime());
				              for (int j = 0; j < tmpList.size(); j++) {
				                if ((((CsvToTimerBean)tmpList.get(j)).getExcutionTime() != null) && (((CsvToTimerBean)tmpList.get(j)).getExcutionTime().length() > 0) && 
				                  (minExecutionTime > Double.parseDouble(((CsvToTimerBean)tmpList.get(j)).getExcutionTime()))) {
				                  minExecutionTime = Double.parseDouble(((CsvToTimerBean)tmpList.get(j)).getExcutionTime());
				                }
				              }
				              snapshotBean.setApiName((String)entry.getKey());
				              snapshotBean.setSampleTransTime(((CsvToTimerBean)tmpList.get(0)).getExcutionTime());
				              snapshotBean.setMonitoringRefId(((MonitorParams)monitorParams.get(i)).getMonitoringId());
				              snapshotBean.setMonitoringName(String.valueOf(minExecutionTime));
				              snapshotBean.setMonitoringValue(String.valueOf(tmpList.size()));
				              snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
				              list.add(snapshotBean);
				            
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("m15_rate")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getM15_rate() ? lastBeanValue.getM15_rate() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("m5_rate")){
							snapshotBean.setMonitoringName(null !=lastBeanValue.getM5_rate() ? lastBeanValue.getM5_rate() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("m1_rate")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getM1_rate() ? lastBeanValue.getM1_rate() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("mean_rate")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getMean_rate() ? lastBeanValue.getMean_rate() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("p99")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getP99() ? lastBeanValue.getP99() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("p999")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getP999() ? lastBeanValue.getP999() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("p98")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getP98() ? lastBeanValue.getP98()  : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("p50")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getP50() ? lastBeanValue.getP50() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("stddev")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getStddev() ? lastBeanValue.getStddev() : "0");
							flag = true;
						}else if(monitorParams.get(i).getParamName().equalsIgnoreCase("mean")){
							snapshotBean.setMonitoringName(null != lastBeanValue.getMean() ? lastBeanValue.getMean() : "0");
							flag = true;
						}else if (monitorParams.get(i).getParamName().equalsIgnoreCase("avg")){
							//total time in Request / total number of successfull request
							double totExecutionTime = 0.0;
							for (int j = 0; j < tmpList.size(); j++) {
								if(tmpList.get(j).getExcutionTime() !=null && tmpList.get(j).getExcutionTime().length()>0)
								totExecutionTime = totExecutionTime + Double.parseDouble(tmpList.get(j).getExcutionTime());
							}
							
							if(totExecutionTime != 0){
								double avgTime = totExecutionTime/tmpList.size();
										snapshotBean.setApiName(entry.getKey());
										snapshotBean.setSampleTransTime(tmpList.get(0).getExcutionTime());
										snapshotBean.setMonitoringRefId(monitorParams.get(i).getMonitoringId());
										snapshotBean.setMonitoringName(String.valueOf(avgTime));
										snapshotBean.setMonitoringValue(String.valueOf(tmpList.size()));
										snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
										list.add(snapshotBean);
							}
							
						}
						if(flag){
							snapshotBean.setApiName(entry.getKey());
							snapshotBean.setSampleTransTime(tmpList.get(0).getExcutionTime());
							snapshotBean.setMonitoringRefId(monitorParams.get(i).getMonitoringId());
							snapshotBean.setMonitoringValue(String.valueOf(tmpList.size()));
							snapshotBean.setInsertDate(Timestamp.valueOf(snapshotTime));
							//snapshotBean.setMonitoringId(nextVal);
							list.add(snapshotBean);
						}
						
				}
			}
		}else{
			//throw new NullPointerException("Mo Monitoring Params Found in DB");
			logger.error("Mo Monitoring Params Found in DB for the file ==> "+file.getAbsolutePath());
			MetricsSchUtil.renameFile(file,"error");
			file.delete();
			continue;
		}
			//recording the last Metrics Value for this API
			//System.out.println(lastBeanValue);
		}
	
		return list;
	}
	
	/*
	 * This method will read the Monitoring Params from DB.
	 */
	private List<MonitorParams> readMonitoringParams() {
		List<MonitorParams> monitorParams = new ArrayList<MonitorParams>();
		try {
		monitorParams = timerDAO.getMonitoringParams();
		//System.out.println(monitorParams);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("Error While fetching the montioring Data from DB."+e.getMessage());
		}
		return monitorParams;
	}

	/*
	 * This Method will read all the csv file form a location and call a method to parse the csv file to Jaba Bean
	 * Then it will call another method to store the list of Bean in DB
	 * After successfully storing the Data in DB it will move the file to new location by renaming the file with .processed extension.
	 * 
	 */
	
	public  void readCsvAndStoreInDB() throws NullPointerException{
		String fileLocation = MetricsSchUtil.getPropValues("movedFileLocation");
		File movedDir = new File(fileLocation);
		try {
		 if (movedDir.exists()) {
				moveFiles();
				File[] files = movedDir.listFiles();
				logger.info("Number of files present in the location => "+files.length);
				for(File file : files){  
		            String filePath = file.getAbsolutePath();  
		            String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());  
		            if("csv".equals(fileExtenstion)){
		            	//parse the csv file to Java Bean
					
		        		List<CsvToTimerBean> timerBeanList = MetricsSchUtil.parseCSVToBeanList(file);
		        		
		        		List<CsvToTimerBean> successTimerBeanList = new ArrayList<CsvToTimerBean>();
		        		
		        		List<CsvToTimerBean> failureTimerBeanList = new ArrayList<CsvToTimerBean>();
		        		
		        		if(timerBeanList !=null && timerBeanList.size()>0){
		        			logger.info("Number of Records read from the file "+file.getName()+" is ==>" +timerBeanList.size() );
				        	
	        				//form the Success Timer Bean List
	        				for (Iterator iterator = timerBeanList.iterator(); iterator.hasNext();) {
								CsvToTimerBean tmpTimerBean = (CsvToTimerBean) iterator
										.next();
								//System.out.println(tmpTimerBean);
								if("SUCCESS".equalsIgnoreCase(tmpTimerBean.getRequestStatus())){
									successTimerBeanList.add(tmpTimerBean);
								}else if("ERROR".equalsIgnoreCase(tmpTimerBean.getRequestStatus())){
									failureTimerBeanList.add(tmpTimerBean);
								}
							}
		        				//prepare the sanp shot
			        			if(null != successTimerBeanList && successTimerBeanList.size()>0){
			        				logger.info("Number of Success Request Records read from the file "+file.getName()+" is ==>" +successTimerBeanList.size() );
			        				List<SnapshotBean> snap = new ArrayList<SnapshotBean>();
					        		snap =createSnapshot(successTimerBeanList,failureTimerBeanList,file);
					        		//save to DB
					        		if (null != snap) {
					        			storeInDB(snap,file);
									}else{
										logger.error("No Snapshot Got Created as there was Error While creating the snapshot ===> "+movedDir.getAbsolutePath());
										if(file.exists()){
											MetricsSchUtil.renameFile(file,"error");
					        				file.delete();
										}
				        				continue;
									}
			        			}else{
			        				//if the empty file is present then move it to another folder..
			        				logger.error("File contains No Success Data..."+file.getAbsolutePath());
			        				MetricsSchUtil.renameFile(file,"error");
			        				file.delete();
			        				continue;
			        			}
		            }else{
		            	//if the empty file is present then move it to another folder..
        				logger.error("File contains No Data..."+file.getAbsolutePath());
        				MetricsSchUtil.renameFile(file,"error");
        				file.delete();
        				continue;
		            }
		           }else{
		        	   logger.error("This Location doesnot contains Any CSVType file to be processed.");
		           }
		        }//for loop ends here
		}else{
			logger.error("No such folder/Directory Exists. ===> "+movedDir.getAbsolutePath());;
				throw new IOException("No such folder/Directory Exists. ===> "+movedDir.getAbsolutePath());
				
		}
		} catch (IOException e) {
			logger.error("IO Exception Occured");
			e.printStackTrace();
		} catch (NullPointerException e) {
			logger.error("Null Pointer Exception Occured");
			e.printStackTrace();
		}catch (WebApplicationException e) {
			logger.error("WebApplication Exception Occured");
			e.printStackTrace();
		}
} 
	 

	
	private  void storeInDB(List<SnapshotBean> snap, File file) throws WebApplicationException{
		try {
			//Save the data in DB
			for (SnapshotBean snapshotBean : snap) {
				System.out.println("==============================data in the bean for DB ==============================");
				timerDAO.postSnaphotInfo(snapshotBean);
				System.out.println(snapshotBean);
			}
		//once the data is stored in DB move the file to new location and delete the processed file form location.
			if(file.exists()){
				String fileLocation = MetricsSchUtil.getPropValues("successFileLocation");
				file.renameTo(new File(fileLocation+file.getName()+".processed"));
				file.delete();
				logger.info("File "+file.getName()+ " is renamed to => "+file.getName()+".processed and move to processed folder");
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Error While Saving data in DB");
			e.printStackTrace();
		}
	}
	
	private void moveFiles(){
		//pick the files from the current location and move them to the Moved Folder.
		TimeZone utc = TimeZone.getTimeZone("UTC");
		   try{
				String fileLocation = MetricsSchUtil.getPropValues("currentFileLocation");
				File currentDir = new File(fileLocation);
				File[]   files = currentDir.listFiles();
				logger.info("Number of files present in the current location => "+files.length);
				if(currentDir.length() == 0){
					logger.error("There are no files in the current folder to be moved for Processing...");
					logger.info("There are no files in the current folder to be moved for Processing...");
				}else{
					for(File file : files){  
			            String filePath = file.getAbsolutePath();  
			            String fileExtenstion = filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length());  
			            if("csv".equals(fileExtenstion)){
			            	String fileName = file.getName();
			        		String snapshotTime = fileName.substring(fileName.indexOf("-")+1,fileName.lastIndexOf("."));
			        		SimpleDateFormat formatter = new SimpleDateFormat(MetricsSchUtil.getPropValues("fileTimeStampFormat"));
			        		Date fileDate;
			        		try {
			        			fileDate = formatter.parse(snapshotTime);
			        			Calendar cal = GregorianCalendar.getInstance(utc);
			        			cal.setTimeZone(utc);
			    				//get the configurable time from the csv file 
			    				Integer val = Integer.parseInt(MetricsSchUtil.getPropValues("timeGap"));
			    				//get the - 10 min 
			    		        cal.add(Calendar.MINUTE, -val);
			    		        Date systemtimeReduced = cal.getTime();
			    		        if(systemtimeReduced.after(fileDate)){
			    		        	//move the existing file to another location
			    		        	//move all the files from this location to moved folder left over files due to server restart or any other reason.
			    		        	file.renameTo(new File(MetricsSchUtil.getPropValues("movedFileLocation")+file.getName()));
					            	file.delete();
			    		        }
			        		}catch(Exception e){
			        			e.printStackTrace();
			        			logger.error("Error while parsing the Files Dates from the current Location");
			        		}
			            	
			        	}else{
			        		logger.info("There are no files with CSV extension to be moved to MOVED folder.");
			        	}
			     
			        }
				}
		   }
		   catch(Exception exx)
		   {
			   exx.printStackTrace();
			   logger.error("Error while moving the file from current location to Moved Location");
		   }
		
	}
	
	public void deleteProcessedFiles(){
		MetricsSchUtil.deteleSuccessFiles();
	}
	public void deleteErrorFiles(){
		MetricsSchUtil.deteleErroneousFiles();
	}
	
}
