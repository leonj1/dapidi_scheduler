package com.dapidi.scheduler.controllers.routes.agents;

import com.dapidi.scheduler.controllers.routes.ExitRoute;
import com.dapidi.scheduler.services.AgentService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ListAllAgentsRoute implements Route, ExitRoute {

    private AgentService agentService;
    private Gson gson;

    public ListAllAgentsRoute(AgentService agentService) {
        this.agentService = agentService;
        this.gson = new Gson();
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return this.gson.toJson(this.agentService.listRegisteredMachines());
    }
}
