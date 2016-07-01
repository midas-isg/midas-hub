/*
 * Copyright (C) 2016 University of Pittsburgh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package edu.pitt.isg.sbad.controllers;

import com.auth0.web.Auth0User;
import com.auth0.web.NonceUtils;
import com.auth0.web.SessionUtils;
import edu.pitt.isg.sbad.auth0.AppUser;
import edu.pitt.isg.sbad.auth0.UrlAid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 *
 * Feb 18, 2016 2:00:19 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Controller
@SessionAttributes("appUser")
public class Auth0LoginController implements ViewPath {
    public static final String LOGIN_VIEW = "auth0Login";
    private static final Logger LOGGER = LoggerFactory.getLogger(Auth0LoginController.class);

    @RequestMapping(value = "auth0", method = RequestMethod.GET)
    public String processAuth0Login(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final RedirectAttributes redirectAttributes,
            final Model model) {
        Auth0User auth0User = (Auth0User)request.getSession().getAttribute("auth0User");
        String email = auth0User.getEmail().toLowerCase();
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setFirstName(auth0User.getGivenName());
        appUser.setLastName(auth0User.getFamilyName());
        appUser.setLocalAccount(false);
        model.addAttribute("appUser", appUser);
        return TERMS_VIEW;
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(
            final HttpServletRequest request,
            final SessionStatus sessionStatus,
            final Model model) {

        NonceUtils.addNonceToStorage(request);
        String nonce = SessionUtils.getState(request);

        model.addAttribute("callbackUrl", UrlAid.buildURI(request, "callback"));
        model.addAttribute("state", nonce);

        return LOGIN_VIEW;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RedirectView logOut(
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
