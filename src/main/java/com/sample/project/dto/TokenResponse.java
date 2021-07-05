package com.sample.project.dto;

import java.io.Serializable;

public class TokenResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3731609324934597080L;

	private String statusCode;

	private String statusMessage;

	private String token;

	private String user;

	private String expirationTime;

	private String issuedTime;

	private String tokenType;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(String issuedTime) {
		this.issuedTime = issuedTime;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public TokenResponse(String statusCode, String statusMessage, String token, String user, String expirationTime,
			String issuedTime, String tokenType) {
		super();
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.token = token;
		this.user = user;
		this.expirationTime = expirationTime;
		this.issuedTime = issuedTime;
		this.tokenType = tokenType;
	}

	public TokenResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TokenResponse(String statusCode, String statusMessage) {
		super();
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}

}
