package com.bdt.service;

import java.io.IOException;

import org.apache.htrace.fasterxml.jackson.core.JsonParseException;
import org.apache.htrace.fasterxml.jackson.databind.JsonMappingException;

public interface HbaseService {
	
	void insert(String message) throws JsonParseException, JsonMappingException, IOException;

}
