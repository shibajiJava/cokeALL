package com.ko.cds.request.survey;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.QueryParam;

import net.sf.oval.constraint.AssertFieldConstraints;
import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Size;

import org.codehaus.jackson.annotate.JsonProperty;

import com.ko.cds.pojo.survey.OptPreference;
import com.ko.cds.utils.CDSConstants;

/**
 * Request class for Opts API (postOpts & getOpts).
 * 
 * @author IBM
 * 
 */

public class OptsRequest implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty()
	@QueryParam("memberId")
	@NotNull(profiles = { CDSConstants.POSTOPTS_PROFILE_NAME,
			CDSConstants.GETOPTS_PROFILE_NAME }, message = "opts.insert.violation")
	private BigInteger memberId;
	@QueryParam("sessionUUID")
	@MaxLength(value = 36, profiles = { CDSConstants.POSTOPTS_PROFILE_NAME,
			CDSConstants.GETOPTS_PROFILE_NAME }, message = "opts.insert.violation")
	private String sessionUUID;
	@AssertValid(profiles = { CDSConstants.POSTOPTS_PROFILE_NAME }, message = "opts.insert.violation")
	@NotNull(profiles = { CDSConstants.POSTOPTS_PROFILE_NAME }, message = "opts.insert.violation")
	@Size(min = 1, profiles = { CDSConstants.POSTOPTS_PROFILE_NAME }, message = "opts.insert.violation")
	private List<OptPreference> opts;

	public BigInteger getMemberId() {
		return memberId;
	}

	public void setMemberId(@AssertFieldConstraints BigInteger memberId) {
		this.memberId = memberId;
	}

	public String getSessionUUID() {
		return sessionUUID;
	}

	public void setSessionUUID(String sessionUUID) {
		this.sessionUUID = sessionUUID;
	}

	public List<OptPreference> getOpts() {
		return opts;
	}

	public void setOpts(List<OptPreference> opts) {
		this.opts = opts;
	}

}
