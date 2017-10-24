package com.dapidi.scheduler.services;

import org.apache.http.client.methods.HttpPost;

/**
 * Class needed to extend since HttpDelete does not accept body in request
 * Source: https://stackoverflow.com/a/16959547/706837
 *
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class MyHttpDelete extends HttpPost {
    public MyHttpDelete(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }
}
