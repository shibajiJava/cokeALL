package com.ko.cds.response.survey;

import java.io.Serializable;
import java.util.List;

import com.ko.cds.pojo.survey.OptPreference;

/**
 * GetOpts Response object.
 * @author IBM
 *
 */
public class GetOptsResponse implements Serializable {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private List<OptPreference> opts;

	public List<OptPreference> getOpts() {
		return opts;
	}

	public void setOpts(List<OptPreference> opts) {
		this.opts = opts;
	}

}
