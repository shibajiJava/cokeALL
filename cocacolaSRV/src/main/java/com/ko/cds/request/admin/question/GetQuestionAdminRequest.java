package com.ko.cds.request.admin.question;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.annotate.JsonProperty;

import net.sf.oval.constraint.AssertFieldConstraints;
import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.exclusion.Nullable;

import com.ko.cds.pojo.question.AnswerOptionsObject;
import com.ko.cds.pojo.question.Question;



public class GetQuestionAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty()
	@QueryParam("questionID")
	private int questionID;

	
	public int getQuestionID() {
		
		return questionID;
	}


	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}


	
	
	
	

	
}
