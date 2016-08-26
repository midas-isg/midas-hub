package edu.pitt.isg.security;


import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_ADMIN;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;
import static edu.pitt.isg.midas.hub.controllers.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.AUTHENTICATED_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.PUBLIC_PING_URL;
import static edu.pitt.isg.midas.hub.controllers.PingController.SECURED_PING_URL;
import static edu.pitt.isg.security.SecurityAid.assertAuthenticatedPing;
import static edu.pitt.isg.security.SecurityAid.assertAuthorizedSecuredPing;
import static edu.pitt.isg.security.SecurityAid.assertPing;
import static edu.pitt.isg.security.SecurityAid.toAuthorities;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PingByIsgAdminUserTest extends BasePingTest {
    private UserRequestPostProcessor asUser() {
        return user("admin").authorities(toAuthorities(ISG_USER, ISG_ADMIN));
    }

    @Test
    public void testPingUrl() throws Exception {
        assertPing(mvc.perform(get(PUBLIC_PING_URL).with(asUser())));
    }

    @Test
    public void testAuthenticatedPingUrl() throws Exception {
        assertAuthenticatedPing(mvc.perform(get(AUTHENTICATED_PING_URL).with(asUser())));
    }

    @Test
    public void testSecuredPingUrl() throws Exception {
        assertAuthorizedSecuredPing(mvc.perform(get(SECURED_PING_URL).with(asUser())));
    }

    @Test
    public void testAdminPingUrl() throws Exception {
        mvc.perform(get(ADMIN_PING_URL).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you're Admin!"));
    }
}
