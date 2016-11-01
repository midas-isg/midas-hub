package edu.pitt.isg.midas.hub.user;

import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0Config;
import com.auth0.web.Auth0User;
import edu.pitt.isg.midas.hub.affiliation.AffiliationForm;
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
import static org.springframework.http.HttpMethod.PATCH;

@Service
public class UserRule {
    private static final String ROLES = "roles";
    private static final String APP_METADATA = "app_metadata";
    @Value("${app.auth0.secret.token}")
    private String bearerToken;
    @Autowired
    private Auth0Config auth0Config;
    @Autowired
    private Auth0Dao dao;

    public void guardAuthorization(String requestedUserId, Auth0UserDetails userDetails) {
        if (userDetails.getIdentities() != null){
            final String userId = userDetails.getUserId();
            if (! requestedUserId.equals(userId))
                throw new RuntimeException(userId + " is not allowed to access profile of user " + requestedUserId);
        }
    }

    public Auth0User getUserProfileByUserId(String userId){
        final String url = toAuth0UserUrl(auth0Config.getDomain(), userId);
        return dao.getAuth0User(url, bearerToken);
    }

    public void saveUserMetaDataAffiliationAndIsgUserRole(Auth0User user, AffiliationForm form) {
        final Map<String, Object> appMetadata = user.getAppMetadata();
        putAffiliation(appMetadata, form);
        addRole(appMetadata, ISG_USER);
        addAllowedServices(appMetadata);
        Map<String, Object> userProfile = toUserProfileMap(appMetadata);
        dao.saveUserMetaDataAffiliationAndIsgUserRole(user, userProfile);
    }


    private void addAllowedServices(Map<String, Object> appMetadata) {
        final List<Object> services =  new ArrayList<>();
        Map<String, String> service = new HashMap<>();

        service.put("d", "UPitt");
        service.put("n", "SEIR");
        service.put("v", "3.0");
        services.add(service);

        service = new HashMap<>();
        service.put("d", "UPitt,PSC,CMU");
        service.put("n", "FRED");
        service.put("v", "2.0.1_i");
        services.add(service);

        appMetadata.put(ALLOWED_SERVICES, services);
    }
}
