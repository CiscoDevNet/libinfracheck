package com.cisco.devnet.infracheck;

import org.junit.*;
import static org.junit.Assert.assertEquals;

public class InfraCheckTest {

    String TICKET;
    InfraCheck health = new InfraCheck();

    @Before
    public void setUp() {
        TICKET = health.login().getBody().getObject().getJSONObject("response").getString("serviceTicket");
    }

    @Test
    public void testPathCheck() throws  Exception {
        String result = health.pathCheck(TICKET)
                .getBody()
                .getObject()
                .getJSONObject("response")
                .getJSONObject("request")
                .getString("status");

        assertEquals("FAILED", result);

    }


}
