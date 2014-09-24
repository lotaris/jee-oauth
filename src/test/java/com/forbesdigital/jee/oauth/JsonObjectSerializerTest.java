package com.forbesdigital.jee.oauth;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @see JsonObjectSerializer
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"jsonObjectSerializer"})
public class JsonObjectSerializerTest {

	@Test
	@RoxableTest(key = "30df459215ec")
	public void writeValueAsStringWithSuccessForSimpleObject() throws Exception {
		JsonObjectSerializer instance = new JsonObjectSerializer();
		TestClass obj = new TestClass();
		obj.setTestProperty("test");
		String result = instance.writeValueAsString(obj);

		assertEquals(result, "{\"testProperty\":\"test\"}");
	}

	@Test
	@RoxableTest(key = "a729302ad399")
	public void writeValueAsStringWithSuccessForSimpleObjectWithDateProperty() throws Exception {
		Calendar date = Calendar.getInstance();
		date.set(2014, Calendar.DECEMBER, 20, 0, 0, 0);
		int timezoneOffset = (date.getTimeZone().getOffset(date.getTime().getTime()) / 1000 / 60 / 60 * 100);
		JsonObjectSerializer instance = new JsonObjectSerializer();
		TestClassWithDateProperty obj = new TestClassWithDateProperty();
		obj.setTestProperty("test");
		obj.setDate(date.getTime());
		String result = instance.writeValueAsString(obj);
		assertEquals(result, String.format("{\"testProperty\":\"test\",\"date\":\"2014-12-20T00:00:00+0%s\"}", timezoneOffset));
	}

	@Test
	@RoxableTest(key = "9c60763dafb1")
	public void writeValueAsStringWithSuccessForSimpleObjectWithOneNullProperty() throws Exception {
		JsonObjectSerializer instance = new JsonObjectSerializer();
		TestClassWithDateProperty obj = new TestClassWithDateProperty();
		obj.setTestProperty("test");
		obj.setDate(null);
		String result = instance.writeValueAsString(obj);
		assertEquals(result, String.format("{\"testProperty\":\"test\"}"));
	}

	public static class TestClass implements Serializable {

		private String testProperty;

		//<editor-fold defaultstate="collapsed" desc="Constructors">
		public TestClass() {
		}
		//</editor-fold>

		//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
		public String getTestProperty() {
			return testProperty;
		}

		public void setTestProperty(String testProperty) {
			this.testProperty = testProperty;
		}
		//</editor-fold>

	}

	public static class TestClassWithDateProperty implements Serializable {

		private String testProperty;
		private Date date;

		//<editor-fold defaultstate="collapsed" desc="Constructors">
		public TestClassWithDateProperty() {
		}
		//</editor-fold>

		//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
		public String getTestProperty() {
			return testProperty;
		}

		public void setTestProperty(String testProperty) {
			this.testProperty = testProperty;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
		//</editor-fold>

	}

}
