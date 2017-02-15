package edu.pitt.isg.midas.hub.auth0;


import edu.pitt.isg.midas.hub.MvcTest;
import edu.pitt.isg.midas.hub.affiliation.Affiliation;
import edu.pitt.isg.midas.hub.affiliation.AffiliationRepository;
import edu.pitt.isg.midas.hub.auth0.a1.AuthenticationFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Auth0Configuration.class, Auth0LoginController.class})
public class AffiliationTest {
    @Value("${auth0.loginRedirectOnSuccess}")
    private String path;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @MockBean
    private AuthorityRule mockAuthorityRule;
    @MockBean
    private AffiliationRepository mockRepo;
    @MockBean
    private AuthenticationFilter.Authenticator authenticator;

    @Before
    public void setUp() throws Exception {
        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        when(authenticator.authenticate(any())).thenReturn(null);
    }

    @Test
    public void testWithoutLogin() throws Exception {
        SecurityAid.assertLoginRequired(mvc.perform(get(path)));
    }

    @Test
    public void testLoginWithoutAffiliation() throws Exception {
        MockHttpSession session = SecurityAid.toMockHttpSessionWithAffliation(null);
        List<Affiliation> affiliates = asList(new Affiliation("A", ""), new Affiliation("B", ""));
        when(mockRepo.findAll()).thenReturn(affiliates);
        mvc.perform(get(path).with(user("aNewUser")).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/tos"));
    }

    @Test
    public void testLoginWithAffiliation() throws Exception {
        MockHttpSession session = SecurityAid.toMockHttpSessionWithAffliation("affiliation");
        mvc.perform(get(path).with(user("aRegisteredUser")).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));
    }
}