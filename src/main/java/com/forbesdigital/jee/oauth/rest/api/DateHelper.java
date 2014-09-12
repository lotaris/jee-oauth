package com.forbesdigital.jee.oauth.rest.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date helper class. Offers helper methods for managing Date objects in OAuth lib.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
class DateHelper {

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String UTC_TIMEZONE = "UTC";
	
	/**
	 * Converts a date object to a string with the following format: yyyy-MM-ddTHH:mm:ssZ .
	 * 
	 * @param date The date to be converted
	 * @return The resulting string
	 */
	public String convertToUtcString(Date date) {
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		df.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE));		
		String dateString = df.format(date);
		return dateString;
	}
	
}
