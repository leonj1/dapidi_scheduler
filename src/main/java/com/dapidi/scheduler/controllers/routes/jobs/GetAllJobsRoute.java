package com.dapidi.scheduler.controllers.routes.jobs;

import com.dapidi.scheduler.controllers.routes.ExitRoute;
import com.dapidi.scheduler.models.Jobs;
import com.dapidi.scheduler.services.JobService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class GetAllJobsRoute implements Route, ExitRoute {

    private JobService jobService;
    private Gson gson;

    public GetAllJobsRoute(JobService jobService) {
        this.jobService = jobService;
        this.gson = new Gson();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        List<Jobs> jobs = this.jobService.getJobsAndInstances();
        return this.gson.toJson(jobs);
    }
}
