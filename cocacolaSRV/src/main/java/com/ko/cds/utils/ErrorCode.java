package com.ko.cds.utils;

public interface ErrorCode {
	
	public static final int STATUS_CODE_OK=200;
	public static final int STATUS_CODE_BAD_REQUEST=400;
	public static final String GEN_INVALID_ARGUMENT ="GEN_INVALID_ARGUMENT";
	public static int STATUS_CODE_INTERNAL_SERVER_ERROR= 500;
	public static final String GEN_SERVICE_NOT_AVAILABLE="GEN_SERVICE_NOT_AVAILABLE";
	public static final String INTERNAL_SERVER_ERROR="INTERNAL_SERVER_ERROR";
	public static final String NOT_MERGED_NONLITE_ACCOUNT="NOT_MERGED_NONLITE_ACCOUNT";
	
	public static final String SURVEY_NOT_FOUND="SURVEY_NOT_FOUND";  // For SURVEY_NOT_FOUND 
	public static final String QUESTION_NOT_FOUND="QUESTION_NOT_FOUND";  // For QUESTION_NOT_FOUND 
	public static final String QUESTION_NOT_FOUND_DESC = "For the question ID that was passed, there was no matching question";
	public static final String SURVEY_NOT_FOUND_DESC = "For the survey ID that was passed, there was no matching survey.";
	
	public static final String SURVEY_EXPIRED = "SURVEY_EXPIRED";
	public static final String SURVEY_EXPIRED_DESC = "The survey has expired. ";
	public static final String ACT_MAX_CREDIT_HIT="ACT_MAX_CREDIT_HIT";
	public static final String ACT_MAX_CREDIT_HIT_DESC="The credit action would have hit the maximum allowed points as defined within the configuration of the account.";
	public static final String ACT_INSUFFICIENT_POINTS="ACT_INSUFFICIENT_POINTS";
	public static final String ACT_INSUFFICIENT_POINTS_DESC="Transaction type is debit and there are not enough points to debit";
	public static final String ACT_HOLDCONFIRMATION_FAILED="ACT_HOLDCONFIRMATION_FAILED";
	public static final String ACT_HOLDCONFIRMATION_FAILED_DESC="The Confirmation of a hold did not work. Either the points rolled back already or some other transaction took place";

	public static final String ACCOUNT_NOT_FOUND="ACCOUNT_NOT_FOUND";
	public static final String ACCOUNT_NOT_FOUND_DESC="No account exists for the member ID passed in the request.";
	public static final String MEMBER_NOT_FOUND="MEMBER_NOT_FOUND";
	public static final String MEMBER_NOT_FOUND_DESC="No such member exists against the details passed in the request.";
	public static final String DATE_ERROR_MESSAGE = "Start Date cannot be greater than End Date";
	public static final String DURATION_EXCEEDED_ERROR_MESSAGE = "Search Limit Exceeded the Defined number of Days";
	public static final String UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER="UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER";
	public static final String UNIQUE_KEY_VIOLATION_FOR_UPDATE_MEMBER_DESC="SMS Number/External Id/Social Domain already exists in the database";
	public static final String GEN_INVALID_ARGUMENT_DESC="Invalid request data";
	public static final String DATA_BASE_INTERNAL_ERROR="Database Internal Error";
	public static final String SERVICE_NOT_FOUND="HTTP 404 Not Found,Service Unavailable";
	public static final String SERVICE_METHOD_NOT_ALLOWED="HTTP 405 Method Not Allowed,Service Unavailable";
	public static final String DATA_NOT_FOUND="DATA_NOT_FOUND";
	public static final String NO_HISTORY_FOUND="NO_HISTORY_FOUND";
	public static final String PARAM_ERROR="PARAM_ERROR";
	public static final String FIELD_NOT_SELECTED_FOR_SEARCH = "No fields selected for the search";
	public static final String ADVANCE_SEARCH_MEMBER_NOT_FOUND_DESC = "Member not found";
	public static final String ADV_SEARCH_WILD_LENTH_LIMIT="Wildcard search with too few characters";
	public static final String ADV_SEARC_LOGICAL_EXCLUSIVE_SET_ERROR_DESC = "The search does not work with the combination of params in request";
	public static final String MEMBER_EXISTS = "Memeber with same UUID already exists, try Creating Member with another unique UUID";
	public static final String REEDEMTION_LOT_CAMPAING_ERROR = "CampaignID , LotId, they both canot be null or exists together";
	public static final String DUPLICATE_SESSION_UUID_FOUND="DUPLICATE_SESSION_UUID_FOUND";
	
	public static final String TRANSACTION_NOT_ALLOWED="TRANSACTION_NOT_ALLOWED";
	public static final String TRANSACTION_NOT_ALLOWED_BY_MEMBER_ERROR="Member is not allowed to proceed for points transaction, He/She is either not present or not in Active state in databse.";
	
	public static final String RECORD_ALREADY_EXISTS = "RECORD_ALREADY_EXISTS";
	public static final String RECORD_ALREADY_EXISTS_DESC = "Duplicate Record";
	public static final String REFERENCE_NOT_FOUND = "REFERENCE_NOT_FOUND";
	public static final String REFERENCE_NOT_FOUND_DESC = "Related Data not Found";
	public static final String ANSWER_NOT_FOUND = "ANSWER_NOT_FOUND";
	public static final String ANSWER_NOT_FOUND_DESC = "No answer found for the given Id";
	public static final String SITE_NOT_FOUND = "SITE_NOT_FOUND";
	public static final String SITE_NOT_FOUND_DESC = "No site found for the given Id";
	public static final String SQL_CODE_PK_VIOLATION = "SQLCODE=-803";
	public static final String SQL_STATE_PK_VIOLATION = "SQLSTATE=23505";
	public static final String SQL_CODE_FK_VIOLATION = "SQLCODE=-530";
	public static final String SQL_STATE_FK_VIOLATION = "SQLSTATE=23503";
	public static final String UNEXPECTED_DB_ERROR = "UNEXPECTED_DB_ERROR";
	public static final String UNEXPECTED_DB_ERROR_DESC = "Exception at DB end";
	
}
