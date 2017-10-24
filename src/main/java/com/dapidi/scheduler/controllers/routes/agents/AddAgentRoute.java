package com.dapidi.scheduler.controllers.routes.agents;

import com.dapidi.scheduler.context.agents.AddAgentContext;
import com.dapidi.scheduler.services.AgentService;
import com.dapidi.scheduler.services.SimpleExitRoute;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AddAgentRoute implements Route {
    private static final Logger log = LoggerFactory.getLogger(AddAgentRoute.class);
    private AgentService agentService;
    private Gson gson;

    public AddAgentRoute(AgentService agentService) {
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
        UUID agentId = this.agentService.register(context);
        log.info(
                String.format(
                        "Agent for machine %s registered as id %s",
                        context.getMachine(),
                        agentId.toString()
                )
        );
        return SimpleExitRoute.builder(res).OK_200().text(agentId.toString());
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return execute(response, request.body());
    }
}
