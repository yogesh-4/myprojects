package com.sample.project.common;

public class Constants {
	
	public static final String SECRET = "SecretKeyToGenJWTs";
    //public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final long EXPIRATION_TIME = 180000; // 1 min
    public static final String TOKEN_BEARER = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN = "/oauth/login";
    public static final String REGISTER = "/oauth/register";
	public static final String REFRESH_TOKEN = "/oauth/refreshtoken";
	public static final String DB_URL = "/h2-console/**";
	public static final String LOGOUT = "/oauth/logout";
	public static final String ACCESS_DENIED = "ACCESS_DENIED";
	public static final String SUCCESS_CODE = "100";
	public static final String LOGOUT_MESSAGE = "Logout Success";
	public static final String SUCCESS_MESSAGE = "SUCCESS";
	

}
