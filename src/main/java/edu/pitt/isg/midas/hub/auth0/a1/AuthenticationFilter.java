package edu.pitt.isg.midas.hub.auth0.a1;

import com.auth0.spring.security.mvc.Auth0AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends Auth0AuthenticationFilter {
    @Autowired
    private Authenticator authenticator;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = null;
        try {
            final HttpServletRequest request = (HttpServletRequest) req;
            authentication = authenticator.authenticate(request);
            if (authentication != null)
                SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.debug("Failed to authenticate via JWT in header.", e);
            SecurityContextHolder.clearContext();
        } finally {
            if (authentication == null)
                super.doFilter(req, res, chain);
            else
                chain.doFilter(req, res);
        }
    }

    public interface Authenticator {
        Authentication authenticate(HttpServletRequest req) throws Exception;
    }
}