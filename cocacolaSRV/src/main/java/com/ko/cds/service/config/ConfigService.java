package com.ko.cds.service.config;

import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.config.ReasonCode;
import com.ko.cds.response.config.GetReasonCodeResponse;
import com.ko.cds.service.helper.ConfigServiceHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class ConfigService implements IConfigService {
	
	@Autowired
	ConfigServiceHelper serviceHelper ;
	
	
	@Transactional
	@AopServiceAnnotation
	@Override
	public Response getReasonCodes() throws BadRequestException {
		List<ReasonCode> reasonCodesList= serviceHelper.getReasonCodes();
		 if (reasonCodesList == null) {
				// Empty JSON Object - to create {} as notation for empty resultset
				JSONObject obj = new JSONObject();			
				return CDSOUtils.createOKResponse(obj.toString());
			} else {
				GetReasonCodeResponse response = new GetReasonCodeResponse();
				response.setReasonCodes(reasonCodesList);
				return CDSOUtils.createOKResponse(response);
			}
	}

}
