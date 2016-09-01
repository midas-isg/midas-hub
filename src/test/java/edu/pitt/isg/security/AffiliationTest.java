package edu.pitt.isg.security;


import com.auth0.authentication.result.UserProfile;
import com.auth0.web.Auth0User;
import com.auth0.web.SessionUtils;
import edu.pitt.isg.MvcTest;
import edu.pitt.isg.midas.hub.affiliation.Affiliation;
import edu.pitt.isg.midas.hub.affiliation.AffiliationRepository;
import edu.pitt.isg.midas.hub.auth0.Auth0Configuration;
import edu.pitt.isg.midas.hub.auth0.Auth0LoginController;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;
import static edu.pitt.isg.security.SecurityAid.assertLoginRequired;
import static java.util.Arrays.asList;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
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
    private AffiliationRepository mockRepo;

    @Before
    public void setUp() throws Exception {
        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testWithoutLogin() throws Exception {
        assertLoginRequired(mvc.perform(get(path)));
    }

    @Test
    public void testLoginWithoutAffiliation() throws Exception {
        MockHttpSession session = toMockHttpSession(null);
        List<Affiliation> affiliates = asList(new Affiliation("A", ""), new Affiliation("B", ""));
        when(mockRepo.findAll()).thenReturn(affiliates);
        mvc.perform(get(path).with(user("aNewUser")).session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("affiliations", affiliates))
                .andExpect(view().name("terms"));
    }

    @Test
    public void testLoginWithAffiliation() throws Exception {
        MockHttpSession session = toMockHttpSession("affiliation");
        mvc.perform(get(path).with(user("aRegisteredUser")).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", endsWith(MvcTest.HOME_URL)));
    }

    private MockHttpSession toMockHttpSession(String affiliation) {
        final MockHttpSession session = new MockHttpSession();
        final Auth0User auth0User = toAuth0User(affiliation);
        session.setAttribute(SessionUtils.AUTH0_USER, auth0User);
        return session;
    }

    private Auth0User toAuth0User(String affiliation) {
        final Map<String, Object> appMetadata = new HashMap<>();
        appMetadata.put(AFFILIATION, affiliation);
        final UserProfile profile = new UserProfile(null, null, null, null, "e@mail.com",
                false, null, null, null, null,
                null, appMetadata, null);
        return new Auth0User(profile);
    }
}
