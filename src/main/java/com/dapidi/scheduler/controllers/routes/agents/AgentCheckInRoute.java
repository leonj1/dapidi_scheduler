package com.dapidi.scheduler.controllers.routes.agents;

import com.dapidi.scheduler.controllers.routes.ExitRoute;
import com.dapidi.scheduler.services.AgentService;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class AgentCheckInRoute implements Route, ExitRoute {
    private AgentService agentService;

    public AgentCheckInRoute(AgentService agentService) {
        this.agentService = agentService;
    }

    private String execute(Response res, String machine) {
        this.agentService.checkIn(machine);
        return this.exit(res, HttpStatus.OK_200, "Hello there!", null);
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String machine;
        machine = request.params(":machine");
        if (machine == null || machine.equals("")) {
            return this.exit(response, HttpStatus.BAD_REQUEST_400, "no machine provided", null);
        }
        return execute(response, machine);
    }
}
