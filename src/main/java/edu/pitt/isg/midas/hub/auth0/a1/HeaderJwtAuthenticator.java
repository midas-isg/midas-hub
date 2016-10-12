package edu.pitt.isg.midas.hub.auth0.a1;

import com.auth0.spring.security.mvc.Auth0JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** See specification at https://tools.ietf.org/html/rfc6750 */
@Service
class HeaderJwtAuthenticator implements AuthenticationFilter.Authenticator {
    private static final String BEARER = "Bearer";
    private static final String AUTHORIZATION = "Authorization";
    private static final int SEGMENTS = 2;
    @Autowired
    private JwtVerifierFactory jwtVerifierFactory;
    @Autowired
    private PrincipalFactory principalFactory;

    @Override
    public Auth0JWTToken authenticate(HttpServletRequest request) throws Exception {
        final String jwt = extractJwt(request.getHeader(AUTHORIZATION));
        final Map<String, Object> map = jwtVerifierFactory.make().verify(jwt);
        return principalFactory.make(jwt, map);
    }

    private String extractJwt(String credentialsValue) {
        final String[] credentials = credentialsValue.split(" ");
        guardNumberOfSegments(credentials);
        guardSchema(credentials[0]);
        return credentials[1];
    }

    private void guardNumberOfSegments(String[] credentials) {
        if (credentials.length != SEGMENTS)
            throw new RuntimeException("Expected " + SEGMENTS + " for segments, not " + credentials.length);
    }

    private void guardSchema(String schema) {
        if (! schema.equalsIgnoreCase(BEARER))
            throw new RuntimeException("Expected " + BEARER + " for authentication schema, not " + schema);
    }
}