package com.bdt;

public interface Constant {
    
	String KAFKA_HBASE_TOPIC = "TWEET_HBASE";
    
    String GROUP_ID ="tweet_hash";
    
    String TABLE_NAME = "tweet_analysis";
    
    String TWEET_HASH_FAMILY = "tweet_hash";
    
    String TWEET_COUNT_FAMILY = "tweet_count";
    
    String HASH_COLUMN = "hash";
    
    String COUNT_COLUMN = "count";
    
    
}
