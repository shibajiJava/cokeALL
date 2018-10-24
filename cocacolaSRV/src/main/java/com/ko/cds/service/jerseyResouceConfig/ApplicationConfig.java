package com.ko.cds.service.jerseyResouceConfig;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.ko.cds.exceptions.mapper.BadRequestExceptionMapper;
import com.ko.cds.exceptions.mapper.CDSWebApplicationExceptionMapper;
import com.ko.cds.exceptions.mapper.CustomAdminExceptionMapper;
import com.ko.cds.exceptions.mapper.DataIntegrityViolationExceptionMapper;
import com.ko.cds.exceptions.mapper.IllegalArgumentExceptionHandler;
import com.ko.cds.exceptions.mapper.JsonMappingExceptionMapper;
import com.ko.cds.exceptions.mapper.JsonParseExceptionMapper;
import com.ko.cds.exceptions.mapper.RuntimeExceptionMapper;
import com.ko.cds.exceptions.mapper.UnrecognizedPropertyExceptionMapper;
import com.ko.cds.filter.mockResponse.MockResponseService;
import com.ko.cds.report.metrics.IGenerateMetricsServiceReport;
import com.ko.cds.service.activity.IActivityService;
import com.ko.cds.service.admin.answer.IAnswerAdminService;
import com.ko.cds.service.admin.question.IQuestionAdminService;
import com.ko.cds.service.admin.reason.IReasonAdminService;
import com.ko.cds.service.admin.site.ISiteAdminService;
import com.ko.cds.service.admin.survey.ISurveyAdminService;
import com.ko.cds.service.config.IConfigService;
import com.ko.cds.service.csr.ICSRService;
import com.ko.cds.service.member.IMemberService;
import com.ko.cds.service.member.IMemberServiceV2;
import com.ko.cds.service.member.TestingRest;
import com.ko.cds.service.points.IPointService;
import com.ko.cds.service.points.IPointServiceV2;
import com.ko.cds.service.question.IQuestionService;
import com.ko.cds.service.serverHealthCheck.IServerHealthCheckService;
import com.ko.cds.service.survey.ISurveyService;




public class ApplicationConfig extends ResourceConfig{
	public ApplicationConfig (){
		register(RequestContextFilter.class);
		register(TestingRest.class);
		register(IMemberService.class);
		register(ISurveyService.class); // For Survey API
		register(IQuestionService.class);
		register(IActivityService.class);
		register(JacksonFeature.class);		
		register(IllegalArgumentExceptionHandler.class);
		register(JsonParseExceptionMapper.class);
		register(CDSWebApplicationExceptionMapper.class);
		register(BadRequestExceptionMapper.class);
		register(CustomAdminExceptionMapper.class); // for self-serv api exception
		register(UnrecognizedPropertyExceptionMapper.class);
		register(JsonMappingExceptionMapper.class);
		register(RuntimeExceptionMapper.class);
		register(IPointService.class);
		register(MockResponseService.class);
		register(IConfigService.class);
		register(IServerHealthCheckService.class);
		register(DataIntegrityViolationExceptionMapper.class);
		register(ICSRService.class);
		register(IGenerateMetricsServiceReport.class);
		//register(IPointServiceV2.class);
		register(IMemberServiceV2.class);
		register(ISurveyAdminService.class);
		register(IAnswerAdminService.class);
		register(IQuestionAdminService.class);
		register(ISiteAdminService.class);
		register(IReasonAdminService.class);
		
	}
}
