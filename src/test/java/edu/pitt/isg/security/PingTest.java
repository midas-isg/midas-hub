package edu.pitt.isg.security;


import org.junit.Test;

import static edu.pitt.isg.midas.hub.controllers.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.AUTHENTICATED_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.PUBLIC_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.SECURED_PING_URL;
import static edu.pitt.isg.security.SecurityAid.assertLoginRequired;
import static edu.pitt.isg.security.SecurityAid.assertPing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class PingTest extends BasePingTest {
    @Test
    public void testPingUrl() throws Exception {
        assertPing(mvc.perform(get(PUBLIC_PING_URL)));
    }

    @Test
    public void testAuthenticatedPingUrl() throws Exception {
        assertLoginRequired(mvc.perform(get(AUTHENTICATED_PING_URL)));
    }

    @Test
    public void testSecuredPingUrl() throws Exception {
        assertLoginRequired(mvc.perform(get(SECURED_PING_URL)));
    }

    @Test
    public void testAdminPingUrl() throws Exception {
        assertLoginRequired(mvc.perform(get(ADMIN_PING_URL)));
    }
}
