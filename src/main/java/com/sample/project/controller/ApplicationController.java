package com.sample.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.project.common.Constants;
import com.sample.project.domain.AppUserDetails;
import com.sample.project.domain.TokenDetails;
import com.sample.project.dto.TokenResponse;
import com.sample.project.dto.TokenValidRequest;
import com.sample.project.dto.UserDto;
import com.sample.project.service.ApplicationService;
import com.sample.project.service.TokenAuthenticationService;
import com.sample.project.utils.TokenUtil;

import io.jsonwebtoken.impl.DefaultClaims;

@RestController
@RequestMapping("/oauth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ApplicationController {

	@Autowired
	private ApplicationService service;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private TokenAuthenticationService tokenService;

	@Autowired
	@Qualifier("UserService")
	private UserDetailsService userService;

	@PostMapping("/register")
	ResponseEntity<?> saveUser(@RequestBody UserDto userDetails) throws Exception {

		TokenResponse response = new TokenResponse();

		AppUserDetails result = service.findByUserName(userDetails.getEmailId());
		if (result != null)
			throw new Exception("Username already talen");

		// TODO - Use AES encryption
		userDetails.setPassword(userDetails.getPassword());

		service.saveUser(userDetails);

		response.setStatusCode(Constants.SUCCESS_CODE);

		response.setStatusMessage(Constants.SUCCESS_MESSAGE);

		return new ResponseEntity<TokenResponse>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	ResponseEntity<?> loginUser(@RequestBody UserDto userDetails) throws Exception {

		authenticate(userDetails.getEmailId(), userDetails.getPassword());

		String subject = userDetails.getEmailId();

		// checking for active tokens
		TokenDetails result = tokenService.getActiveUser(subject);

		if (result != null)
			throw new Exception("User has active session.Please log out");

		// Generate JWT token
		String jti = TokenUtil.genrateUUUID();
		TokenResponse response = TokenUtil.generateToken(subject, jti);

		// Adding users into cache
		TokenDetails tokenDtls = new TokenDetails();
		tokenDtls.setSubject(subject);
		tokenDtls.setTokenId(jti);
		tokenService.addUser(tokenDtls);

		response.setStatusCode(Constants.SUCCESS_CODE);

		response.setStatusMessage(Constants.SUCCESS_MESSAGE);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/users")
	ResponseEntity<List<UserDto>> getAllUsers() {

		return new ResponseEntity<List<UserDto>>(service.getAllUsers(), HttpStatus.OK);
	}
	
	@GetMapping("/validate")
	ResponseEntity<?> validate(HttpServletRequest hRequest) throws Exception {
		
		TokenResponse response = new TokenResponse();
		
		
				String currentId  = (String) hRequest.getAttribute("tokenId");
				String subject  = (String) hRequest.getAttribute("user");
				
		
				
				TokenDetails result = tokenService.getUser(currentId);
				if (!result.getSubject().equals(subject) || !result.getTokenId().equals(currentId))
					throw new Exception("Unauthorized Request");
				
				
				response.setStatusCode(Constants.SUCCESS_CODE);

				response.setStatusMessage(Constants.SUCCESS_MESSAGE);

				return ResponseEntity.ok(response);

	}
	

	@GetMapping("/refreshtoken")
	ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {

		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
		if (claims == null)
			throw new Exception("User session is active");

		Map<String, Object> expectedMap = TokenUtil.getMapFromIoJsonwebtokenClaims(claims);

		// checking for active tokens
		String subject = expectedMap.get("sub").toString();
		String currentId = claims.get("jti").toString();
		TokenDetails result = tokenService.getUser(currentId);

		if (result == null)
			throw new Exception("Invalid request.Please Login");
		if (!result.getSubject().equals(subject) || !result.getTokenId().equals(currentId))
			throw new Exception("Unauthorized Request");

		// Generate JWT token
		String jti = TokenUtil.genrateUUUID();
		TokenResponse response = TokenUtil.generateToken(subject, jti);

		// update token
		result.setTokenId(jti);
		tokenService.updateUser(result);

		response.setStatusCode(Constants.SUCCESS_MESSAGE);

		response.setStatusMessage(HttpStatus.OK.name());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/logout")
	ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

		TokenResponse token = new TokenResponse();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
			new SecurityContextLogoutHandler().logout(request, response, auth);

		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		// clear token
		TokenDetails result = tokenService.getUser(claims.get("jti").toString());
		if (result == null) {
			throw new Exception("There is no active Session.Please Log in");
		}
		tokenService.removeUser(result.getTokenId());

		token.setStatusCode(Constants.SUCCESS_CODE);

		token.setStatusMessage(Constants.SUCCESS_MESSAGE);

		token.setUser(result.getSubject());

		return ResponseEntity.ok(token);
	}

	private void authenticate(String username, String password) throws Exception {

		// try {
		// authenticationManager
		// .authenticate(new UsernamePasswordAuthenticationToken(username, password, new
		// ArrayList<>()));
		AppUserDetails result = service.findByUserName(username);
		if(result == null) 
			throw new Exception("REGISTER USER AND LOGIN");
		if (!result.getPassword().equals(password))

			// } catch (AuthenticationException e) {
			throw new Exception("INVALID CREDENTIALS");
	}

}
