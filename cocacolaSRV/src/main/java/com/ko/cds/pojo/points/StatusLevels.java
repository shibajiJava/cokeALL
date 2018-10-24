package com.ko.cds.pojo.points;

public class StatusLevels {
	
	private static final long serialVersionUID = 100L;
	
	private String statusLevelOrder;
	private String statusLevelLimit;
	private String statusLevelName;
	
	public String getStatusLevelOrder() {
		return statusLevelOrder;
	}
	public void setStatusLevelOrder(String statusLevelOrder) {
		this.statusLevelOrder = statusLevelOrder;
	}
	public String getStatusLevelLimit() {
		return statusLevelLimit;
	}
	public void setStatusLevelLimit(String statusLevelLimit) {
		this.statusLevelLimit = statusLevelLimit;
	}
	public String getStatusLevelName() {
		return statusLevelName;
	}
	public void setStatusLevelName(String statusLevelName) {
		this.statusLevelName = statusLevelName;
	}
	@Override
	public String toString() {
		return "StatusLevels [statusLevelOrder=" + statusLevelOrder
				+ ", statusLevelLimit=" + statusLevelLimit
				+ ", statusLevelName=" + statusLevelName + "]";
	}
	
	

}
