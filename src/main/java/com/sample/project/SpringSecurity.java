package com.sample.project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sample.project.common.Constants;
import com.sample.project.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SpringSecurity  extends WebSecurityConfigurerAdapter{
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	//.antMatchers(HttpMethod.POST, Constants.REFRESH_TOKEN).permitAll()
	/* @Override
	public void configure(WebSecurity web) throws Exception {
		 web.ignoring().antMatchers(Constants.REFRESH_TOKEN);
	 }*/
	
	@Bean
	public AccessDeniedHandler accessdeniedHandler() {
		return new RestAccessDeniedHandler();
	}
	
	@Bean
	public AuthenticationEntryPoint authenticationEntry() {
		return new RestAuthenticationEntryPoint();
	}

	 
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.cors().and().csrf().disable().authorizeRequests()
	        		
	                .antMatchers(Constants.LOGIN,Constants.REFRESH_TOKEN,Constants.DB_URL,Constants.REGISTER,Constants.LOGOUT).permitAll()
	                
	                .anyRequest().authenticated()
	                .and()
	               // .addFilter(new JWTAuthenticationFilter(authenticationManager()))
	                .addFilter(new JWTAuthorizationFilter(authenticationManagerBean())).exceptionHandling()
	                .accessDeniedHandler(accessdeniedHandler()).authenticationEntryPoint(authenticationEntry()).and()
	                // this disables session creation on Spring Security
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        http.headers().frameOptions().sameOrigin();
	    }

	    @Override
	    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception{
	    	return super.authenticationManagerBean();
	    }
	    
	    @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	      final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	      source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	      return source;
	    }


}
