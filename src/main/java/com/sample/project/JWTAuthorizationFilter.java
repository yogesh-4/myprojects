package com.sample.project;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.project.common.Constants;
import com.sample.project.domain.TokenDetails;
import com.sample.project.service.TokenAuthenticationService;
import com.sample.project.utils.TokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;



public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
	
	

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	
	
	@Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(Constants.HEADER_STRING);

        if (header == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws JsonMappingException, JsonProcessingException ,
			ServletException {
		String token = request.getHeader(Constants.HEADER_STRING);
		String requestURL = request.getRequestURL().toString();
		if (token != null) {

			try {
				// token validation
				Claims tokenClaims = TokenUtil.validateToken(token);
				
				if (requestURL.contains("logout"))
					request.setAttribute("claims", tokenClaims);

				String user = tokenClaims.getSubject();
				System.out.println("The User is " + tokenClaims.getSubject());
				System.out.println("The ID is " + tokenClaims.getId());
				System.out.println("The expiry time is" + tokenClaims.getExpiration());
				request.setAttribute("tokenId", tokenClaims.getId());
				request.setAttribute("user", tokenClaims.getSubject());
				if (user != null) {
					return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
				}
				
				return null;
			} catch (ExpiredJwtException tokenExp) {
				String isRefreshToken = request.getHeader("isRefreshToken");

				// allow for Refresh Token creation if following conditions are true.
				if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
					System.out.println("refersh token");
					allowForRefreshToken(tokenExp, request);
				} else if (requestURL.contains("logout"))
					request.setAttribute("claims", tokenExp.getClaims());
				else
					throw tokenExp;

			}

		}
		return null;
	}

	private void allowForRefreshToken(ExpiredJwtException tokenExp, HttpServletRequest request) {
		// create a UsernamePasswordAuthenticationToken with null values.
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						null, null, null);
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				// Set the claims so that in controller we will be using it to create
				// new JWT
				request.setAttribute("claims", tokenExp.getClaims());
				//request.setAttribute("subject", tokenExp.getClaims().getSubject());
		
	}

}
