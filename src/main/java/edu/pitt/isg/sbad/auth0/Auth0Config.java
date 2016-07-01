/*package edu.pitt.isg.sbad.auth0;

import com.auth0.Auth0Filter;
import com.auth0.Auth0ServletCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySources({
        //@PropertySource("classpath:application.properties"),
        @PropertySource("classpath:auth0.properties")
})
public class Auth0Config {
    @Value(value = "${auth0.clientId}")
    private String clientId;

    @Value(value = "${auth0.clientSecret}")
    private String clientSecret;

    @Value(value = "${auth0.domain}")
    private String issuer;

    @Value(value = "${auth0.securedRoute}")
    protected String securedRoute;

    @Bean(name = "appAuthFilter")
    public FilterRegistrationBean authFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AppAuth0Filter());
        registrationBean.addInitParameter("auth0.redirect_on_authentication_error", "/login");
        registrationBean.addUrlPatterns("/");

        return registrationBean;
    }

    /*@Bean(name = "authFilter")
    public FilterRegistrationBean appAuthFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AppAuth0Filter());
        registrationBean.addUrlPatterns("/");

        return registrationBean;
    }

    @Bean(name = "redirectCallback")
    public ServletRegistrationBean redirectCallback() {
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("auth0.redirect_on_success", "/auth0");
        initParameters.put("auth0.redirect_on_error", "/login");
        initParameters.put("auth0.client_id", clientId);
        initParameters.put("auth0.client_secret", clientSecret);
        initParameters.put("auth0.domain", issuer);

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new Auth0ServletCallback());
        registrationBean.setInitParameters(initParameters);
        registrationBean.addUrlMappings("/callback");

        return registrationBean;
    }
}*/