package com.bdt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TwitterHbaseApplication implements CommandLineRunner {
	
    public static void main(String[] args) {
        SpringApplication.run(TwitterHbaseApplication.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		
	}

}
