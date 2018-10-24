package com.ko.cds.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.config.ReasonCode;

@Component
public interface ConfigDAO {

	public List<ReasonCode> getReasonCodes();
	
}
