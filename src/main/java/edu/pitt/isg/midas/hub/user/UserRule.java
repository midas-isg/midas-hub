package edu.pitt.isg.midas.hub.user;

import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0Config;
import com.auth0.web.Auth0User;
import edu.pitt.isg.midas.hub.affiliation.AffiliationForm;
import edu.pitt.isg.midas.hub.auth0.Auth0Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;
import static edu.pitt.isg.midas.hub.user.AppMetadataAid.addAllowedServices;
import static edu.pitt.isg.midas.hub.user.AppMetadataAid.addRole;
import static edu.pitt.isg.midas.hub.user.AppMetadataAid.addAffiliation;
import static edu.pitt.isg.midas.hub.user.AppMetadataAid.toUserProfileMap;

@Service
public class UserRule {
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
        return dao.getAuth0User(userId);
    }

    public void saveUserMetaDataAffiliationAndIsgUserRole(Auth0User user, AffiliationForm form) {
        final Map<String, Object> appMetadata = user.getAppMetadata();
        addAffiliation(appMetadata, form);
        addRole(appMetadata, ISG_USER);
        addAllowedServices(appMetadata);
        Map<String, Object> userProfile = toUserProfileMap(appMetadata);
        dao.saveUserMetaDataAffiliationAndIsgUserRole(user, userProfile);
    }
}
