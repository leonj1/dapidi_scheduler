package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.context.jobs.UpdateJobContext;
import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class UpdateJobRoute implements Route {

    private JobService jobService;
    private Gson gson;

    public UpdateJobRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload, UUID id) {
        UpdateJobContext context;
        try {
            context = this.gson.fromJson(payload, UpdateJobContext.class);
        } catch (JsonSyntaxException e) {
            return SimpleExitRoute.builder(res).BAD_REQUEST_400().text("invalid json", e);
        }

        try {
            this.jobService.updateJob(id, context);
        } catch (Exception e) {
            return SimpleExitRoute.builder(res).BAD_REQUEST_400().text("unable to update job state", e);
        }

        return SimpleExitRoute.builder(res).OK_200().text("updated");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UUID id = null;
        try {
            id = UUID.fromString(request.params(":id"));
        } catch (NumberFormatException e) {
            return SimpleExitRoute.builder(response).BAD_REQUEST_400().text("invalid id", e);
        }
        return execute(response, request.body(), id);
    }
}
