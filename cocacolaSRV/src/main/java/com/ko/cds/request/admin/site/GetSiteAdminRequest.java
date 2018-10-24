package com.ko.cds.request.admin.site;

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



public class GetSiteAdminRequest implements Serializable{

	/**
	 * @author IBM
	 * API Name: PostSurvey
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int siteId;


	public int getSiteId() {
		return siteId;
	}


	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	
	
	
	
}
