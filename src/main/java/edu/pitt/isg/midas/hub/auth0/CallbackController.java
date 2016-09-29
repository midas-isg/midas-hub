package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0CallbackHandler;
import com.auth0.web.QueryParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.web.SessionUtils.getAuth0User;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.RETURN_TO_URL_KEY;
import static edu.pitt.isg.midas.hub.auth0.UrlAid.toRelativeUrl;

@Controller
@SessionAttributes("appUser")
class CallbackController extends Auth0CallbackHandler {
    private SsoConfig ssoConfig;

    @Autowired
    protected void setSsoConfig(final SsoConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
    }

    @RequestMapping(value = "${auth0.loginCallback}", method = RequestMethod.GET)
    protected void callback(final HttpServletRequest req, final HttpServletResponse res,
                            final Model model)
            throws ServletException, IOException {
        super.handle(req, res);
        model.addAttribute("appUser", getAuth0User(req));
    }

    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final String partnerUrl = getExternalReturnUrl(req);
        if (partnerUrl != null) {
            req.getSession().setAttribute(RETURN_TO_URL_KEY, partnerUrl);
        }
        res.sendRedirect(toRelativeUrl(req, this.redirectOnSuccess));
    }

    @Override
    protected void onFailure(HttpServletRequest req, HttpServletResponse res, Exception ex) throws ServletException, IOException {
        ex.printStackTrace();
        final String partnerUrl = getExternalReturnUrl(req);
        if (partnerUrl != null) {
            final String redirectExternalOnFailLocation = partnerUrl + "?error=callbackError";
            res.sendRedirect(redirectExternalOnFailLocation);
        } else {
            final String redirectOnFailLocation = toRelativeUrl(req, this.redirectOnFail);
            res.sendRedirect(redirectOnFailLocation);
        }
    }

    @Override
    protected boolean isValidState(final HttpServletRequest req) {
        final boolean isNonceValid = super.isValidState(req);
        final String externalReturnUrl = getExternalReturnUrl(req);
        final boolean isTrustedExternalReturnUrl = (externalReturnUrl == null) ||
                ssoConfig.getTrustedExternalReturnUrls().stream()
                        .anyMatch(trustedUrl -> externalReturnUrl.startsWith(trustedUrl));
        return isNonceValid && isTrustedExternalReturnUrl;
    }

    private String getExternalReturnUrl(final HttpServletRequest req) {
        final String stateFromRequest = req.getParameter("state");
        if (stateFromRequest == null) {
            throw new IllegalStateException("state missing in request");
        }
        return QueryParamUtils.parseFromQueryParams(stateFromRequest, RETURN_TO_URL_KEY);
    }
}
