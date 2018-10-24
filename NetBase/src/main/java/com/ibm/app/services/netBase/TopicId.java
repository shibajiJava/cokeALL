package com.ibm.app.services.netBase;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@JsonIgnoreProperties(value = { "contentType","createDate","description","displayImgUrl","editDate","fhTwitterStatus","fromDate","languageFilters","name","owner","status","timeInterval","toDate" })
public class TopicId {


	private String topicId;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	
}
