package com.ko.cds.utils;

public interface CSRConstants {

	 public static final String POINT_REDEMPTION="Points";
	 public static final String CHALLENGE_CODE="No points redemption";
	 public static final String NO_FIELD_SELECTION_ERROR="No fields selected for the search";
	 public static final String DATE_SELECTION_ERROR = "No end date when a there is a start date or no start date where there is an end date ";
	 public static final String MAX_RECORDS="MAXIMUM";
	 public static final String MEMBER_NOT_EXISTS = "Member does not exist";
	 public static final String MEMBER_SCHEDULE_FOR_DELETE = "Member already scheduled to be deleted";
	 public static final int MAX_RECORDS_FOR_POINTS = 300;
	 public static final int MAX_RECORDS_FOR_API = 1000;
	 public static final String TAG_SEARCH_LIMIT_DESC = "Only one Tag can be searched at a time during the search";
	 public static final String TAG_OBJECT_ERROR = "tagObject should contain a name and value pair tagobject";
	 public static final String TAG_OBJECT_NAME_ERROR = "tagObject should contain at least Name object for Search";
}
