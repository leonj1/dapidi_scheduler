package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.context.jobs.AddJobDefinitionContext;
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
public class AddJobRoute implements Route {

    private JobService jobService;
    private Gson gson;

    public AddJobRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload) {
        AddJobDefinitionContext context;
        try {
            context = this.gson.fromJson(payload, AddJobDefinitionContext.class);
        } catch (JsonSyntaxException e) {
            return SimpleExitRoute.builder(res).BAD_REQUEST_400().text("invalid json", e);
        }

        this.jobService.addJob(context);

        return SimpleExitRoute.builder(res).OK_200().text("saved");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.body());
    }
}
