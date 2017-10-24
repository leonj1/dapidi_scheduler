package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.services.JobService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class GetAllJobsRoute implements Route {

    private JobService jobService;

    public GetAllJobsRoute(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return SimpleExitRoute.builder(response)
                .OK_200()
                .json(
                        this.jobService.getJobsAndInstances(),
                        ArrayList.class
                );
    }
}
