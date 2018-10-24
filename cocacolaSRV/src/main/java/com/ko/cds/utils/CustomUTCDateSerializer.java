package com.ko.cds.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom serializer to parse date in UTC format.
 * 
 * @author IBM
 * 
 */
public class CustomUTCDateSerializer extends JsonSerializer<String> {

	public static Logger logger = LoggerFactory.getLogger(CDSOUtils.class);

	@Override
	public void serialize(String dateStr, JsonGenerator jGen,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		if( !dateStr.contains(":")){
			format = new SimpleDateFormat("yyyy-MM-dd");
		}
		
		logger.info("input date : " + dateStr);

		try {
			Date date = format.parse(dateStr);
			format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String outputDate = format.format(date);
			logger.info("output date : " + outputDate);
			jGen.writeString(outputDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
