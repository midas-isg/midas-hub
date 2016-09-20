package edu.pitt.isg.midas.hub.auth0;

import com.auth0.web.Auth0User;
import com.auth0.web.NonceUtils;
import com.auth0.web.SessionUtils;
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

@Controller
@SessionAttributes("appUser")
public class Auth0LoginController {
    private static final String LOGIN_VIEW = "auth0Login";

    @RequestMapping(value = "/auth0", method = RequestMethod.GET)
    public String processAuth0Login(
            final HttpServletRequest request) {
        return "redirect:/secured/home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(
            final HttpServletRequest request,
            final Model model) {
        NonceUtils.addNonceToStorage(request);
        final String nonce = SessionUtils.getState(request);

        model.addAttribute("callbackUrl", UrlAid.toAbsoluteUrl(request, "callback"));
        model.addAttribute("state", nonce);
        final Auth0User auth0User = SessionUtils.getAuth0User(request);
        if (auth0User != null) {
            model.addAttribute("userId", auth0User.getUserId());
            model.addAttribute("appUser", auth0User);
        }
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

        final String redirectUrl = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(auth0Domain)
                .pathSegment("v2", "logout")
                .queryParam("returnTo", UrlAid.toAbsoluteUrl(request, "login"))
                .build().toString();
        return new RedirectView(redirectUrl, false);
    }
}
