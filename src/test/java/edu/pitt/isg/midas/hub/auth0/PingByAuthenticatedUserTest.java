package edu.pitt.isg.midas.hub.auth0;


import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import static edu.pitt.isg.midas.hub.auth0.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.AUTHENTICATED_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.PUBLIC_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.SECURED_PING_URL;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class PingByAuthenticatedUserTest extends BasePingTest {
    private UserRequestPostProcessor toUser() {
        return user("authenticated");
    }

    @Test
    public void testPingUrl() throws Exception {
        SecurityAid.assertPing(mvc.perform(get(PUBLIC_PING_URL).with(toUser())));
    }

    @Test
    public void testAuthenticatedPingUrl() throws Exception {
        SecurityAid.assertAuthenticatedPing(mvc.perform(get(AUTHENTICATED_PING_URL).with(toUser())));
    }

    @Test
    public void testSecuredPingUrl() throws Exception {
        SecurityAid.assertForbidden(mvc.perform(get(SECURED_PING_URL).with(toUser())));
    }

    @Test
    public void testAdminPingUrl() throws Exception {
        SecurityAid.assertForbidden(mvc.perform(get(ADMIN_PING_URL).with(toUser())));
    }
}
