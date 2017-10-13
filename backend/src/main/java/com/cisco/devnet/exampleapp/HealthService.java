package com.cisco.devnet.exampleapp;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.cisco.devnet.infracheck.InfraCheck;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.logging.Logger;

@Path("/")
public class HealthService {

    private static final Logger log = Logger.getLogger(HealthService.class.getName());

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHealth(@PathParam("param") String message) {

        log.info("Health check started via API!");

        String healthStatus;
        HttpResponse<JsonNode> healthObject;
        InfraCheck health = new InfraCheck();

        try {

            log.info("Starting healthcheck process");
            String token = health.getTicket();

            healthObject = health.pathCheck(token);

            healthStatus = healthObject
                    .getBody()
                    .getObject()
                    .getJSONObject("response")
                    .getJSONObject("request")
                    .getString("status");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Returning response based on health check");
        if ( healthStatus.equals("FAILED") ) {
            log.severe("Health check failed");
            return Response.status(500).entity(healthObject.getBody().toString()).build();
        } else {
            log.info("Health check passed");
            return Response.status(200).entity("OK").build();
        }

    }
}