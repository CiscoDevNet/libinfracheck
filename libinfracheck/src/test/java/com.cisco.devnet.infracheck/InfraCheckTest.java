package com.cisco.devnet.infracheck;

import com.cisco.devnet.infracheck.InfraCheck;
import junit.framework.TestCase;


public class InfraCheckTest extends TestCase {

    String TICKET;

    protected void setUp() {
        InfraCheck health = new InfraCheck();
        TICKET = health.login().getBody().getObject().getJSONObject("response").getString("serviceTicket");
    }

    public void testPathCheck() throws  Exception {
        InfraCheck health = new InfraCheck();
        String result = health.pathCheck(TICKET)
                .getBody()
                .getObject()
                .getJSONObject("response")
                .getString("status");

        assertEquals(result, "FAILED");
//        if (result == "FAILED") {
//            return "{\"message\": \"failed\"}";
//        }

    }


}
