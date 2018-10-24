package com.ko.cds.response.csr;

import java.util.List;

import com.ko.cds.pojo.csr.ActivitySearch;

/**
 * Pojo for capturing Response  for the Activity Search  
 * @author ibm
 *
 */
public class ActivitySearchResponse {
	
	private List<ActivitySearch> activityObjects;
	private String maximumRecords;
	
	
	public List<ActivitySearch> getActivityObjects() {
		return activityObjects;
	}
	public void setActivityObjects(List<ActivitySearch> activityObjects) {
		this.activityObjects = activityObjects;
	}
	public String getMaximumRecords() {
		return maximumRecords;
	}
	public void setMaximumRecords(String maximumRecords) {
		this.maximumRecords = maximumRecords;
	}
	@Override
	public String toString() {
		return "ActivitySearchResponse [activityObjects=" + activityObjects
				+ ", maximumRecords=" + maximumRecords + "]";
	}
	
	

}
