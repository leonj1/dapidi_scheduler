package com.dapidi.scheduler.services;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface RestClient {
    int statusCode() throws Exception;
    String contents() throws Exception;
}
