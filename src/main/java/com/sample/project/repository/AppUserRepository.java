package com.sample.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.sample.project.domain.AppUserDetails;

//@Repository("AppUserRepository")
@Component("AppUserRepository")
public interface AppUserRepository  extends CrudRepository<AppUserDetails, String>{
	
	
	AppUserDetails findByEmailIdAndPassword(String emailId,String password);
	
}
