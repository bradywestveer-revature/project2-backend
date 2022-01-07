package com.revature.project2backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
class CORSConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings (CorsRegistry registry) {
		registry.addMapping ("/**").allowedOrigins ("http://localhost:4200/", "http://3.137.152.242/").allowCredentials (true);
	}
}