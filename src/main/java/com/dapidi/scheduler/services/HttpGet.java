package com.dapidi.scheduler.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class HttpGet implements RestClient{
    private static final Logger log = LoggerFactory.getLogger(HttpGet.class);
    private String url;

    public HttpGet(String url) {
        this.url = url;
    }

    @Override
    public int statusCode() throws Exception {
        log.info(String.format("Getting %s", this.url));
        HttpClient client = HttpClientBuilder.create().build();
        org.apache.http.client.methods.HttpGet request = new org.apache.http.client.methods.HttpGet(url);
        HttpResponse response = client.execute(request);
        return response.getStatusLine().getStatusCode();
    }

    @Override
    public String contents() throws Exception {
        URL obj = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
