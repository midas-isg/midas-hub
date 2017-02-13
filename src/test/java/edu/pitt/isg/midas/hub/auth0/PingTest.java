package edu.pitt.isg.midas.hub.auth0;


import org.junit.Test;

import static edu.pitt.isg.midas.hub.auth0.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.AUTHENTICATED_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.PUBLIC_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.REPORT_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.SECURED_PING_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class PingTest extends BasePingTest {
    @Test
    public void testPingUrl() throws Exception {
        SecurityAid.assertPing(mvc.perform(get(PUBLIC_PING_URL)));
    }

    @Test
    public void testAuthenticatedPingUrl() throws Exception {
        SecurityAid.assertLoginRequired(mvc.perform(get(AUTHENTICATED_PING_URL)));
    }

    @Test
    public void testSecuredPingUrl() throws Exception {
        SecurityAid.assertLoginRequired(mvc.perform(get(SECURED_PING_URL)));
    }

    @Test
    public void testAdminPingUrl() throws Exception {
        SecurityAid.assertLoginRequired(mvc.perform(get(ADMIN_PING_URL)));
    }

    @Test
    public void testReportPingUrl() throws Exception {
        SecurityAid.assertLoginRequired(mvc.perform(get(REPORT_PING_URL)));
    }
}
