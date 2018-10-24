package com.ibm.app.services.parsistant;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.ibm.app.services.domain.UserData;

public interface UserDataService {
	
	@Select("SELECT * FROM test1.userdata")
	public List<UserData> getAllUser();
	
	@Select("SELECT * FROM test1.userdata where empid=#{empId}")
	public UserData getUserById(String empId);
}
