package com.ko.cds.service.admin.site;

import java.math.BigInteger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.customAnnotation.AopServiceAnnotation;
import com.ko.cds.dao.admin.survey.SurveyAdminDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.exceptions.CustomAdminException;
import com.ko.cds.request.admin.site.GetSiteAdminRequest;
import com.ko.cds.request.admin.site.PostSiteAdminRequest;
import com.ko.cds.request.admin.site.UpdateSiteAdminRequest;
import com.ko.cds.response.admin.site.GetSiteAdminResponseList;
import com.ko.cds.response.admin.site.PostSiteAdminResponse;
import com.ko.cds.response.admin.site.UpdateSiteAdminResponse;
import com.ko.cds.service.helper.SurveyServiceAdminHelper;
import com.ko.cds.utils.CDSOUtils;

@Component
@Transactional
public class SiteAdminService implements ISiteAdminService {
	
	@Autowired
	private SurveyAdminDAO surveyDAO;

	@Autowired
	private SurveyServiceAdminHelper serviceHelper;

	
	

    @Transactional(rollbackFor={BadRequestException.class,RuntimeException.class,WebApplicationException.class})
	@AopServiceAnnotation
	@Override
	public Response postSiteAdmin(PostSiteAdminRequest postSiteAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
    	PostSiteAdminResponse siteIdResponse = new PostSiteAdminResponse();
		BigInteger siteId = serviceHelper.postSiteAdmin(postSiteAdminRequest);
		siteIdResponse.setSiteId(siteId);
		return CDSOUtils.createOKResponse(siteIdResponse);
	}
	
	@Override
	public Response getSiteAdmin(GetSiteAdminRequest getSiteAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		GetSiteAdminResponseList resp =new GetSiteAdminResponseList();
		
		resp = serviceHelper.getSiteAdmin(getSiteAdminRequest);
        
		if (resp == null) {
			// Empty JSON Object - to create {} as notation for empty resultset
			JSONObject obj = new JSONObject();			
			return CDSOUtils.createOKResponse(obj.toString());
		} else {
			return CDSOUtils.createOKResponse(resp);
		}
		
	}
	@Override
	public Response updateSiteAdmin(UpdateSiteAdminRequest updateSiteAdminRequest)
			throws CustomAdminException {
		// TODO Auto-generated method stub
		UpdateSiteAdminResponse resp =new UpdateSiteAdminResponse();
		int siteId = serviceHelper.updateSiteAdmin(updateSiteAdminRequest);
		resp.setSiteId(siteId);
		return CDSOUtils.createOKResponse(resp);
		
	}
	
}
