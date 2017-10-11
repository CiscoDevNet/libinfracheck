package com.cisco.devnet.exampleapp;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import com.cisco.devnet.infracheck.InfraCheck;

@Path("/")
public class HealthService {
    @GET
    @Path("/{param}")
    public Response getMessage(@PathParam("param") String message) {
        String output = "Jersey says " + message;
        InfraCheck health = new InfraCheck();
        try {
            output = health.getHealth().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Response.status(200).entity(output).build();
    }
}