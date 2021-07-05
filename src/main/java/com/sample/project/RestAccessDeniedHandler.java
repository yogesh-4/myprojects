package com.sample.project;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.project.common.Constants;
import com.sample.project.dto.TokenResponse;

public class RestAccessDeniedHandler implements  AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse httpresponse,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		TokenResponse response = new TokenResponse(String.valueOf(HttpStatus.FORBIDDEN.value()),
				Constants.ACCESS_DENIED);
		OutputStream out = httpresponse.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, response);
		httpresponse.setStatus(HttpStatus.OK.value());
		out.flush();

	}

}
