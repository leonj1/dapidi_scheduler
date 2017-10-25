package com.dapidi.scheduler.controllers;

import com.dapidi.scheduler.controllers.routes.jobs.AddJobRoute;
import com.dapidi.scheduler.controllers.routes.jobs.GetAllJobsRoute;
import com.dapidi.scheduler.controllers.routes.jobs.GetJobByNameRoute;
import com.dapidi.scheduler.controllers.routes.jobs.UpdateJobInstanceRoute;
import com.dapidi.scheduler.controllers.routes.jobs.UpdateJobRoute;
import com.dapidi.scheduler.services.Controller;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobsController implements Controller {

    private GetAllJobsRoute getAllJobsRoute;
    private AddJobRoute addJobRoute;
    private UpdateJobRoute updateJobRoute;
    private UpdateJobInstanceRoute updateJobInstanceRoute;
    private GetJobByNameRoute getJobByNameRoute;

    public JobsController(GetAllJobsRoute getAllJobsRoute, AddJobRoute addJobRoute, UpdateJobRoute updateJobRoute, UpdateJobInstanceRoute updateJobInstanceRoute, GetJobByNameRoute getJobByNameRoute) {
        this.getAllJobsRoute = getAllJobsRoute;
        this.addJobRoute = addJobRoute;
        this.updateJobRoute = updateJobRoute;
        this.updateJobInstanceRoute = updateJobInstanceRoute;
        this.getJobByNameRoute = getJobByNameRoute;
    }

    public void expose() {

        get("/jobs/all", this.getAllJobsRoute);

        get("/jobs/:name", this.getJobByNameRoute);

        post("/jobs", this.addJobRoute);

        // to put the job ONICE, ONHOLD, etc
        put("/job/:id", this.updateJobRoute);

        // to update the status of any jobs that ran on machines
        put("/jobinstance/:id", this.updateJobInstanceRoute);

    }
}
