package com.sample.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.sample.project.domain.TokenDetails;
import com.sample.project.repository.JWTTokenRepository;

@Service("TokenService")
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService{
	
	@Autowired
	JWTTokenRepository tokenRepo;

	@Cacheable(value = "Tokens", key = "#tokenId")
	@Override
	public TokenDetails getUser(String tokenId) {

		TokenDetails token = null;
		Optional<TokenDetails> dtls = tokenRepo.findById(tokenId);
		if(dtls.isPresent()) {
			token = dtls.get();
		}
		
		return token;
	}

	@CachePut(value = "Tokens", key = "#token.tokenId")
	@Override
	public TokenDetails updateUser(TokenDetails token) {
		
		return tokenRepo.save(token);
	}

	@CacheEvict(value = "Tokens",allEntries = true)
	@Override
	public String removeUser(String id) {
		 tokenRepo.deleteById(id);
		return "Deleted Successfully";
	}

	@Override
	public String addUser(TokenDetails token) {
		if(tokenRepo.save(token) == null)
			return null;
		
		return "Added Successfully";
	}

	@Override
	public TokenDetails getActiveUser(String subject) {
		return tokenRepo.findBySubject(subject);
	}

}
