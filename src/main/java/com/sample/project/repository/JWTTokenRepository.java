package com.sample.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.sample.project.domain.TokenDetails;

//@Repository
@Component("JWTTokenRepository")
public interface JWTTokenRepository extends CrudRepository<TokenDetails, String> {
	
	
	TokenDetails findByTokenId(String tokenId);
	
	TokenDetails findBySubject(String subject);
	
	

}
