package com.bdt.service;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TwitterService {

    void send() throws IOException, URISyntaxException, InterruptedException;
}
