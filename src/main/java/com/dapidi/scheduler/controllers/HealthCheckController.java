package com.dapidi.scheduler.controllers;

import com.dapidi.scheduler.controllers.routes.health.HealthCheckRoute;
import com.dapidi.scheduler.services.Controller;

import static spark.Spark.get;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class HealthCheckController implements Controller {
    private HealthCheckRoute healthCheckRoute;

    public HealthCheckController(HealthCheckRoute healthCheckRoute) {
        this.healthCheckRoute = healthCheckRoute;
    }

    @Override
    public void expose() {
        get("/public/health", this.healthCheckRoute);
    }
}
