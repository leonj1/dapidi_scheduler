package com.dapidi.scheduler.controllers.routes.agents;

import com.dapidi.scheduler.context.agents.AddAgentContext;
import com.dapidi.scheduler.services.AgentService;
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
public class AgentCheckInRoute implements Route {
    private AgentService agentService;
    private Gson gson;

    public AgentCheckInRoute(AgentService agentService) {
        this.agentService = agentService;
        this.gson = new Gson();
    }

    private String execute(Response res, String payload) {
        AddAgentContext context;
        try {
            context = this.gson.fromJson(payload, AddAgentContext.class);
        } catch (JsonSyntaxException e) {
            return SimpleExitRoute.builder(res).BAD_REQUEST_400().text("invalid json", e);
        }
        this.agentService.checkIn(context);
        return SimpleExitRoute.builder(res).OK_200().text("Hello there!");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.body());
    }
}
