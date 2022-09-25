package com.bdt.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import scala.Tuple2;

import com.bdt.Constant;
import com.bdt.domain.Tweet;
import com.bdt.util.TweetDeserializer;
import com.bdt.util.TweetHash;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SparkServiceImpl implements SparkService {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;
	
	private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

	private SparkConf sparkConf;
	
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public SparkServiceImpl(SparkConf sparkConf, KafkaTemplate<String, String> kafkaTemplate){
		this.sparkConf = sparkConf;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@Override
	public void process() {
		
		JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(10));
		
		ObjectMapper mapper = new ObjectMapper();

        JavaInputDStream<ConsumerRecord<String, Tweet>> messages = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.<String, Tweet> Subscribe(Arrays.asList(Constant.KAFKA_TOPIC), kafkaParams()));

        JavaDStream<Tweet> tweets = messages.map(stringStringConsumerRecord -> stringStringConsumerRecord.value());

        tweets
        .filter(tweet ->  Objects.nonNull(tweet) && isTweetEnglish(tweet))
         .count()
		 .map(cnt -> "Popular hash tags in last 60 seconds (" + cnt + " total tweets):")
		 .print();
        
        tweets
        .filter(tweet ->  Objects.nonNull(tweet) && isTweetEnglish(tweet))
        .flatMap(text -> hashTagsFromTweet(text))
        .mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
        .reduceByKey((a, b) -> Integer.sum(a, b))
        .mapToPair(stringIntegerTuple2 -> stringIntegerTuple2.swap())
        .foreachRDD(rrdd -> {
            System.out.println("---------------------------------------------------------------");
            List<Tuple2<Integer, String>> sorted;
            JavaPairRDD<Integer, String> counts = rrdd.sortByKey(false);
            sorted = counts.collect();
            sorted.forEach( record -> {
            	try {
					kafkaTemplate.send(Constant.KAFKA_HBASE_TOPIC,mapper.writeValueAsString(new TweetHash(record._2, record._1.toString())));
				} catch (Exception e) {
					e.printStackTrace();
				}
                System.out.println(String.format(" %s (%d)", record._2, record._1));
            });
        });
        
        streamingContext.start();
        try {
        	streamingContext.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	


    public static Iterator<String> hashTagsFromTweet(Tweet tweet) {
        List<String> hashTags = new ArrayList<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(tweet.getText());
        while (matcher.find()) {
            String handle = matcher.group();
            hashTags.add(handle);
        }
        return hashTags.iterator();
    }
    
    public static boolean isTweetEnglish(Tweet tweet) {
    return "en".equals(tweet.getLang());
  }

	
	
	private Map<String,Object> kafkaParams(){
		Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", bootstrapServer);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", TweetDeserializer.class);
        kafkaParams.put("group.id", Constant.GROUP_ID);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        return kafkaParams;

	}

}
