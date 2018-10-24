package com.ko.cds.utils;

import java.util.Comparator;

import com.ko.cds.pojo.survey.Answer;


public class AnswerComparable implements Comparator<Answer> {

	@Override
	public int compare(Answer o1, Answer o2) {
		// TODO Auto-generated method stub
		return o1.getAnswerOrder()-o2.getAnswerOrder();
	}

	

}
