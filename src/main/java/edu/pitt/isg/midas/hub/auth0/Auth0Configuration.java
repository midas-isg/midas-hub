package edu.pitt.isg.midas.hub.auth0;

import com.auth0.spring.security.mvc.Auth0SecurityConfig;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_ADMIN;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;


@Configuration
@ComponentScan(basePackages = {"com.auth0"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class Auth0Configuration extends Auth0SecurityConfig {
    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/public/**", "/webjars/**", "/logoutFromAuth0", "/callback", "/sso").permitAll()
                .antMatchers("/secured/**").hasAnyAuthority(ISG_USER)
                .antMatchers("/admin/**").hasAnyAuthority(ISG_ADMIN)
                .antMatchers(securedRoute).authenticated()
        .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler())
        .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleUrlLogoutSuccessHandler(){
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                setDefaultTargetUrl("/logoutFromAuth0");
                super.handle(request, response, authentication);
            }
        };
    }
}