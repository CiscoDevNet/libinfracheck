package com.cisco.devnet.infracheck;

import com.cisco.devnet.infracheck.InfraCheck;
import junit.framework.TestCase;


public class InfraCheckTest extends TestCase {

    String TICKET;
    InfraCheck health = new InfraCheck();

    protected void setUp() {
        TICKET = health.login().getBody().getObject().getJSONObject("response").getString("serviceTicket");
    }

    public void testPathCheck() throws  Exception {
        String result = health.pathCheck(TICKET)
                .getBody()
                .getObject()
                .getJSONObject("response")
                .getJSONObject("request")
                .getString("status");

        assertEquals("FAILED", result);
//        if (result == "FAILED") {
//            return "{\"message\": \"failed\"}";
//        }

    }


}
