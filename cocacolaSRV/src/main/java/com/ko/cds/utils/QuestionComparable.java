package com.ko.cds.utils;
import com.ko.cds.pojo.survey.Question;

import java.util.Comparator;



public class QuestionComparable implements Comparator<Question> {

	@Override
	public int compare(Question o1, Question o2) {
		// TODO Auto-generated method stub
		 return o1.getDisplayOrder()- o2.getDisplayOrder(); 
	}

	

}
