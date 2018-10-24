package com.ko.cds.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class DictionaryValueCheck extends
		AbstractAnnotationCheck<DictionaryValue> {
	private Logger log = LoggerFactory.getLogger(DictionaryValueCheck.class);
	 
    private String file;
 
    Map<String, String> dictionary;
 
    @Override
	public void configure(final DictionaryValue constraintAnnotation) {
		super.configure(constraintAnnotation);
		setFile(constraintAnnotation.file());
	}
 
    private void setFile(String file) {
        this.file = file;
        dictionary = loadDictionaryFrom(file);
        requireMessageVariablesRecreation();
    }
 
    private Map<String, String> loadDictionaryFrom(String file) {
        String fileStr;
        Map<String, String> map = new HashMap<String, String>();
        try {
        	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
            fileStr = IOUtils.toString(inputStream);
        } catch (IOException e) {
            log.error("Error loading file: ", e);
            return map;
        }
        for (String line : fileStr.split(",")) {
            if (line.trim().length() == 0)
                continue;
           /* String[] tokens = line.split(" ", 2); // we want 2 substrings max
            if (tokens.length == 2)
                map.put(tokens[0], tokens[1]);*/
            
              map.put(line, line);
        }
        return map;
    }
 

	@Override
	public boolean isSatisfied(Object validatedObject, Object valueToValidate,
			OValContext context, Validator validator) throws OValException {
		if (valueToValidate == null)
            return true;
        return dictionary.containsKey(valueToValidate);
	}

}
