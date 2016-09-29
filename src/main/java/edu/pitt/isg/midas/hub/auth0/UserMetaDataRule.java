package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0Config;
import com.auth0.web.Auth0User;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;
import static edu.pitt.isg.midas.hub.auth0.UrlAid.toAuth0UserUrl;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpMethod.PATCH;

@Service
public class UserMetaDataRule {
    private static final String ROLES = "roles";
    private static final String APP_METADATA = "app_metadata";
    @Value("${app.auth0.secret.token}")
    private String bearerToken;
    @Autowired
    private Auth0Config auth0Config;

    Void saveUserMetaDataAffiliationAndIsgUserRole(Auth0User user, String affiliationName) {
        final Map<String, Object> appMetadata = user.getAppMetadata();
        saveAffiliation(appMetadata, affiliationName);
        addRole(appMetadata, ISG_USER);
        Map<String, Object> userProfile = toUserProfileMap(appMetadata);
        final HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(userProfile, toHttpHeaders());
        final String url = toAuth0UserUrl(auth0Config.getDomain(), user.getUserId());
        final ResponseEntity<HashMap> exchange = toRestTemplate().exchange(url, PATCH, requestEntity, HashMap.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            updateAuth0UserToGainNewAuthorities(user, exchange);
        }
        return null;
    }

    private void updateAuth0UserToGainNewAuthorities(Auth0User user, ResponseEntity<HashMap> exchange) {
        @SuppressWarnings("unchecked")
        final Map<String, Object> body = (Map<String, Object>)exchange.getBody();
        @SuppressWarnings("unchecked")
        final Map<String, Object> appMetadata = (Map<String, Object>) body.get(APP_METADATA);
        user.getAppMetadata().putAll(appMetadata);

        final List<String> roles = user.getRoles();
        roles.clear();
        @SuppressWarnings("unchecked")
        final List<String>list = (List<String>) appMetadata.get(ROLES);
        roles.addAll(list);
    }

    private RestTemplate toRestTemplate() {
        final RestTemplate rest = new RestTemplate();
        final HttpClient httpClient = HttpClients.createDefault();
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        return rest;
    }

    private Map<String, Object> toUserProfileMap(Map<String, Object> appMetadata) {
        final Map<String, Object> userProfile = new HashMap<>();
        userProfile.put(APP_METADATA, appMetadata);
        return userProfile;
    }

    private HttpHeaders toHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String bearer = "Bearer " + bearerToken;
        headers.set("Authorization", bearer);
        return headers;
    }

    private void saveAffiliation(Map<String, Object> appMetadata, String orgName) {
        appMetadata.put(AFFILIATION, singletonMap("name", orgName));
    }

    @SuppressWarnings("unchecked")
    private void addRole(Map<String, Object> appMetadata, String role) {
        final Set<String> roles =  new TreeSet<>();
        final Object obj = appMetadata.get(ROLES);
        if (obj != null)
            roles.addAll((List<String>) obj);
        roles.add(role);
        appMetadata.put(ROLES, roles);
    }
}