package com.ibm.app.services.parsistantImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.app.services.domain.UserData;
import com.ibm.app.services.parsistant.UserDataService;

public class ServiceMy  {

	@Autowired
	private UserDataService mapper;
	@Transactional
	public List<UserData> selectAllPerson() {
		List<UserData> ss =null;
		
		try{
			ss = mapper.getAllUser();
		}
		catch(Exception xx)
		{
			xx.printStackTrace();
		}
		catch(Throwable txx)
		{
			txx.printStackTrace();
		}
	    return ss;
	  }
	
	public UserData selectPersonById(String empID)
	{
		System.out.println("Trying to get data from DB==== for empid "+empID);
		UserData userData = mapper.getUserById(empID);
		return userData;
	}
}
