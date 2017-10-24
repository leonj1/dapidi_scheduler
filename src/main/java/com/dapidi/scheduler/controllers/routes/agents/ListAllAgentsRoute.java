package com.dapidi.scheduler.controllers.routes.agents;

import com.dapidi.scheduler.services.AgentService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashSet;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ListAllAgentsRoute implements Route {

    private AgentService agentService;

    public ListAllAgentsRoute(AgentService agentService) {
        this.agentService = agentService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return SimpleExitRoute.builder(res).OK_200()
                .json(
                        this.agentService.listRegisteredMachines(),
                        HashSet.class
                );
    }
}
