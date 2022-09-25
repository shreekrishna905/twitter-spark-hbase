package com.bdt;

import com.bdt.service.TwitterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterStreamApplication implements CommandLineRunner {

    @Autowired
    private TwitterService twitterService;

    public static void main(String[] args) {
        SpringApplication.run(TwitterStreamApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        twitterService.send();
    }

}
