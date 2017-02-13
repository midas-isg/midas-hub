package edu.pitt.isg.midas.hub.auth0;


import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import static edu.pitt.isg.midas.hub.auth0.PingController.ADMIN_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PingController.REPORT_PING_URL;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ACCOUNTS_APP_ADMIN;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ACCOUNTS_DIRECTOR;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;
import static edu.pitt.isg.midas.hub.auth0.SecurityAid.toAuthorities;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PingReportByAuthorizedUsersTest extends BasePingTest {
    private UserRequestPostProcessor asDirector() {
        return user("director").authorities(toAuthorities(ISG_USER, ACCOUNTS_DIRECTOR));
    }

    @Test
    public void testAdminPingUrlAsDirector() throws Exception {
        SecurityAid.assertForbidden(mvc.perform(get(ADMIN_PING_URL).with(asDirector())));
    }

    @Test
    public void testReportPingUrlAsDirector() throws Exception {
        mvc.perform(get(REPORT_PING_URL).with(asDirector()))
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you can view log reports!"));

    }

    private UserRequestPostProcessor asAppAdmin() {
        return user("app admin").authorities(toAuthorities(ISG_USER, ACCOUNTS_APP_ADMIN));
    }

    @Test
    public void testAdminPingUrlAppAdmin() throws Exception {
        SecurityAid.assertForbidden(mvc.perform(get(ADMIN_PING_URL).with(asAppAdmin())));
    }

    @Test
    public void testReportPingUrlAppAdmin() throws Exception {
        mvc.perform(get(REPORT_PING_URL).with(asAppAdmin()))
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you can view log reports!"));

    }
}
