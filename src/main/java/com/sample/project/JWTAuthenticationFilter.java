package com.sample.project;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.project.common.Constants;
import com.sample.project.domain.AppUserDetails;
import com.sample.project.domain.TokenDetails;
import com.sample.project.service.TokenAuthenticationService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Autowired
    TokenAuthenticationService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AppUserDetails creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AppUserDetails.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmailId(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	
    	String id = UUID.randomUUID().toString();
    	TokenDetails result = null;
    	String subject = ((User) auth.getPrincipal()).getUsername();
    	
        
    	SignatureAlgorithm signAlgo = SignatureAlgorithm.HS256;
    	byte[] secretKey = DatatypeConverter.parseBase64Binary(Constants.SECRET);
    	Key signingKey = new SecretKeySpec(secretKey, signAlgo.getJcaName());
    	
    	JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(new Date(System.currentTimeMillis()))
    			.setSubject(subject).setIssuer("iss").signWith(signAlgo, signingKey);

    	if(Constants.EXPIRATION_TIME >0) {
    		long expTime=System.currentTimeMillis()+Constants.EXPIRATION_TIME ;
    		builder.setExpiration(new Date(expTime));
    	}
        String token = /*JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .sign(HMAC512(Constants.SECRET.getBytes()));
        */builder.compact();
        System.out.println("The Token is "+token);
        
          
       // res.addHeader(Constants.HEADER_STRING, Constants.TOKEN_PREFIX + token);
    	}
        
       

}
