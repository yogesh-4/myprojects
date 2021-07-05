package com.sample.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.sample.project")
@EnableCaching
@PropertySource(value ="application.properties")
public class Application  {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
