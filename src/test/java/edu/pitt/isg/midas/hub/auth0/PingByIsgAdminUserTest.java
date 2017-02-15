package edu.pitt.isg.midas.hub.auth0;


import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import static edu.pitt.isg.midas.hub.auth0.PingController.REPORT_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_ADMIN;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;
import static edu.pitt.isg.midas.hub.auth0.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.AUTHENTICATED_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.PUBLIC_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.SECURED_PING_URL;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PingByIsgAdminUserTest extends BasePingTest {
    private UserRequestPostProcessor asUser() {
        return user("admin").authorities(SecurityAid.toAuthorities(ISG_USER, ISG_ADMIN));
    }

    @Test
    public void testPingUrl() throws Exception {
        SecurityAid.assertPing(mvc.perform(get(PUBLIC_PING_URL).with(asUser())));
    }

    @Test
    public void testAuthenticatedPingUrl() throws Exception {
        SecurityAid.assertAuthenticatedPing(mvc.perform(get(AUTHENTICATED_PING_URL).with(asUser())));
    }

    @Test
    public void testSecuredPingUrl() throws Exception {
        SecurityAid.assertAuthorizedSecuredPing(mvc.perform(get(SECURED_PING_URL).with(asUser())));
    }

    @Test
    public void testAdminPingUrl() throws Exception {
        mvc.perform(get(ADMIN_PING_URL).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you're Admin!"));
    }

    @Test
    public void testReportPingUrl() throws Exception {
        mvc.perform(get(REPORT_PING_URL).with(asUser()))
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you can view log reports!"));

    }
}
