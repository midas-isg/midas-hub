package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0User;
import com.auth0.web.NonceUtils;
import com.auth0.web.SessionUtils;
import edu.pitt.isg.midas.hub.affiliation.AffiliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.AFFILIATION;

@Controller
@SessionAttributes("appUser")
public class Auth0LoginController {
    private static final String LOGIN_VIEW = "auth0Login";

    @Autowired
    private AffiliationRepository repo;

    @RequestMapping(value = "/auth0", method = RequestMethod.GET)
    public String processAuth0Login(
            final HttpServletRequest request,
            final Model model) {
        final Auth0User auth0User = SessionUtils.getAuth0User(request);
        model.addAttribute("appUser", auth0User);
        if (auth0User.getAppMetadata().get(AFFILIATION) == null) {
            model.addAttribute("affiliations", repo.findAll());
            return "terms";
        }
        return "redirect:/secured/home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(
            final HttpServletRequest request,
            final Model model) {
        NonceUtils.addNonceToStorage(request);
        String nonce = SessionUtils.getState(request);

        model.addAttribute("callbackUrl", UrlAid.buildURI(request, "callback"));
        model.addAttribute("state", nonce);

        return LOGIN_VIEW;
    }

    @RequestMapping(value = "/logoutFromAuth0", method = RequestMethod.GET)
    public RedirectView logout(
            @Value("${auth0.domain}") final String auth0Domain,
            final HttpServletRequest request,
            final SessionStatus sessionStatus,
            final RedirectAttributes redirectAttributes) {

        sessionStatus.setComplete();
        redirectAttributes.addFlashAttribute("successMsg", Collections.singletonList("You have successfully logged out."));

        String redirectUrl = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(auth0Domain)
                .pathSegment("v2", "logout")
                .queryParam("returnTo", UrlAid.buildURI(request, "login"))
                .build().toString();
        return new RedirectView(redirectUrl, false);
    }
}
