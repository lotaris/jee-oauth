package com.forbesdigital.jee.oauth.rest.api;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @see DateHelper
 * @author Andrei Bucin <andrei.bucin@fortech.ro>
 */
@RoxableTestClass(tags = {"dateHelper"})
public class DateHelperUnitTest {

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String UTC_TIMEZONE = "UTC";
	//</editor-fold>

	private DateHelper dateHelper;

	@Before
	public void setUp() {
		dateHelper = new DateHelper();
	}

	@Test
	@RoxableTest(key = "fb5c39b50cfc")
	public void shouldConvertDateToUtcString() throws ParseException {
		String initialDate = "2014-01-27T13:29:15Z";

		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		df.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE));
		Date date = df.parse(initialDate);

		String formattedDate = dateHelper.convertToUtcString(date);

		assertNotNull("The generated date should not be null.", formattedDate);
		assertEquals("The returned formatedDate should be the same as the initialDate", initialDate, formattedDate);
	}

}
