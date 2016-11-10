package edu.pitt.isg.midas.hub.user;

import edu.pitt.isg.midas.hub.affiliation.AffiliationForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.APP_METADATA;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ROLES;

public class AppMetadataAid {
    private static final String ALLOWED_SERVICES = "allowedServices";

    private AppMetadataAid(){
    }

    static void addAffiliation(Map<String, Object> appMetadata, AffiliationForm form) {
        appMetadata.put(AFFILIATION, toAffiliationMap(form));
    }

    @SuppressWarnings("unchecked")
    static void addRole(Map<String, Object> appMetadata, String role) {
        final Set<String> roles =  new TreeSet<>();
        final Object obj = appMetadata.get(ROLES);
        if (obj != null)
            roles.addAll((List<String>) obj);
        roles.add(role);
        appMetadata.put(ROLES, roles);
    }


    static void addAllowedServices(Map<String, Object> appMetadata) {
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

    static Map<String, Object> toUserProfileMap(Map<String, Object> appMetadata) {
        final Map<String, Object> userProfile = new HashMap<>();
        userProfile.put(APP_METADATA, appMetadata);
        return userProfile;
    }

    static private Map<String, Object> toAffiliationMap(AffiliationForm form) {
        Map<String, Object> map = new HashMap<>();
        final String name = form.getName();
        map.put("name", name);
        final String description = name.equalsIgnoreCase("other")
                ? ""
                : form.getDescription();
        map.put("description", description);
        map.put("groups", form.getAffiliationGroups());
        map.put("secondaries", form.getAdditionalAffiliationNames());
        return map;
    }

    public static String toAffiliationName(Map<String, ?> appMetadataMap) {
        final Object affiliation = appMetadataMap.get("affiliation");
        if (affiliation == null)
            return null;
        final Map<String, ?> affiliationMap = (Map<String, ?>) affiliation;
        return Objects.toString(affiliationMap.get("name"));
    }
}