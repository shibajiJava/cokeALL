package com.ko.cds.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
@Component
public class EmailValidator {
   private Pattern pattern;
	
	
 
	private static final String EMAIL_BASE_PATTERN = "['_A-Za-z0-9-&+]+(\\.['_A-Za-z0-9-&]+)*[.]{0,1}@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))";
	public EmailValidator() {
		pattern = Pattern.compile("^" + EMAIL_BASE_PATTERN + "$");
	}
 
	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {
		
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
 
	}
	public static void main(String args[]){
		EmailValidator validator = new EmailValidator();
		System.out.println(validator.validate("test+1@gmail.com"));
	}

}
