package edu.pitt.isg.midas.hub.auth0.a1;

import com.auth0.jwt.JWTVerifier;
import com.auth0.web.Auth0Config;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class JwtVerifierFactory {
    @Autowired
    private Auth0Config auth0Config;

    JWTVerifier make() {
        final String clientSecret = auth0Config.getClientSecret();
        final String clientId = auth0Config.getClientId();
        return new JWTVerifier(Base64.decodeBase64(clientSecret), clientId, clientId);
    }
}