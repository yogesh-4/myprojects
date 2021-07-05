package com.sample.project.service;

import java.util.List;

import com.sample.project.domain.AppUserDetails;
import com.sample.project.dto.UserDto;

public interface ApplicationService {
	
	String saveUser(UserDto userDetails);

	List<UserDto> getAllUsers();
	
	 AppUserDetails findByUserName(String emailId);
	 
	 AppUserDetails checkCredentials(String emailId,String password);

}
