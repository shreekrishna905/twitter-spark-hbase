package com.bdt.config;

import org.apache.spark.SparkConf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TwitterSparkConfig {
	
	@Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName("TwitterSparkAnalysis")
                .setMaster("local[2]")
                .set("spark.executor.memory","1g");
    }

}
