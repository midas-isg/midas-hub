package edu.pitt.isg.midas.hub.user;

import com.auth0.authentication.result.UserIdentity;
import com.auth0.authentication.result.UserProfile;
import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0User;
import org.assertj.core.api.Condition;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

class Aid {
    static final String IDENTITIES = "identities";

    static  <T> Condition<T> forAll(Predicate<T> predicate) {
        return new Condition<>(predicate, "");
    }

    static UserIdentity newUserIdentity() {
        return new UserIdentity(null, null, null, false, null, null, null);
    }

    static Auth0UserDetails newUserDetails(List<UserIdentity> identities) {
        final UserProfile userProfile = new UserProfile(null, null, null, null, null, false, null, null, identities, null, null, null, null);
        final Auth0User user = new Auth0User(userProfile);
        return new Auth0UserDetails(user, null);
    }

    static HashMap<String, Object> newUserWithSensitiveData() {
        final HashMap<String, Object> map = newUserWithNoSensitiveData();
        map.put(IDENTITIES, "don't care (sensitive data case)");
        return map;
    }

    static HashMap<String, Object> newUserWithNoSensitiveData() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("nonSensitiveData", "don't care");
        return map;
    }

}
