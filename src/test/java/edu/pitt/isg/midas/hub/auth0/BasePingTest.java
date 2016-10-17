package edu.pitt.isg.midas.hub.auth0;

import edu.pitt.isg.midas.hub.Application;
import edu.pitt.isg.midas.hub.auth0.Auth0Configuration;
import edu.pitt.isg.midas.hub.auth0.PingController;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class})
public abstract class BasePingTest {
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
