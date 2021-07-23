package com.code.removal.sc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thoughtworks.qdox.JavaDocBuilder;

@Configuration
public class SourceCodeConfiguration {

	@Bean
	public JavaDocBuilder getJavaDocBuilder() {
		return new JavaDocBuilder();
	}
}
