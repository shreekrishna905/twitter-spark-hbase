package com.bdt.service;

import com.bdt.Constant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;


@Service
public class TwitterServiceImpl implements TwitterService{
	
    private KafkaTemplate<String, String> kafkaTemplate;

    private OkHttpClient okHttpClient;
    
    public TwitterServiceImpl(KafkaTemplate<String,String> kafkaTemplate, OkHttpClient okHttpClient){
        this.kafkaTemplate = kafkaTemplate;
        this.okHttpClient = okHttpClient;
    }
    
    @Override
    public void send() throws IOException, URISyntaxException, InterruptedException {
    	
    	Request request = new Request.Builder()
        .url(Constant.TWITTER_STREAM_API)
        .addHeader(Constant.AUTHORIZATION_HEADER, Constant.BEARER_TOKEN)
        .build();

		try (Response response = okHttpClient.newCall(request).execute()) {
		
		    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		
		    sendToKafkaTopic(response.body().byteStream());
		}
    }

    private void sendToKafkaTopic(InputStream httpResponse) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse));
        String line = null;
        while ((line = reader.readLine()) != null) {
        	if(!line.isEmpty()){
        		System.out.println(line);
        		kafkaTemplate.send(Constant.KAFKA_TOPIC, line);
        	}
        }
    }
}
