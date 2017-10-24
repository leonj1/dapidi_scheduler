package com.dapidi.scheduler.services;

import java.io.IOException;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public interface AppHttpClient {
    WebRestResponse getActiveJobInstances(String baseUrl) throws IOException;
    WebRestResponse getActiveJobInstance(String baseUrl, UUID jobInstanceUuid) throws IOException;
    WebRestResponse health(String baseUrl) throws IOException;
}
