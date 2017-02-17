package edu.pitt.isg.midas.hub.log;

import edu.pitt.isg.midas.hub.user.AppMetadataAid;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.APP_METADATA;
import static java.util.stream.Collectors.toMap;

@Component
class Factory {
    Map<String, String> makeApplicationId2Name(List<HashMap<String, ?>> apps){
        return apps.stream().collect(
                toMap(this::toApplicationId, this::toName)
        );
    }

    Map<String, String> makeUserId2AffiliationName(List<HashMap<String, ?>> users){
        return users.stream().collect(
                toMap(this::toUserId, this::toAffiliationName)
        );
    }

    Map<String, String> makeUserId2Name(List<HashMap<String, ?>> users){
        return users.stream().collect(
                toMap(this::toUserId, this::toName)
        );
    }

    private String toApplicationId(HashMap<String, ?> app) {
        return Objects.toString(app.get("client_id"));
    }

    private String toName(HashMap<String, ?> map) {
        return Objects.toString(map.get("name"));
    }

    private String toUserId(HashMap<String, ?> user) {
        return Objects.toString(user.get("user_id"));
    }

    private String toAffiliationName(HashMap<String, ?> user) {
        return Objects.toString(toNullableAffiliationName(user));
    }

    private String toNullableAffiliationName(HashMap<String, ?> user) {
        final Object appMetadata = user.get(APP_METADATA);
        if (appMetadata == null)
            return null;
        @SuppressWarnings("unchecked")
        final Map<String, ?> map = (Map<String, ?>) appMetadata;
        return AppMetadataAid.toAffiliationName(map);
    }
}

