package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.context.jobs.DeleteJobContext;
import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class DeleteJobRoute implements Route {
    private static final Logger log = LoggerFactory.getLogger(DeleteJobRoute.class);

    private JobService jobService;
    private Gson gson;

    public DeleteJobRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload) {
        DeleteJobContext context;
        try {
            context = this.gson.fromJson(payload, DeleteJobContext.class);
        } catch (JsonSyntaxException e) {
            return SimpleExitRoute.builder(res).BAD_REQUEST_400().text("invalid json", e);
        }

        log.info(String.format("Attempting to delete job %s", context.jobId));
        com.dapidi.scheduler.response.Response result = this.jobService.deleteJob(context.jobId);

        return SimpleExitRoute.builder(res).OK_200().text(result.getMessage());
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.body());
    }
}
