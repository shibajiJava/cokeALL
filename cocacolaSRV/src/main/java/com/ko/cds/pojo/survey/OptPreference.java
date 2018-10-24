package com.ko.cds.pojo.survey;

import java.io.Serializable;

import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.ko.cds.utils.CDSConstants;
import com.ko.cds.utils.CustomTimestampDeserializer;
import com.ko.cds.utils.CustomUTCDateSerializer;

/**
 * Opt Preference Object for Opts API.
 * @author IBM
 *
 */
public class OptPreference implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")	
	@MaxLength(value = 250, profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	private String communicationTypeName;
//	@NotNull(profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	@JsonDeserialize(using = CustomTimestampDeserializer.class)
	@JsonSerialize(using = CustomUTCDateSerializer.class)
	private String acceptedDate;
	@MaxLength(value = 20, profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	private String type;
	@MaxLength(value = 10, profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	private String schedule;
	@MaxLength(value = 10, profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	private String format;
	@MaxLength(value = 10, profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	@MatchPattern(pattern = "true|false", profiles = {CDSConstants.POSTOPTS_PROFILE_NAME}, message="opts.insert.violation")
	private String optInIndicator;
//	private String clientId;

	public String getCommunicationTypeName() {
		return communicationTypeName;
	}

	public void setCommunicationTypeName(String communicationTypeName) {
		this.communicationTypeName = communicationTypeName;
	}

	public String getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(String acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getOptInIndicator() {
		return optInIndicator;
	}

	public void setOptInIndicator(String optInIndicator) {
		this.optInIndicator = optInIndicator;
	}

//	public String getClientId() {
//		return clientId;
//	}
//
//	public void setClientId(String clientId) {
//		this.clientId = clientId;
//	}

}
