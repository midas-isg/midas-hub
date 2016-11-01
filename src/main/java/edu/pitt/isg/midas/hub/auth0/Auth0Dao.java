package edu.pitt.isg.midas.hub.auth0;

import com.auth0.authentication.result.UserProfile;
import com.auth0.request.internal.RequestFactory;
import com.auth0.web.Auth0Config;
import com.auth0.web.Auth0User;
import com.google.gson.Gson;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
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

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.APP_METADATA;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ROLES;
import static edu.pitt.isg.midas.hub.auth0.UrlAid.toAuth0UserUrl;
import static org.springframework.http.HttpMethod.PATCH;

@Service
public class Auth0Dao {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_SCHEME = "Bearer ";

    @Value("${app.auth0.secret.token}")
    private String bearerToken;
    @Autowired
    private Auth0Config auth0Config;

    public Auth0User getAuth0User(String userId) {
        final String url = toAuth0UserUrl(auth0Config.getDomain(), userId);
        return getAuth0User(bearerToken, url);
    }

    private Auth0User getAuth0User(String bearerToken, String url) {
        final HttpUrl httpUrl = HttpUrl.parse(url).newBuilder().build();
        final Gson gson = UserProfileDeserializer.makeGson();
        final UserProfile userProfile = new RequestFactory().GET(httpUrl, new OkHttpClient(), gson, UserProfile.class)
                .addHeader(AUTHORIZATION, BEARER_SCHEME + bearerToken)
                .execute();
        return new Auth0User(userProfile);
    }

    public void saveUserMetaDataAffiliationAndIsgUserRole(Auth0User user, Map<String, Object> userProfile) {
        final HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(userProfile, toHttpHeaders(bearerToken));
        final String url = toAuth0UserUrl(auth0Config.getDomain(), user.getUserId());
        final ResponseEntity<HashMap> exchange = toRestTemplate().exchange(url, PATCH, requestEntity, HashMap.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            updateAuth0UserToGainNewAuthorities(user, exchange);
        } else {
            throw new RuntimeException("Failed to save user metadata: " + exchange.getStatusCode());
        }
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

    private HttpHeaders toHttpHeaders(String bearerToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final String bearer = BEARER_SCHEME + bearerToken;
        headers.set(AUTHORIZATION, bearer);
        return headers;
    }
}