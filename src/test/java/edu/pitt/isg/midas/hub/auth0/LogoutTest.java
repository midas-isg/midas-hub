package edu.pitt.isg.midas.hub.auth0;


import edu.pitt.isg.midas.hub.TestApplication;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.SIGNOFF;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class LogoutTest extends TestApplication {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testLogout() throws Exception {
        mvc.perform(logout())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", SIGNOFF));
    }
}