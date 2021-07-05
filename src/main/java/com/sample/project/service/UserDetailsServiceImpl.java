package com.sample.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sample.project.domain.AppUserDetails;
import com.sample.project.repository.AppUserRepository;

import static java.util.Collections.emptyList;

import java.util.Optional;

@Service("UserService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	AppUserRepository applicationUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUserDetails applicationUser = null;
		Optional<AppUserDetails> res = applicationUserRepository.findById(username);
		if( res.isPresent())
			applicationUser = res.get();
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getEmailId(), applicationUser.getPassword(), emptyList());
	}

}
