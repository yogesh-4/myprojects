package com.sample.project.service;

import com.sample.project.domain.TokenDetails;

public interface TokenAuthenticationService {
	
	
	public TokenDetails getUser(String token);
	
	
	public TokenDetails updateUser(TokenDetails token);
	
	
	public String removeUser(String token);
	
	public String addUser(TokenDetails token);
	
	public TokenDetails getActiveUser(String subject);
	
	
	

}
