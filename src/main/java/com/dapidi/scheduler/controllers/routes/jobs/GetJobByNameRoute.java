package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.models.JobDefinition;
import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class GetJobByNameRoute implements Route {
    private JobService jobService;

    public GetJobByNameRoute(JobService jobService) {
        this.jobService = jobService;
    }

    public String execute(Response res, String jobName) {
        JobDefinition jobDefinition = null;
        try {
            jobDefinition = this.jobService.getJobByName(jobName);
        } catch (Exception e) {
            return SimpleExitRoute.builder(res).INTERNAL_SERVER_ERROR_500().text(e.getMessage(), e);
        }

        return SimpleExitRoute.builder(res).OK_200().json(jobDefinition, JobDefinition.class);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.params("name"));
    }
}
