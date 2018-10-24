package com.ko.cds.request.admin.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;



import java.util.List;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import net.sf.oval.constraint.Length;

import com.ko.cds.pojo.admin.survey.QuestionIdAdmin;
import com.ko.cds.utils.CustomUTCDateSerializer;



public class QuestionIdS implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurveyAdmin
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

    
    
    private BigInteger QuestionIds;


    @JsonProperty("QuestionIds")
	public BigInteger getQuestionIds() {
		return QuestionIds;
	}



	public void setQuestionIds(BigInteger questionIds) {
		QuestionIds = questionIds;
	}
    


	

	
	

}
