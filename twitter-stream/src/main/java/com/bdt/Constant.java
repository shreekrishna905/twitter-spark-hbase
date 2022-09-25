package com.bdt;

public interface Constant {
    String BEARER_TOKEN = "Bearer AAAAAAAAAAAAAAAAAAAAAMDGhAEAAAAARTRJpSWOXWtuVISlyUrWZ61AmJA%3DQuEnB7VyJy9nfQuz8bbDao2mSnoCwN2QNNPQYvZrju25BkSJre";
    String KAFKA_TOPIC = "TWEET";
    String TWITTER_STREAM_API ="https://api.twitter.com/2/tweets/sample/stream?tweet.fields=geo,id,lang,text&user.fields=name,username&place.fields=country_code,geo,place_type";
    String AUTHORIZATION_HEADER = "Authorization";
}
