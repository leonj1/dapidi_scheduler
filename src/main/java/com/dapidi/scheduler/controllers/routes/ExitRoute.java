package com.dapidi.scheduler.controllers.routes;

import com.dapidi.scheduler.response.SimpleResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2016
 **/
public interface ExitRoute {

    Logger log = LoggerFactory.getLogger(ExitRoute.class);

    default String exit(Response res, int status, String message, Exception e) {
        Gson gson = new Gson();
        if (e != null) {
            log.error(message, e);
        }
        res.status(status);
        return gson.toJson(new SimpleResponse(message));
    }

}
