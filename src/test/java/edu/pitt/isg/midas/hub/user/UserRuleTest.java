package edu.pitt.isg.midas.hub.user;

import com.auth0.authentication.result.UserIdentity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.pitt.isg.midas.hub.user.Aid.IDENTITIES;
import static edu.pitt.isg.midas.hub.user.Aid.forAll;
import static edu.pitt.isg.midas.hub.user.Aid.newUserDetails;
import static edu.pitt.isg.midas.hub.user.Aid.newUserIdentity;
import static edu.pitt.isg.midas.hub.user.Aid.newUserWithNoSensitiveData;
import static edu.pitt.isg.midas.hub.user.Aid.newUserWithSensitiveData;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserRuleTest {
    private final UserRule rule = new UserRule();

    @Test
    public void testFilterOutSensitiveData() throws Exception {
        final List<HashMap<String, ?>> users = list();
        rule.filterOutSensitiveData(users);
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).are(forAll(map -> !map.containsKey(IDENTITIES) && !map.isEmpty()));
    }

    @Test
    public void authorizeNullIdentities() throws Exception {
        rule.guardAuthorization("don't care: null identities case", newUserDetails(null));
    }

    @Test
    public void deny() throws Exception {
        final List<UserIdentity> identities = asList(newUserIdentity());
        final String requestedUserId = "id2";
        assertThatThrownBy(() -> rule.guardAuthorization(requestedUserId, newUserDetails(identities)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(null + " is not allowed to access profile of user " + requestedUserId);
    }

    private List<HashMap<String, ?>> list() {
        final List<HashMap<String, ?>> users = new ArrayList<>();
        users.add(newUserWithSensitiveData());
        users.add(newUserWithNoSensitiveData());
        return users;
    }
}