package edu.pitt.isg.security;


import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import static edu.pitt.isg.midas.hub.controllers.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.AUTHENTICATED_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.PUBLIC_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.SECURED_PING_URL;
import static edu.pitt.isg.security.SecurityAid.assertAuthenticatedPing;
import static edu.pitt.isg.security.SecurityAid.assertForbidden;
import static edu.pitt.isg.security.SecurityAid.assertPing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class PingByAuthenticatedUserTest extends BasePingTest {
    private UserRequestPostProcessor toUser() {
        return user("authenticated");
    }

    @Test
    public void testPingUrl() throws Exception {
        assertPing(mvc.perform(get(PUBLIC_PING_URL).with(toUser())));
    }

    @Test
    public void testAuthenticatedPingUrl() throws Exception {
        assertAuthenticatedPing(mvc.perform(get(AUTHENTICATED_PING_URL).with(toUser())));
    }

    @Test
    public void testSecuredPingUrl() throws Exception {
        assertForbidden(mvc.perform(get(SECURED_PING_URL).with(toUser())));
    }

    @Test
    public void testAdminPingUrl() throws Exception {
        assertForbidden(mvc.perform(get(ADMIN_PING_URL).with(toUser())));
    }
}
