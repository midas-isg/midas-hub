package edu.pitt.isg.midas.hub.auth0;

import edu.pitt.isg.midas.hub.TestApplication;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


public abstract class BasePingTest extends TestApplication {
    protected MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
}
