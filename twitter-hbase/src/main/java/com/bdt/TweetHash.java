package com.bdt;

public class TweetHash {
	
	private String hash;
	
	private String count;
	
	
	public TweetHash(){}

	public TweetHash(String hash, String count) {
		super();
		this.hash = hash;
		this.count = count;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	
	

}
