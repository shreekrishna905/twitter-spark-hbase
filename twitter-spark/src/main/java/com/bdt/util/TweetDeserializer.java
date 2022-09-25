package com.bdt.util;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.bdt.domain.Tweet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TweetDeserializer implements Deserializer<Tweet> {
	
	 private ObjectMapper objectMapper = new ObjectMapper();

	    @Override
	    public void configure(Map<String, ?> configs, boolean isKey) {
	    }

	    @Override
	    public Tweet deserialize(String topic, byte[] data) {
	        try {
	            if (data == null || data.length==0){
	                return null;
	            }
	            JsonNode jsonNode = objectMapper.readTree(new String(data, "UTF-8"));
	            jsonNode = jsonNode.get("data");
	            return objectMapper.readValue(jsonNode.toString(), Tweet.class);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            throw new SerializationException("Error when deserializing byte[] to Tweet");
	        }
	    }

	    @Override
	    public void close() {
	    }
}
