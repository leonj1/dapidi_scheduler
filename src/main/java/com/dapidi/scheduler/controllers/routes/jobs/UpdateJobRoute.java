package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.context.jobs.UpdateJobContext;
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

import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class UpdateJobRoute implements Route, ExitRoute {

    private Gson gson;
    private JobService jobService;

    public UpdateJobRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload, UUID id) {
        UpdateJobContext context;
        try {
            context = this.gson.fromJson(payload, UpdateJobContext.class);
        } catch (JsonSyntaxException e) {
            return this.exit(res, HttpStatus.BAD_REQUEST_400, "invalid json", e);
        }

        try {
            this.jobService.updateJob(id, context);
        } catch (Exception e) {
            return this.exit(res, HttpStatus.BAD_REQUEST_400, "unable to update job state", e);
        }

        return this.exit(res, HttpStatus.OK_200, "updated", null);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UUID id = null;
        try {
            id = UUID.fromString(request.params(":id"));
        } catch (NumberFormatException e) {
            return this.exit(response, HttpStatus.BAD_REQUEST_400, "invalid id", e);
        }
        return execute(response, request.body(), id);
    }
}
