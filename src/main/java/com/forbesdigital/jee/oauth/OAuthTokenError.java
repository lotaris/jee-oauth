package com.forbesdigital.jee.oauth;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Transfer object representing an OAuth error returned when requesting and Access Token.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
@JsonPropertyOrder({"error", "error_description"})
public class OAuthTokenError {
	
	private String error;
	
	@JsonIgnore
	private String errorDescription;

	//<editor-fold defaultstate="collapsed" desc="Constructor">
	public OAuthTokenError(String error, String errorDescription) {
		this.error = error;
		this.errorDescription = errorDescription;
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@JsonProperty("error_description")
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	//</editor-fold>
}
