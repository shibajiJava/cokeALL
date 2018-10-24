package com.ibm.ko.cds.pojo.metrics;

/*
 * This bean will capture the information we get in timer/csv file  into a this bean object.
 */
public class CsvToTimerBean {
	//t,count,max,mean,min,stddev,p50,p75,p95,p98,p99,p999,mean_rate,m1_rate,m5_rate,m15_rate,rate_unit,duration_unit
	
	String p95;
	String p98;
	String stddev;
	String count;
	String m1_rate;
	String t;
	String p50;
	String max;
	String min;
	
	String p75;
	String p99;
	String p999;
	String mean_rate;
	String m5_rate;
	String m15_rate;
	String rate_unit;
	String duration_unit;
	String mean;
	String apiName;
	String requestStTime;
	String requestEndTime;
	String excutionTime;
	String requestStatus;
	
	
	public CsvToTimerBean() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the requestStatus
	 */
	public String getRequestStatus() {
		return requestStatus;
	}

	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	

	public CsvToTimerBean(String p95, String p98, String stddev, String count,
			String m1_rate, String t, String p50, String max, String min, String p75,
			String p99, String p999, String mean_rate, String m5_rate,
			String m15_rate, String rate_unit, String duration_unit,
			String mean, String apiName, String requestStTime,
			String requestEndTime, String excutionTime,String requestStatus) {
		super();
		this.p95 = p95;
		this.p98 = p98;
		this.stddev = stddev;
		this.count = count;
		this.m1_rate = m1_rate;
		this.t = t;
		this.p50 = p50;
		this.max = max;
		this.min = min;
		this.p75 = p75;
		this.p99 = p99;
		this.p999 = p999;
		this.mean_rate = mean_rate;
		this.m5_rate = m5_rate;
		this.m15_rate = m15_rate;
		this.rate_unit = rate_unit;
		this.duration_unit = duration_unit;
		this.mean = mean;
		this.apiName = apiName;
		this.requestStTime = requestStTime;
		this.requestEndTime = requestEndTime;
		this.excutionTime = excutionTime;
		this.requestStatus = requestStatus;
	}

	/**
	 * @return the p95
	 */
	public String getP95() {
		return p95;
	}

	/**
	 * @param p95 the p95 to set
	 */
	public void setP95(String p95) {
		this.p95 = p95;
	}

	/**
	 * @return the p98
	 */
	public String getP98() {
		return p98;
	}

	/**
	 * @param p98 the p98 to set
	 */
	public void setP98(String p98) {
		this.p98 = p98;
	}

	/**
	 * @return the stddev
	 */
	public String getStddev() {
		return stddev;
	}

	/**
	 * @param stddev the stddev to set
	 */
	public void setStddev(String stddev) {
		this.stddev = stddev;
	}

	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * @return the m1_rate
	 */
	public String getM1_rate() {
		return m1_rate;
	}

	/**
	 * @param m1_rate the m1_rate to set
	 */
	public void setM1_rate(String m1_rate) {
		this.m1_rate = m1_rate;
	}

	/**
	 * @return the t
	 */
	public String getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(String t) {
		this.t = t;
	}

	/**
	 * @return the p50
	 */
	public String getP50() {
		return p50;
	}

	/**
	 * @param p50 the p50 to set
	 */
	public void setP50(String p50) {
		this.p50 = p50;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the p75
	 */
	public String getP75() {
		return p75;
	}

	/**
	 * @param p75 the p75 to set
	 */
	public void setP75(String p75) {
		this.p75 = p75;
	}

	/**
	 * @return the p99
	 */
	public String getP99() {
		return p99;
	}

	/**
	 * @param p99 the p99 to set
	 */
	public void setP99(String p99) {
		this.p99 = p99;
	}

	/**
	 * @return the p999
	 */
	public String getP999() {
		return p999;
	}

	/**
	 * @param p999 the p999 to set
	 */
	public void setP999(String p999) {
		this.p999 = p999;
	}

	/**
	 * @return the mean_rate
	 */
	public String getMean_rate() {
		return mean_rate;
	}

	/**
	 * @param mean_rate the mean_rate to set
	 */
	public void setMean_rate(String mean_rate) {
		this.mean_rate = mean_rate;
	}

	/**
	 * @return the m5_rate
	 */
	public String getM5_rate() {
		return m5_rate;
	}

	/**
	 * @param m5_rate the m5_rate to set
	 */
	public void setM5_rate(String m5_rate) {
		this.m5_rate = m5_rate;
	}

	/**
	 * @return the m15_rate
	 */
	public String getM15_rate() {
		return m15_rate;
	}

	/**
	 * @param m15_rate the m15_rate to set
	 */
	public void setM15_rate(String m15_rate) {
		this.m15_rate = m15_rate;
	}

	/**
	 * @return the rate_unit
	 */
	public String getRate_unit() {
		return rate_unit;
	}

	/**
	 * @param rate_unit the rate_unit to set
	 */
	public void setRate_unit(String rate_unit) {
		this.rate_unit = rate_unit;
	}

	/**
	 * @return the duration_unit
	 */
	public String getDuration_unit() {
		return duration_unit;
	}

	/**
	 * @param duration_unit the duration_unit to set
	 */
	public void setDuration_unit(String duration_unit) {
		this.duration_unit = duration_unit;
	}

	/**
	 * @return the mean
	 */
	public String getMean() {
		return mean;
	}

	/**
	 * @param mean the mean to set
	 */
	public void setMean(String mean) {
		this.mean = mean;
	}

	/**
	 * @return the apiName
	 */
	public String getApiName() {
		return apiName;
	}

	/**
	 * @param apiName the apiName to set
	 */
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	/**
	 * @return the requestStTime
	 */
	public String getRequestStTime() {
		return requestStTime;
	}

	/**
	 * @param requestStTime the requestStTime to set
	 */
	public void setRequestStTime(String requestStTime) {
		this.requestStTime = requestStTime;
	}

	/**
	 * @return the requestEndTime
	 */
	public String getRequestEndTime() {
		return requestEndTime;
	}

	/**
	 * @param requestEndTime the requestEndTime to set
	 */
	public void setRequestEndTime(String requestEndTime) {
		this.requestEndTime = requestEndTime;
	}

	/**
	 * @return the excutionTime
	 */
	public String getExcutionTime() {
		return excutionTime;
	}

	/**
	 * @param excutionTime the excutionTime to set
	 */
	public void setExcutionTime(String excutionTime) {
		this.excutionTime = excutionTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimerBean [p95=" + p95 + ", p98=" + p98 + ", stddev=" + stddev
				+ ", count=" + count + ", m1_rate=" + m1_rate + ", t=" + t
				+ ", p50=" + p50 + ", max=" + max + ", min=" + min + ", p75="
				+ p75 + ", p99=" + p99 + ", p999=" + p999 + ", mean_rate="
				+ mean_rate + ", m5_rate=" + m5_rate + ", m15_rate=" + m15_rate
				+ ", rate_unit=" + rate_unit + ", duration_unit="
				+ duration_unit + ", mean=" + mean + ", apiName=" + apiName
				+ ", requestStTime=" + requestStTime + ", requestEndTime="
				+ requestEndTime + ", excutionTime=" + excutionTime + ", requestStatus= "+ requestStatus +"]";
	}
	
	
}
