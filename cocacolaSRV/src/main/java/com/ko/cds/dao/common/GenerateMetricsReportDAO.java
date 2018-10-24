package com.ko.cds.dao.common;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Component
public interface GenerateMetricsReportDAO {

	
	public String getAvg(Long monitor_id,String stTime,String api_name,@Param("endTime") String endTime);
	public String getMin(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getMax(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getP95(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getP98(Long monitor_id,String stTime,String api_name,@Param("endTime") String endTime);
	public String getP75(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getP99(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getP999(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getMean_Rate(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getMean1_Rate(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getMean5_Rate(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getMean15_Rate(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getStdd_dev(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getCount(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getRecordUnder2Sec(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getRecordUnder4Sec(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getRecordUnderPoint2Sec(Long monitor_id,String stTime,String api_name,@Param("endTime")String endTime);
	public String getSuccessCount(Long monitor_id,String stTime,@Param("endTime")String endTime);
	public String getErrorCount(Long monitor_id,String stTime,@Param("endTime")String endTime);
	public List<HashMap<Object, Object>> getMonitoringValue();
	public List<String> getApi_Name();
}
