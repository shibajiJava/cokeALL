package com.ko.cds.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDateDeserializer extends JsonDeserializer<String>{
	public static Logger logger = LoggerFactory.getLogger(CustomDateDeserializer.class);
	@Override
	public String deserialize(JsonParser jsonparser,DeserializationContext deserializationcontext)
			throws IOException, JsonProcessingException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");		
		String date = jsonparser.getText();
		logger.info("input date : "+date);
		try {
			Date date1 = format.parse(date);
			//format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			String outputDate = format.format(date1).toString();
			logger.info("output date : "+outputDate);
			return outputDate;
		} catch (Exception e) {
			throw new JsonParseException(e.getMessage(),jsonparser.getCurrentLocation(),e.getCause());
		}
	}

}
