package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0Config;
import com.auth0.web.NonceUtils;
import com.auth0.web.QueryParamUtils;
import com.auth0.web.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.RETURN_TO_URL_KEY;

@Controller
public class SsoLoginController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Auth0Config auth0Config;
    private SsoConfig ssoConfig;

    @Autowired
    public SsoLoginController(Auth0Config auth0Config, SsoConfig ssoConfig) {
        this.auth0Config = auth0Config;
        this.ssoConfig = ssoConfig;
    }

    @RequestMapping(value="/sso", method = RequestMethod.GET)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected String partnerLogin(final Map<String, Object> model, final HttpServletRequest req){
        try {
            return doPartnerLogin(model, req);
        } catch (IllegalArgumentException e){
            logger.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

    @RequestMapping(value="/token", method = RequestMethod.POST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected Object postToken(final Map<String, Object> model, final HttpServletRequest req){
        try {
            logger.error("Request Cookies: " + Arrays.toString(req.getCookies()));
            logger.error("Model: " + model.toString());
            Map<String, Object> map = new HashMap<>();
            map.put("access_token", "RsT5OjbzRn430zqMLgV3Ia");
            map.put("expires_in", 3600);
            return map;
        } catch (IllegalArgumentException e){
            logger.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

    private String doPartnerLogin(Map<String, Object> model, HttpServletRequest req) {
        logger.debug("Performing SSO login");
        detectError(model);
        manageExternalReturnUrl(req);
        NonceUtils.addNonceToStorage(req);
        fillModelToBePresented(model, req);
        return "ssoLogin";
    }

    private void manageExternalReturnUrl(HttpServletRequest req) {
        final String externalReturnUrl = req.getParameter(RETURN_TO_URL_KEY);
        validateExternalReturnUrl(externalReturnUrl);
        updateStateInSession(req, externalReturnUrl);
    }

    private void validateExternalReturnUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException("Missing required external return URL query param.");
        } else  if (!isTrustedExternalReturnUrl(url)) {
            throw new IllegalArgumentException("Cannot redirect to untrusted URL: " + url);
        }
    }

    private void updateStateInSession(HttpServletRequest req, String externalReturnUrl) {
        final String previousState = toStateOrElseEmptyString(req);
        final String updatedState = updateExternalReturnUrlInState(previousState, externalReturnUrl);
        SessionUtils.setState(req, updatedState);
    }

    private String updateExternalReturnUrlInState(String previousState, String newUrl) {
        return QueryParamUtils.addOrReplaceInQueryParams(previousState, RETURN_TO_URL_KEY, newUrl);
    }

    private String toStateOrElseEmptyString(HttpServletRequest req) {
        final String state = SessionUtils.getState(req);
        return (state != null) ? state : "";
    }

    private void fillModelToBePresented(final Map<String, Object> model, final HttpServletRequest req) {
        model.put("user", SessionUtils.getAuth0User(req));
        model.put("domain", auth0Config.getDomain());
        model.put("clientId", auth0Config.getClientId());
        model.put("loginCallback", UrlAid.toAbsoluteUrl(req, auth0Config.getLoginCallback()
                .replaceFirst("/", "")
        ));
        model.put("state", SessionUtils.getState(req));
        model.put("returnUrl", req.getParameter("returnUrl"));
        model.put("returnTitle", req.getParameter("returnTitle"));
        putParameterIfNotNull(model, req, "title");
        putParameterIfNotNull(model, req, "message");
    }

    private void putParameterIfNotNull(Map<String, Object> model, HttpServletRequest req, String name) {
        final String title = req.getParameter(name);
        if (title != null)
            model.put(name, title);
    }

    private void detectError(final Map<String, Object> model) {
        model.put("error", model.get("error") != null);
    }

    private boolean isTrustedExternalReturnUrl(final String url) {
        return ssoConfig.getTrustedExternalReturnUrls().stream()
                .anyMatch(trustedUrl -> url.startsWith(trustedUrl));
    }
}
