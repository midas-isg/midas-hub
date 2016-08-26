package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0CallbackHandler;
import com.auth0.web.QueryParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
class CallbackController extends Auth0CallbackHandler {
    //private SsoConfig ssoConfig;

    /*@Autowired
    protected void setSsoConfig(final SsoConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
    }*/

    @RequestMapping(value = "${auth0.loginCallback}", method = RequestMethod.GET)
    protected void callback(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        super.handle(req, res);
    }

    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final String externalReturnUrl = getExternalReturnUrl(req);
        if (externalReturnUrl != null) {
            // redirect back to partner site
            res.sendRedirect(externalReturnUrl);
        } else {
            // redirect back to "callback success" location of this app
            res.sendRedirect(req.getContextPath() + this.redirectOnSuccess);
        }
    }

    @Override
    protected void onFailure(HttpServletRequest req, HttpServletResponse res, Exception ex) throws ServletException, IOException {
        ex.printStackTrace();
        final String externalReturnUrl = getExternalReturnUrl(req);
        if (externalReturnUrl != null) {
            // redirect back to partner site
            final String redirectExternalOnFailLocation = externalReturnUrl + "?error=callbackError";
            res.sendRedirect(redirectExternalOnFailLocation);
        } else {
            // redirect back to "callback failure" location of this app
            final String redirectOnFailLocation = req.getContextPath() + this.redirectOnFail;
            res.sendRedirect(redirectOnFailLocation);
        }
    }

    @Override
    protected boolean isValidState(final HttpServletRequest req) {
        final boolean isNonceValid = super.isValidState(req);
        final String externalReturnUrl = getExternalReturnUrl(req);
        final boolean isTrustedExternalReturnUrl = (externalReturnUrl == null) /*||
                ssoConfig.getTrustedExternalReturnUrls().contains(externalReturnUrl)*/;
        return isNonceValid && isTrustedExternalReturnUrl;
    }

    private String getExternalReturnUrl(final HttpServletRequest req) {
        final String stateFromRequest = req.getParameter("state");
        if (stateFromRequest == null) {
            throw new IllegalStateException("state missing in request");
        }
        return QueryParamUtils.parseFromQueryParams(stateFromRequest, "externalReturnUrl");
    }
}
