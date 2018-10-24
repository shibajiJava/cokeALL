package com.ko.cds.request.admin.answer;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import com.ko.cds.pojo.question.AnswerOptionsObject;
import com.ko.cds.pojo.question.Question;



public class GetAnswerAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private BigInteger answerID;
	
	
	public BigInteger getAnswerID() {
		return answerID;
	}

	public void setAnswerID(BigInteger answerID) {
		this.answerID = answerID;
	}

	
}
