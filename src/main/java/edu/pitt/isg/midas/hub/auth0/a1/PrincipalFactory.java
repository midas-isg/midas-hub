package edu.pitt.isg.midas.hub.auth0.a1;

import com.auth0.authentication.result.UserProfile;
import com.auth0.spring.security.mvc.Auth0JWTToken;
import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class PrincipalFactory {
    Auth0JWTToken make(String jwt, Map<String, Object> map) {
        final Auth0JWTToken authentication = new Auth0JWTToken(jwt);
        authentication.setAuthenticated(true);
        authentication.setPrincipal(toUserDetail(map));
        return authentication;
    }

    private Auth0UserDetails toUserDetail(Map<String, Object> map) {
        return new Auth0UserDetails(new Auth0User(toProfile(map)), null);
    }

    private UserProfile toProfile(Map<String, Object> map) {
        final String title = toString(map, "subTitle");
        final String subject = toString(map, "sub");
        return new UserProfile(subject, title, title, null, null, false, null, null, null, null, null, null, null);
    }

    private String toString(Map<String, Object> map, String key) {
        return map.get(key).toString();
    }
}