package edu.pitt.isg.midas.hub.auth0;


import com.auth0.web.Auth0CallbackHandler;
import edu.pitt.isg.midas.hub.affiliation.AffiliationForm;
import edu.pitt.isg.midas.hub.user.UserRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.auth0.web.SessionUtils.getAuth0User;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.RETURN_TO_URL_KEY;

@Controller
public class AccessControlController extends Auth0CallbackHandler {
    private UserRule rule;

    @Autowired
    public AccessControlController(UserRule userMetaDataRule) {
        rule = userMetaDataRule;
    }

    @RequestMapping(value = "/term-acceptance", method = RequestMethod.POST)
    public String acceptTerms(@ModelAttribute AffiliationForm form, HttpServletRequest request, RedirectAttributes redirectAttributes){
        final String affiliationName = form.getName();
        if (affiliationName == null)
            return prepareLocationForErrorRequiringOrganization(redirectAttributes);

        rule.saveUserMetaDataAffiliationAndIsgUserRole(getAuth0User(request), form);
        return "redirect:" + toReturnUrl(request.getSession());
    }

    private String prepareLocationForErrorRequiringOrganization(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Organization is required!");
        return "redirect:" + Auth0LoginController.TOS;
    }

    private String toReturnUrl(HttpSession session) {
        final Object attribute = session.getAttribute(RETURN_TO_URL_KEY);
        if (attribute != null)
            return attribute.toString();
        return this.redirectOnSuccess;
    }
}
