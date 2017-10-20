package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.context.jobs.AddJobDefinitionContext;
import com.dapidi.scheduler.controllers.routes.ExitRoute;
import com.dapidi.scheduler.services.JobService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AddJobRoute implements Route, ExitRoute {

    private Gson gson;
    private JobService jobService;

    public AddJobRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload) {
        AddJobDefinitionContext context;
        try {
            context = this.gson.fromJson(payload, AddJobDefinitionContext.class);
        } catch (JsonSyntaxException e) {
            return this.exit(res, HttpStatus.BAD_REQUEST_400, "invalid json", e);
        }

        this.jobService.addJob(context);

        return this.exit(res, HttpStatus.OK_200, "saved", null);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.body());
    }
}
