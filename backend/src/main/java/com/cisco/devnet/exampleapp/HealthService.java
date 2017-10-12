package com.cisco.devnet.exampleapp;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import com.cisco.devnet.infracheck.InfraCheck;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

@Path("/")
public class HealthService {
    @GET
    @Path("/health")
    public Response getHealth(@PathParam("param") String message) {

        String healthStatus;
        HttpResponse<JsonNode> healthObject;
        InfraCheck health = new InfraCheck();

        try {

            String token = health.login().getBody().getObject().getJSONObject("response").getString("serviceTicket");

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

        if ( healthStatus.equals("FAILED") ) {
            return Response.status(500).entity(healthObject.getBody().toString()).build();
        } else {
            return Response.status(200).entity("OK").build();
        }

    }
}