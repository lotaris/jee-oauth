package com.forbesdigital.jee.oauth.rest.api;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.*;

/**
 * Test suite for the DateHelper class.
 *
 * @author Andrei Bucin <andrei.bucin@fortech.ro>
 */
@RoxableTestClass(tags = {"dateHelper"})
public class DateHelperUnitTest {

	//<editor-fold defaultstate="collapsed" desc="Mocks">
	@InjectMocks
	private DateHelper dateHelper;
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Constants">
	private static final String DATE_FORMATTING_PATTERN = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z";
	//</editor-fold>

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test of convertToUtcString method, of class DateHelper.
	 */
	@Test
	@RoxableTest(key = "fc062b8352df", tickets = {"DCO-952"})
	public void shouldConvertDateToUtcString() {
		String date = dateHelper.convertToUtcString(new Date());
		assertNotNull(date);
		assertTrue("Date: " + date + " does not match formatting pattern.", date.matches(DATE_FORMATTING_PATTERN));
	}

}
