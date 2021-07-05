package com.sample.project.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import com.sample.project.common.Constants;
import com.sample.project.dto.TokenResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

public class TokenUtil {

	private final static String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

	public static Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

	public static TokenResponse generateToken(String subject, String jti) throws Exception {

		TokenResponse response = new TokenResponse();

		long currentTime = System.currentTimeMillis();

		Date currentDate = new Date(currentTime);

		Date expiredDate = new Date(currentTime + Constants.EXPIRATION_TIME);

		JwtBuilder token = Jwts.builder().setId(jti).setSubject(subject).setIssuedAt(currentDate)
				.setExpiration(expiredDate).signWith(SignatureAlgorithm.HS256, Constants.SECRET);

		response.setUser(subject);
		response.setToken(token.compact());
		response.setExpirationTime(convertDateToString(expiredDate));
		response.setIssuedTime(convertDateToString(currentDate));
		response.setTokenType(Constants.TOKEN_BEARER);

		return response;

	}

	public static String genrateUUUID() {
		return UUID.randomUUID().toString();
	}

	public static Claims validateToken(String token) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(Constants.SECRET)).parseClaimsJws(token)
				.getBody();
	}

	public static String convertDateToString(Date date) throws Exception {
		String dateString = null;

		try {
			if (date != null) {

				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				dateString = sdf.format(date);
			}
		} catch (Exception e) {
			throw e;
		}

		return dateString;

	}

}
