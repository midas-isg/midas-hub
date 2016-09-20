package edu.pitt.isg.midas.hub.auth0;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Holds the configuration specific to this SSO app
 * Taken from properties files
 *
 * See sso.properties file
 */
@Component
@Configuration
@ConfigurationProperties("sso")
@PropertySources({@PropertySource("classpath:sso.properties")})
public class SsoConfig {
    @Value(value = "${sso.logoutEndpoint}")
    protected String logoutEndpoint;

    @Value("#{'${sso.trustedExternalReturnUrls}'.split(',')}")
    protected List<String> trustedExternalReturnUrls;


    public String getLogoutEndpoint() {
        return logoutEndpoint;
    }

    public List<String> getTrustedExternalReturnUrls() {
        return trustedExternalReturnUrls;
    }
}
