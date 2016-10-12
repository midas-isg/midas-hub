package edu.pitt.isg.midas.hub.auth0;


import com.auth0.authentication.result.UserProfile;
import com.auth0.web.Auth0User;
import com.auth0.web.SessionUtils;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityAid {
    static void assertAuthorizedSecuredPing(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you're User!"));
    }

    static void assertPing(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("All good. You don't need to be authenticated to call this"));
    }

    static void assertAuthenticatedPing(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("All good! You only get this message if you're authenticated!"));
    }

    static void assertForbidden(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isForbidden());
    }

    static void assertLoginRequired(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", endsWith("/login")));
    }

    static List<GrantedAuthority> toAuthorities(String... roles) {
        List<GrantedAuthority> list = new ArrayList<>(roles.length);
        for (String role: roles)
            list.add(new SimpleGrantedAuthority(role));
        return list;
    }

    static MockHttpSession toMockHttpSessionWithAffliation(String affiliation) {
        final MockHttpSession session = new MockHttpSession();
        final Auth0User auth0User = toAuth0User(affiliation);
        session.setAttribute(SessionUtils.AUTH0_USER, auth0User);
        return session;
    }

    static Auth0User toAuth0User(String affiliation) {
        final Map<String, Object> appMetadata = new HashMap<>();
        appMetadata.put(AFFILIATION, affiliation);
        final UserProfile profile = new UserProfile(null, null, null, null, "e@mail.com",
                false, null, null, null, null,
                null, appMetadata, null);
        return new Auth0User(profile);
    }
}
