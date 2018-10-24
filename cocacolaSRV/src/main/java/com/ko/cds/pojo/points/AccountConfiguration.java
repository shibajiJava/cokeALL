package com.ko.cds.pojo.points;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(value = {"level_1_limit","level_2_limit","level_3_limit","level_4_limit","level_5_limit","level_1_name","level_2_name","level_3_name","level_4_name","level_5_name"})
public class AccountConfiguration {

	private static final long serialVersionUID = 1990L;
	
	private String pointAccountType;
	private String pointAccountTypeName;
	
	private String level_1_name;
	private String level_1_limit;
	
	private String level_2_name;
	private String level_2_limit;
	
	private String level_3_name;
	private String level_3_limit;
	
	private String level_4_name;
	private String level_4_limit;
	
	private String level_5_name;
	private String level_5_limit;
	
	private List<StatusLevels> statusLevels;
	
	
	public String getPointAccountType() {
		return pointAccountType;
	}
	public void setPointAccountType(String pointAccountType) {
		this.pointAccountType = pointAccountType;
	}
	public String getPointAccountTypeName() {
		return pointAccountTypeName;
	}
	public void setPointAccountTypeName(String pointAccountTypeName) {
		this.pointAccountTypeName = pointAccountTypeName;
	}
	public List<StatusLevels> getStatusLevels() {
		return statusLevels;
	}
	public void setStatusLevels(List<StatusLevels> statusLevels) {
		this.statusLevels = statusLevels;
	}
	
	public String getLevel_1_name() {
		return level_1_name;
	}
	public void setLevel_1_name(String level_1_name) {
		this.level_1_name = level_1_name;
	}
	public String getLevel_1_limit() {
		return level_1_limit;
	}
	public void setLevel_1_limit(String level_1_limit) {
		this.level_1_limit = level_1_limit;
	}
	public String getLevel_2_name() {
		return level_2_name;
	}
	public void setLevel_2_name(String level_2_name) {
		this.level_2_name = level_2_name;
	}
	public String getLevel_2_limit() {
		return level_2_limit;
	}
	public void setLevel_2_limit(String level_2_limit) {
		this.level_2_limit = level_2_limit;
	}
	public String getLevel_3_name() {
		return level_3_name;
	}
	public void setLevel_3_name(String level_3_name) {
		this.level_3_name = level_3_name;
	}
	public String getLevel_3_limit() {
		return level_3_limit;
	}
	public void setLevel_3_limit(String level_3_limit) {
		this.level_3_limit = level_3_limit;
	}
	public String getLevel_4_name() {
		return level_4_name;
	}
	public void setLevel_4_name(String level_4_name) {
		this.level_4_name = level_4_name;
	}
	public String getLevel_4_limit() {
		return level_4_limit;
	}
	public void setLevel_4_limit(String level_4_limit) {
		this.level_4_limit = level_4_limit;
	}
	public String getLevel_5_name() {
		return level_5_name;
	}
	public void setLevel_5_name(String level_5_name) {
		this.level_5_name = level_5_name;
	}
	public String getLevel_5_limit() {
		return level_5_limit;
	}
	public void setLevel_5_limit(String level_5_limit) {
		this.level_5_limit = level_5_limit;
	}
	@Override
	public String toString() {
		return "AccountConfiguration [pointAccountType=" + pointAccountType
				+ ", pointAccountTypeName=" + pointAccountTypeName
				+ ", level_1_name=" + level_1_name + ", level_1_limit="
				+ level_1_limit + ", level_2_name=" + level_2_name
				+ ", level_2_limit=" + level_2_limit + ", level_3_name="
				+ level_3_name + ", level_3_limit=" + level_3_limit
				+ ", level_4_name=" + level_4_name + ", level_4_limit="
				+ level_4_limit + ", level_5_name=" + level_5_name
				+ ", level_5_limit=" + level_5_limit + ", statusLevels="
				+ statusLevels + "]";
	}
	
}
