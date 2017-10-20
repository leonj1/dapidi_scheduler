package com.dapidi.scheduler.services;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class HttpPost implements RestClient {
    private static final Logger log = LoggerFactory.getLogger(HttpGet.class);
    private static final int FIVE_SECOND_TIMEOUT = 5000;
    private String url;
    private String json;

    public HttpPost(String url, String json) {
        this.url = url;
        this.json = json;
    }

    @Override
    public int statusCode() throws Exception {
        log.info(String.format("Getting %s", this.url));
        URL obj = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(FIVE_SECOND_TIMEOUT);
        conn.setReadTimeout(FIVE_SECOND_TIMEOUT);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStream os = conn.getOutputStream();
        os.write(this.json.getBytes("UTF-8"));
        os.close();

        conn.disconnect();

        return conn.getResponseCode();
    }

    @Override
    public String contents() throws Exception {
        log.info(String.format("Getting %s", this.url));
        URL obj = new URL(this.url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(FIVE_SECOND_TIMEOUT);
        conn.setReadTimeout(FIVE_SECOND_TIMEOUT);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStream os = conn.getOutputStream();
        os.write(this.json.getBytes("UTF-8"));
        os.close();

        // read the response - this line performed the HTTP call
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = IOUtils.toString(in, "UTF-8");
//        JSONObject jsonObject = new JSONObject(result);

        in.close();
        conn.disconnect();

        return result;
    }
}
