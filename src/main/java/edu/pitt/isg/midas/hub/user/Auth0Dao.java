package edu.pitt.isg.midas.hub.user;

import com.auth0.authentication.result.UserIdentity;
import com.auth0.authentication.result.UserProfile;
import com.auth0.request.internal.RequestFactory;
import com.auth0.util.JsonRequiredTypeAdapterFactory;
import com.auth0.web.Auth0User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class Auth0Dao {
    public Auth0User getAuth0User(String url, String bearerToken) {
        final HttpUrl httpUrl = HttpUrl.parse(url).newBuilder().build();
        final Gson gson = makeGson();
        final UserProfile userProfile = new RequestFactory().GET(httpUrl, new OkHttpClient(), gson, UserProfile.class)
                .addHeader("Authorization","Bearer " + bearerToken)
                .execute();
        return new Auth0User(userProfile);
    }

    private Gson makeGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new JsonRequiredTypeAdapterFactory())
                .registerTypeAdapter(UserProfile.class, new UserProfileDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
    }
}

class UserProfileDeserializer implements JsonDeserializer<UserProfile> {
    @Override
    public UserProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject() || json.isJsonNull()) {
            throw new JsonParseException("user profile json is not a valid json object");
        }

        JsonObject object = json.getAsJsonObject();
        final String id = requiredValue("user_id", String.class, object, context);
        final String name = requiredValue("name", String.class, object, context);
        final String nickname = requiredValue("nickname", String.class, object, context);
        final String picture = requiredValue("picture", String.class, object, context);

        final String email = context.deserialize(object.remove("email"), String.class);
        final String givenName = context.deserialize(object.remove("given_name"), String.class);
        final String familyName = context.deserialize(object.remove("family_name"), String.class);
        final Boolean emailVerified = object.has("email_verified") ? context.<Boolean>deserialize(object.remove("email_verified"), Boolean.class) : false;
        final Date createdAt = context.deserialize(object.remove("created_at"), Date.class);

        final Type identitiesType = new TypeToken<List<UserIdentity>>(){}.getType();
        final List<UserIdentity> identities = context.deserialize(object.remove("identities"), identitiesType);

        final Type metadataType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> userMetadata = context.deserialize(object.remove("user_metadata"), metadataType);
        Map<String, Object> appMetadata = context.deserialize(object.remove("app_metadata"), metadataType);
        Map<String, Object> extraInfo = context.deserialize(object, metadataType);
        return new UserProfile(id, name, nickname, picture, email, emailVerified, familyName, createdAt, identities, extraInfo, userMetadata, appMetadata, givenName);
    }

    private <T> T requiredValue(String name, Type type, JsonObject object, JsonDeserializationContext context) throws JsonParseException {
        T value = context.deserialize(object.remove(name), type);
        if (value == null) {
            throw new JsonParseException(String.format("Missing required attribute %s", name));
        }
        return value;
    }
}