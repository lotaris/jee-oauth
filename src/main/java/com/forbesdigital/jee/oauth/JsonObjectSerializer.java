package com.forbesdigital.jee.oauth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Configuration of Jackson serialization.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
public class JsonObjectSerializer {

	private ObjectMapper jacksonObjectSerializer;

	public JsonObjectSerializer() {

		jacksonObjectSerializer = new ObjectMapper();

		// do not serialize null values
		jacksonObjectSerializer.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

		/*
		 * Do not wrap objects in a single-property JSON object with the root
		 * name. The result is:
		 *
		 * { name: 'my app' }
		 *
		 * Instead of:
		 *
		 * { application: { name: 'my app' } }
		 */
		jacksonObjectSerializer.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		jacksonObjectSerializer.setDateFormat(dateFormat);
	}

	/**
	 * Method that can be used to serialize any Java value as a String.
	 * 
	 * @param object The object to be serialized
	 * @return The serializes Object as String.
	 * @throws IOException In case the serialization fails.
	 */
	public String writeValueAsString(Object object) throws IOException {
		return jacksonObjectSerializer.writeValueAsString(object);
	}
	
}
