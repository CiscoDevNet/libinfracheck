package com.cisco.devnet.infracheck;

import org.junit.*;
import static org.junit.Assert.assertEquals;

public class InfraCheckTest {

    String TICKET;
    InfraCheck health = new InfraCheck();

    @Before
    public void setUp() {
        TICKET = String.valueOf(health.getTicket());
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

    @Test
    public void testOptionalPathCheck() throws  Exception {
        String result = health.pathCheck(TICKET, "7ce5d5e4-98f3-4ae3-ba90-b3d81502fb90")
                .getBody()
                .getObject()
                .getJSONObject("response")
                .getJSONObject("request")
                .getString("status");

        assertEquals("FAILED", result);

    }


}
