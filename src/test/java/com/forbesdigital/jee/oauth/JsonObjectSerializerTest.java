package com.forbesdigital.jee.oauth;

import static org.junit.Assert.*;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.io.Serializable;
import org.junit.Test;

/**
 * Test suite for JsonObjectSerializer class.
 *
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"jsonObjectSerializer"})
public class JsonObjectSerializerTest {

	/**
	 * Test of writeValueAsString method, of class JsonObjectSerializer.
	 */
	@Test
	@RoxableTest(key = "30df459215ec")
	public void testWriteValueAsString() throws Exception {
		JsonObjectSerializer instance = new JsonObjectSerializer();
		TestClass obj = new TestClass();
		obj.setTestProperty("test");
		String result = instance.writeValueAsString(obj);

		assertEquals(result, "{\"testProperty\":\"test\"}");
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

}
