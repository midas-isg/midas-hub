package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0CallbackHandler;
import com.auth0.web.Auth0User;
import com.auth0.web.QueryParamUtils;
import com.auth0.web.SessionUtils;
import edu.pitt.isg.midas.hub.affiliation.AffiliationRepository;
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

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.RETURN_TO_URL_KEY;

@Controller
@SessionAttributes("appUser")
class CallbackController extends Auth0CallbackHandler {
    private static final String TOS = "/tos";
    private SsoConfig ssoConfig;
    private AffiliationRepository repo;

    @Autowired
    protected void setSsoConfig(final SsoConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
    }

    @Autowired
    protected void setAffiliationRepository(final AffiliationRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "${auth0.loginCallback}", method = RequestMethod.GET)
    protected void callback(final HttpServletRequest req, final HttpServletResponse res,
                            final Model model)
            throws ServletException, IOException {
        super.handle(req, res);
        final Auth0User auth0User = SessionUtils.getAuth0User(req);
        model.addAttribute("appUser", auth0User);
    }

    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        final String location = prepareForLocationToRedirect(req);
        res.sendRedirect(location);
    }

    private String prepareForLocationToRedirect(HttpServletRequest req) {
        final Auth0User auth0User = SessionUtils.getAuth0User(req);
        final String partnerUrl = getExternalReturnUrl(req);
        if (auth0User.getAppMetadata().get(AFFILIATION) == null) {
            req.getSession().setAttribute(RETURN_TO_URL_KEY, partnerUrl);
            return toRelativeUrl(req, TOS);
        } else if (partnerUrl != null) {
            return  partnerUrl;
        } else {
            return toRelativeUrl(req, this.redirectOnSuccess);
        }
    }

    @RequestMapping(value = TOS, method = RequestMethod.GET)
    protected String termsOfServices(final HttpServletRequest req, final HttpServletResponse res,
                                     final Model model)
            throws ServletException, IOException {
        final Auth0User auth0User = SessionUtils.getAuth0User(req);
        model.addAttribute("affiliations", repo.findAll());
        return "terms";
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

    private String toRelativeUrl(HttpServletRequest req, String path) {
        return req.getContextPath() + path;
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
