package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.context.jobs.UpdateJobInstanceContext;
import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class UpdateJobInstanceRoute implements Route {
    private JobService jobService;
    private Gson gson;

    public UpdateJobInstanceRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload) {
        UpdateJobInstanceContext context;
        try {
            context = this.gson.fromJson(payload, UpdateJobInstanceContext.class);
        } catch (JsonSyntaxException e) {
            return SimpleExitRoute.builder(res).BAD_REQUEST_400().text("invalid json", e);
        }

        try {
            this.jobService.updateJobInstance(context);
        } catch (Exception e) {
            return SimpleExitRoute.builder(res).NOT_FOUND_404().text(e.getMessage(), e);
        }

        return SimpleExitRoute.builder(res).OK_200().text("updated");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.body());
    }
}
