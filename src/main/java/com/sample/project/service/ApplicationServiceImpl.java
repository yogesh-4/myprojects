package com.sample.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.project.common.ApplicationMapper;
import com.sample.project.domain.AppUserDetails;
import com.sample.project.dto.UserDto;
import com.sample.project.repository.AppUserRepository;

@Service("ApplicationService")
public class ApplicationServiceImpl implements ApplicationService{
	
	@Autowired
	AppUserRepository userRepo;
	
	@Override
	public String saveUser(UserDto userDetails) {
		AppUserDetails user = new AppUserDetails();
		user.setEmailId(userDetails.getEmailId());
		user.setPassword(userDetails.getPassword());
		userRepo.save(user);
		return "Success";
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<AppUserDetails> userList = new ArrayList<>();
		userRepo.findAll().forEach(userList::add);
		List<UserDto> userDtoList = new ArrayList<>();
		for(AppUserDetails user:userList) {
			UserDto userDto = new UserDto();
			userDto.setEmailId(user.getEmailId());
			userDto.setPassword(user.getPassword());
			userDtoList.add(userDto);
		}
		//return ApplicationMapper.INSTANCE.mapUserDomainToDto(userRepo.findAll());
		return userDtoList;
	}
	
	@Override
	public AppUserDetails findByUserName(String emailId) {
		AppUserDetails userDtls = null;
		Optional<AppUserDetails> res = userRepo.findById(emailId);
		if(res.isPresent()) {
			userDtls = res.get();
		}
		
		return userDtls;
	}
	
	@Override
	public AppUserDetails checkCredentials(String emailId,String password) {
		
		
		return userRepo.findByEmailIdAndPassword(emailId, password);
	}

}
