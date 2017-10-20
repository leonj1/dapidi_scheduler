package com.dapidi.scheduler.controllers;

import com.dapidi.scheduler.controllers.routes.agents.AddAgentRoute;
import com.dapidi.scheduler.controllers.routes.agents.AgentCheckInRoute;
import com.dapidi.scheduler.controllers.routes.agents.ListAllAgentsRoute;
import com.dapidi.scheduler.services.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static spark.Spark.post;
import static spark.Spark.get;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class CommunicationBetweenAgentsController implements Controller {

    private AddAgentRoute addAgentRoute;
    private AgentCheckInRoute agentCheckInRoute;
    private ListAllAgentsRoute listAllAgentsRoute;

    public CommunicationBetweenAgentsController(AddAgentRoute addAgentRoute, AgentCheckInRoute agentCheckInRoute, ListAllAgentsRoute listAllAgentsRoute) {
        this.addAgentRoute = addAgentRoute;
        this.agentCheckInRoute = agentCheckInRoute;
        this.listAllAgentsRoute = listAllAgentsRoute;
    }

    public void expose() {
        post("/agents", this.addAgentRoute);
        get("/agents", this.listAllAgentsRoute);
        post("/agent/:machine", this.agentCheckInRoute);
    }
}
