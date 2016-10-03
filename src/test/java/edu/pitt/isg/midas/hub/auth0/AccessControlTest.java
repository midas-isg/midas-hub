package edu.pitt.isg.midas.hub.auth0;


import edu.pitt.isg.midas.hub.affiliation.AffiliationForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static edu.pitt.isg.midas.hub.auth0.SecurityAid.assertLoginRequired;
import static edu.pitt.isg.midas.hub.auth0.SecurityAid.toMockHttpSessionWithAffliation;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@EnableWebMvc
@ContextConfiguration(classes = {Auth0Configuration.class, AccessControlController.class, AccessControlTest.MockConfig.class})
public class AccessControlTest {
    private static final String affiliationName = "name";
    private static final String description = "description";
    private static final String secondaries = "additionalAffiliationNames";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private UserMetaDataRule mock;

    @Configuration
    static class MockConfig {
        @Bean
        UserMetaDataRule userMetaDataRule() {
            return Mockito.mock(UserMetaDataRule.class);
        }
    }

    @Before
    public void setUp() throws Exception {
        Mockito.reset(mock);
        mvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testAcceptTerms() throws Exception {
        assertLoginRequired(mvc.perform(post("/term-acceptance")));
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor toUser() {
        return user("authenticated");
    }

    @Test
    public void testAcceptTermsWithAuthenticatedUserAndAllofAffiliation() throws Exception {
        MockHttpSession session = toMockHttpSessionWithAffliation(null);
        final String returnToUrl = "returnToUrl";
        session.setAttribute(returnToUrl, returnToUrl);
        final MockHttpServletRequestBuilder post = post("/term-acceptance")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param(affiliationName, affiliationName)
                .param(description, description)
                .param(secondaries, secondaries)
                .with(toUser())
                .session(session);

        mvc.perform(post)
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", returnToUrl));

        ArgumentCaptor<AffiliationForm> captor = forClass(AffiliationForm.class);
        verify(mock).saveUserMetaDataAffiliationAndIsgUserRole(any(), captor.capture());
        final AffiliationForm form = captor.getValue();
        assertEquals(affiliationName, form.getName());
        assertEquals(description, form.getDescription());
        assertEquals(secondaries, form.getAdditionalAffiliationNames());
        verifyNoMoreInteractions(mock);
    }

@Test
    public void testAcceptTermsWithAuthenticatedUserAndAffiliationName() throws Exception {
        final String pathReadingFromCfgInto_Auth0CallbackHandler_redirectOnSuccess = "null";
        MockHttpSession session = toMockHttpSessionWithAffliation(null);
        final MockHttpServletRequestBuilder post = post("/term-acceptance")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param(affiliationName, affiliationName)
                .with(toUser())
                .session(session);

        mvc.perform(post)
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", pathReadingFromCfgInto_Auth0CallbackHandler_redirectOnSuccess))
        ;

        ArgumentCaptor<AffiliationForm> captor = forClass(AffiliationForm.class);
        verify(mock).saveUserMetaDataAffiliationAndIsgUserRole(any(), captor.capture());
        final AffiliationForm form = captor.getValue();
        assertEquals(affiliationName, form.getName());
        assertEquals(null, form.getDescription());
        assertEquals(null, form.getAdditionalAffiliationNames());
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testAcceptTermsWithAuthenticatedUserButNoAffiliationName() throws Exception {
        final MockHttpServletRequestBuilder post = post("/term-acceptance")
                .contentType(APPLICATION_FORM_URLENCODED)
                .with(toUser());
        mvc.perform(post)
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Organization is required!"))
                .andExpect(header().string("Location", "/tos"));
    }
}

