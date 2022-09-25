package com.bdt.service;

import java.io.IOException;

import org.apache.htrace.fasterxml.jackson.core.JsonParseException;
import org.apache.htrace.fasterxml.jackson.databind.JsonMappingException;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bdt.Constant;
import com.bdt.TweetHash;
import com.bdt.repository.HbaseRepository;

@Service
public class HbaseServiceImpl implements HbaseService {

	private HbaseRepository hbaseRepository;
	
	private ObjectMapper objectMapper;
	
	public HbaseServiceImpl(HbaseRepository hbaseRepository){
		this.hbaseRepository = hbaseRepository;
		objectMapper = new ObjectMapper();
	}
	
	@Override
	@KafkaListener(topics = Constant.KAFKA_HBASE_TOPIC, groupId = Constant.GROUP_ID)
	public void insert(String message) throws JsonParseException, JsonMappingException, IOException {
		TweetHash tweet = objectMapper.readValue(message, TweetHash.class);
		System.out.println("Received data, Tweet Hash: "+ tweet.getHash()+", Tweet Count: "+ tweet.getCount());
		hbaseRepository.insert(tweet.getHash(), tweet.getCount());
	}

}
