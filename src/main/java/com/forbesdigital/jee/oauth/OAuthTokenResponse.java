package com.forbesdigital.jee.oauth;

import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

/**
 * Transfer object representing an OAuth Token response.
 *
 * @author Cristian Calugar <cristian.calugar@fortech.ro>
 */
@JsonPropertyOrder({"access_token", "token_type", "expires_in", "expiration_date", "scope"})
public class OAuthTokenResponse {
	
	@JsonIgnore
	private String accessToken;
	@JsonIgnore
	private String tokenType;
	@JsonIgnore
	private Integer expiresIn;
	private String scope;
	@JsonIgnore
	private String expirationDate;
	@JsonIgnore
	private Map<String, Object> additionalInformation = new LinkedHashMap<>();

	//<editor-fold defaultstate="collapsed" desc="Constructor">
	public OAuthTokenResponse() {
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
	@JsonProperty("access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonProperty("token_type")
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@JsonProperty("expires_in")
	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@JsonProperty("expiration_date")
	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(Map<String, Object> additionalInformation) {
		this.additionalInformation = new LinkedHashMap<>(additionalInformation);
	}
	//</editor-fold>

}
