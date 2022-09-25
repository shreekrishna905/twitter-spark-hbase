package com.bdt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bdt.service.SparkService;

@SpringBootApplication
public class TwitterSparkApplication implements CommandLineRunner {

	@Autowired
    private SparkService sparkService;

    public static void main(String[] args) {
 
        SpringApplication.run(TwitterSparkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        sparkService.process();
    }

}
