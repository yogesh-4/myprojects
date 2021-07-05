package com.sample.project;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.project.dto.TokenResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse httpresponse,
			AuthenticationException authException) throws IOException, ServletException {
		
		 TokenResponse response = new TokenResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
				 HttpStatus.UNAUTHORIZED.name());
	        OutputStream out = httpresponse.getOutputStream();
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.writeValue(out, response);
	        httpresponse.setStatus(HttpStatus.OK.value());
	        out.flush();
	}
	}
