package edu.pitt.isg.midas.hub.auth0;


import com.auth0.web.Auth0CallbackHandler;
import com.auth0.web.Auth0User;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.auth0.web.SessionUtils.getAuth0User;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ISG_USER;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.RETURN_TO_URL_KEY;
import static edu.pitt.isg.midas.hub.auth0.UrlAid.toAuth0UserUrl;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpMethod.PATCH;

@Controller
public class AccessControlController extends Auth0CallbackHandler {
    private UserMetaDataRule rule;

    @Autowired
    public AccessControlController(UserMetaDataRule userMetaDataRule) {
        rule = userMetaDataRule;
    }

    @RequestMapping(value = "/term-acceptance", method = RequestMethod.POST)
    public String acceptTerms(String affiliationName, HttpServletRequest request, RedirectAttributes redirectAttributes){
        if (affiliationName == null) {
            return prepareLocationForErrorRequiringOrganization(redirectAttributes);
        }
        rule.saveUserMetaDataAffiliationAndIsgUserRole(getAuth0User(request), affiliationName);
        return "redirect:" + toReturnUrl(request.getSession());
    }

    private String toString(Map<String, Object> body, String key) {
        if (body == null || key == null)
            return null;
        final Object value = body.get(key);
        if (value == null)
            return null;
        return value.toString();
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
