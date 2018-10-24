package com.ko.cds.report.metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVWriter;

import com.ko.cds.dao.common.GenerateMetricsReportDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.report.metrics.pojo.TimerBeanToCsv;
import com.ko.cds.utils.CDSOUtils;
import com.ko.cds.utils.ErrorCode;

@Component
@Transactional
public class GenerateMetricsReport implements IGenerateMetricsServiceReport {

	@Autowired
	GenerateMetricsReportDAO dao;
	
	@Override
	public Response generateReport(String startTime, String endTime) throws BadRequestException {
		// TODO Auto-generated method stub
		//System.out.println("started");
		//covert the time
		SimpleDateFormat sentFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
		SimpleDateFormat convertedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		TimeZone utc = TimeZone.getTimeZone("UTC");
		sentFormat.setTimeZone(utc);
		convertedFormat.setTimeZone(utc);
		long successCount = 0;
		long failureCount = 0;
		try {
			if(startTime != null && startTime.length() >0){
					Date date1 = sentFormat.parse(startTime);
					String outputDate = convertedFormat.format(date1).toString();
					startTime = outputDate;
			}else{
				throw new BadRequestException("Start Date cannot be null",ErrorCode.GEN_INVALID_ARGUMENT);
			}
			
			if(endTime != null && endTime.length() >0){
					Date date1 = sentFormat.parse(endTime);
					String outputDate = convertedFormat.format(date1).toString();
					endTime = outputDate;
			}else{
				endTime = convertedFormat.format(new Date()).toString();
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		List<HashMap<Object, Object>> result = dao.getMonitoringValue();
		List<String> api_name = dao.getApi_Name();
		/*		
			for (HashMap<Object, Object> map : result) {
				//System.out.println(map);
				//System.out.println(map.get("USER_DEF_MON_VALUE"));
				//System.out.println(map.containsKey("MONITOR_DTL_ID"));
				for (Entry<Object, Object> entry : map.entrySet()) {
		            //System.out.println((Long)entry.getValue());
		                //System.out.println((String)entry.getKey());
		        }
			}*/
		List<TimerBeanToCsv> timerBeanList = new ArrayList<TimerBeanToCsv>();
			for (String api : api_name) {
				if(api.equalsIgnoreCase("ERROR RECORDS")|| api.equalsIgnoreCase("SUCCESS RECORDS")){
					continue;
				}
				TimerBeanToCsv timerBean = new TimerBeanToCsv();
				timerBean.setApiName(api);
				//System.out.println("Rinning for the API => "+api);
			 for (int i = 0; i < result.size(); i++) {
			
				/*if(result.get(i).get("USER_DEF_MON_VALUE").equals("p95")){
					String d = dao.getP95((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("p95 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setP95(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("p99")){
					String d = dao.getP99((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("p99 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setP99(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("p999")){
					String d = dao.getP999((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("p999 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setP999(Double.valueOf(d));
				}
				else*/ 
				/*else if(result.get(i).get("USER_DEF_MON_VALUE").equals("stddev")){
					String d = dao.getStdd_dev((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("stddev Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setStddev(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("p75")){
					String d = dao.getP75((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("p75 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setP75(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("p98")){
					String d = dao.getP98((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("p98 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setP98(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("mean_rate")){
					String d = dao.getMean_Rate((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("mean_rate Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setMin(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("m1_rate")){
					String d = dao.getMean1_Rate((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("m1 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setM1_rate(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("m5_rate")){
					String d = dao.getMean5_Rate((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("m5 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setM5_rate(Double.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("m15_rate")){
					String d = dao.getMean15_Rate((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("m15 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setM15_rate(Double.valueOf(d));
				}*/
				 if(result.get(i).get("USER_DEF_MON_VALUE").equals("min")){
						String d = dao.getMin((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
						//System.out.println("min Value =>"+d);
						if(d != null && !d.equals("null"))
						timerBean.setMin(Double.valueOf(d));
					}
					else if(result.get(i).get("USER_DEF_MON_VALUE").equals("max")){
						String d = dao.getMax((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
						//System.out.println("max Value =>"+d);
						if(d != null && !d.equals("null"))
						timerBean.setMax(Double.valueOf(d));
					}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("avg")){
					String d = dao.getAvg((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("avg Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setMean(Double.valueOf(d));
					d= null;
					d = dao.getCount((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("Count Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setCount(Long.valueOf(d));
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals(".2")){
					String d = dao.getRecordUnderPoint2Sec((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println(".2 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setDuration_unit(d);
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("2")){
					String d = dao.getRecordUnder2Sec((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("2 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setExcutionTime(d);
				}
				else if(result.get(i).get("USER_DEF_MON_VALUE").equals("4")){
					String d = dao.getRecordUnder4Sec((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,api,endTime);
					//System.out.println("4 Value =>"+d);
					if(d != null && !d.equals("null"))
					timerBean.setRate_unit(d);
				}
			}
			 timerBeanList.add(timerBean);
		}
			//recording the count of Succces and Error Record.
		for (int i = 0; i < result.size(); i++) {
				if(result.get(i).get("USER_DEF_MON_VALUE").equals("success")){
					TimerBeanToCsv timerBean = new TimerBeanToCsv();
					timerBean.setApiName("SUCCESS RECORD COUNT");
					String d = dao.getSuccessCount((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,endTime);
					//System.out.println("4 Value =>"+d);
					if(d != null){
					timerBean.setCount(Long.valueOf(d));
					successCount = Long.valueOf(d);
					}
					
					timerBeanList.add(timerBean);
				
				}else if(result.get(i).get("USER_DEF_MON_VALUE").equals("error")){
					TimerBeanToCsv timerBean = new TimerBeanToCsv();
					timerBean.setApiName("ERROR RECORD COUNT");
					String d = dao.getErrorCount((Long)result.get(i).get("MONITOR_DTL_ID"),startTime,endTime);
					//System.out.println("4 Value =>"+d);
					if(d != null){
					timerBean.setCount(Long.valueOf(d));
					failureCount = Long.valueOf(d);
					}
					
					timerBeanList.add(timerBean);
				}
			
			 }
			
			try {
				TimerBeanToCsv timerBean = new TimerBeanToCsv();
				timerBean.setApiName("TRANS PER SEC");
				Date d = convertedFormat.parse(startTime);
				Date d1= convertedFormat.parse(endTime);
			
				long diff = (d1.getTime() - d.getTime())/1000;
				//System.out.println("time Difference====> "+ diff);
				double d12 =  (successCount+failureCount)/diff;
				if(d12>0.0){
					timerBean.setMean(Double.valueOf(d12));
					timerBeanList.add(timerBean);
				}
				
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			//writing to the file
			Map<String, String> configProp =  CDSOUtils.getConfigurtionProperty();
			SimpleDateFormat fileTimeStampFormatter = new SimpleDateFormat(configProp.get("fileTimeStampFormat"));
			fileTimeStampFormatter.setTimeZone(utc);
			String fileTimeStamp =null;
			/*try {
				if(endTime != null){
					fileTimeStamp =fileTimeStampFormatter.format(convertedFormat.parse(startTime))+"-to"+fileTimeStampFormatter.format(convertedFormat.parse(endTime));
				}else{
					fileTimeStamp =fileTimeStampFormatter.format(convertedFormat.parse(startTime));
				}
			} catch (ParseException e) {
				// TODO: handle exception
			}*/
			Calendar cal = GregorianCalendar.getInstance(utc);
			Date fileDate = cal.getTime();
			fileTimeStamp =fileTimeStampFormatter.format(fileDate);
    		File file = new File(configProp.get("metricsFileLocation")+configProp.get("MetricsFileName")+fileTimeStamp+".csv");
			FileWriter csvFileWriter;
			try {
				csvFileWriter = new FileWriter(file,true);
		        CSVWriter csvWriter = new CSVWriter(csvFileWriter,',');
		        List<String[]> data  = toStringArray(timerBeanList,file);
		        csvWriter.writeAll(data);
		        csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println("data Written to the file " +file);
			
			return CDSOUtils.createOKResponse("File Created => "+file.getPath());
	}
	
	 private  static List<String[]> toStringArray(List<TimerBeanToCsv> timerBeans, File file) {
	        List<String[]> records = new ArrayList<String[]>();
	        //add header record t,count,max,mean,min,stddev,p50,p75,p95,p98,p99,p999,mean_rate,m1_rate,m5_rate,m15_rate,rate_unit,duration_unit
	        if(file.length()==0){
	        	/*records.add(new String[]{"ApiName","Count Of Records Considered for Average","Average","Record Under .2 Sec","Record Under 2 Sec","Record under 4 Sec","max","min","stddev",
	        			"p95","p98","p99","p999","m1_rate","m5_rate","m15_rate"});*/
	        	records.add(new String[]{"ApiName","Count Of Records Considered for Average","Average","Record Under .2 Sec","Record Under 2 Sec","Record under 4 Sec","max","min"});
	        }
	        Iterator<TimerBeanToCsv> it = timerBeans.iterator();
	        while(it.hasNext()){
	            TimerBeanToCsv timerBean = it.next();
	            /*records.add(new String[]{timerBean.getApiName()
	            		,String.valueOf(timerBean.getCount()),String.valueOf(timerBean.getMean()),String.valueOf(timerBean.getDuration_unit()),String.valueOf(timerBean.getExcutionTime()),String.valueOf(timerBean.getRate_unit()),String.valueOf(timerBean.getMax()),String.valueOf(timerBean.getMin()),String.valueOf(timerBean.getStddev())
	            		,String.valueOf(timerBean.getP95()),String.valueOf(timerBean.getP98()),String.valueOf(timerBean.getP99()),String.valueOf(timerBean.getP999())
	            		,String.valueOf(timerBean.getM1_rate()),String.valueOf(timerBean.getM5_rate()),String.valueOf(timerBean.getM15_rate())});*/
	            records.add(new String[]{timerBean.getApiName()
	            		,String.valueOf(timerBean.getCount()),String.valueOf(timerBean.getMean()),String.valueOf(timerBean.getDuration_unit()),String.valueOf(timerBean.getExcutionTime()),String.valueOf(timerBean.getRate_unit()),String.valueOf(timerBean.getMax()),String.valueOf(timerBean.getMin())});
	        }
	        return records;
	    }

}
