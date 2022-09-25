package com.bdt.config;

import okhttp3.OkHttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterStreamConfiguration {

	@Bean
    public OkHttpClient okHttpClient(){
    	return new OkHttpClient();
    }
	
}
