package com.ibm.app.services.ws.impl;

import java.net.URISyntaxException;
import java.util.List;
import javax.ws.rs.QueryParam;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import com.ibm.app.services.appcontext.ApplicationContextProvider;
import com.ibm.app.services.domain.UserData;
import com.ibm.app.services.parsistantImpl.ServiceMy;
import com.ibm.app.services.ws.AbstractWebService;






@Controller
@RequestMapping(value = "/test")
public class testWebservice extends AbstractWebService {
	
	
	static Logger LOGGER = LoggerFactory.getLogger(testWebservice.class);
	
	ApplicationContext applicationContext = null;
	
	/*@Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        System.out.println("setting context");
        this.applicationContext = applicationContext;
    }*/
	
	@RequestMapping(value= "/testWS",method = RequestMethod.POST ,consumes = "application/xml")
	
	public @ResponseBody String processReport(final NativeWebRequest nativeRequest,final @RequestBody String ParamsListStr) throws URISyntaxException
	{
		
		String returnStr= "Success";
		return returnStr;
	}
	

	@RequestMapping(value= "/testWScall",method = RequestMethod.POST ,consumes = "application/xml")
	
	public @ResponseBody String processReportcall(final NativeWebRequest nativeRequest,final @RequestBody String ParamsListStr) throws URISyntaxException
	{
		ResponseEntity<String> executionEntity = callWebserviceRequestPost(nativeRequest,"http://localhost:8080/cocacolaSRV/test/testWS", ParamsListStr, String.class);
		//String returnStr= "Success";
		return executionEntity.getBody();
	}
	
	
	
	@RequestMapping(value= "/getEmpDtl",method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<UserData>> getEmpDtl(final NativeWebRequest nativeRequest,@QueryParam(value="empId") String empid) 
	{
		LOGGER.info("-------------------------------Starting of getEmpDtl -------------------------------------");
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		List<UserData> userList = null;
		try{
			
			ServiceMy srv = (ServiceMy) ctx.getBean("service",ServiceMy.class);
		    userList =   srv.selectAllPerson();
			if(userList!=null)
			{
				LOGGER.info("List Size : "+userList.size());
			}
			else 
			{
				LOGGER.info("Unable to get data from DB ");
			}
		}
		catch(MyBatisSystemException exx)
		{
			exx.printStackTrace();
		}
		catch(Exception exx1)
		{
			exx1.printStackTrace();
		}
		
		LOGGER.info("Creating ResponseEntity for Service");
		ResponseEntity<List<UserData>> executionEntity =new ResponseEntity<List<UserData>>(userList, HttpStatus.OK);
		return executionEntity;
	}
	
	
	@RequestMapping(value= "/getEmpDtlById",method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<UserData> getEmpDtlByID(final NativeWebRequest nativeRequest,@QueryParam(value="empId") String empId) 
	{
		LOGGER.info("-------------------------------Starting of getEmpDtlByID -------------------------------------");
		LOGGER.info("Emp ID-- : "+empId);
		
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		UserData userData =null;
		try{
			
			ServiceMy srv = (ServiceMy) ctx.getBean("service",ServiceMy.class);
			userData =   srv.selectPersonById(empId);
			if(userData!=null)
			{
				LOGGER.info("List Size : "+userData.toString());
			}
			else 
			{
				LOGGER.info("Unable to get data from DB ");
			}
		}
		catch(MyBatisSystemException exx)
		{
			exx.printStackTrace();
		}
		catch(Exception exx1)
		{
			exx1.printStackTrace();
		}
		
		LOGGER.info("Creating ResponseEntity for Service");
		ResponseEntity<UserData> executionEntity =new ResponseEntity<UserData>(userData, HttpStatus.OK);
		return executionEntity;
	}
}
