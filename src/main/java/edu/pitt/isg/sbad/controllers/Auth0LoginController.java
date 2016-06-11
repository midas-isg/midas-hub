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
package com.example.controllers;

import com.auth0.Auth0User;
import com.auth0.NonceGenerator;
import com.auth0.NonceStorage;
import com.auth0.RequestNonceStorage;
import com.example.main.AppUser;
import com.example.main.CcdProperties;
import com.example.main.UrlAid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 *
 * Feb 18, 2016 2:00:19 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Controller
//@Profile("auth0")
@SessionAttributes("appUser")
public class Auth0LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Auth0LoginController.class);

    private final NonceGenerator nonceGenerator = new NonceGenerator();

    public static final String LOGIN_VIEW = "auth0Login";

    private final CcdProperties ccdProperties;

    //private final UserAccountService userAccountService;

    //private final AppUserService appUserService;

    @Autowired
    public Auth0LoginController(CcdProperties ccdProperties/*, UserAccountService userAccountService, AppUserService appUserService*/) {
        this.ccdProperties = ccdProperties;
        //this.userAccountService = userAccountService;
        //this.appUserService = appUserService;
    }

    @RequestMapping(value = "auth0", method = RequestMethod.GET)
    public String processAuth0Login(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final RedirectAttributes redirectAttributes,
            final Model model) {
        Auth0User auth0User = Auth0User.get(request);
        if (auth0User == null) {
            return "redirect:/login";
        } else {
            String email = auth0User.getEmail().toLowerCase();
            /*UserAccount userAccount = userAccountService.findByEmail(email);
            if (userAccount == null) {*/
                String firstName = auth0User.getProperty("given_name");
                String lastName = auth0User.getProperty("family_name");
                AppUser appUser = new AppUser();
                appUser.setEmail(email);
                appUser.setFirstName((firstName == null) ? "" : firstName);
                appUser.setMiddleName("");
                appUser.setLastName((lastName == null) ? "" : lastName);
                appUser.setLocalAccount(false);
                model.addAttribute("appUser", appUser);
                return "secured/terms";
            /*} else {
                if (userAccount.isActive()) {
                    UserLogin userLogin = userAccount.getUserLogin();
                    userLogin.setLastLoginDate(userLogin.getLoginDate());
                    userLogin.setLastLoginLocation(userLogin.getLoginLocation());
                    userLogin.setLoginDate(new Date(System.currentTimeMillis()));
                    try {
                        userLogin.setLoginLocation(UrlUtility.InetNTOA(request.getRemoteAddr()));
                    } catch (UnknownHostException exception) {
                        LOGGER.info(exception.getLocalizedMessage());
                    }
                    userAccountService.saveUserAccount(userAccount);

                    model.addAttribute("appUser", appUserService.createAppUser(userAccount, false));

                    new WebSubject.Builder(request, response)
                            .authenticated(true)
                            .sessionCreationEnabled(true)
                            .buildSubject();

                    return REDIRECT_HOME;
                } else {
                    redirectAttributes.addAttribute("errorMsg", Collections.singletonList("Your account has not been activated."));

                    return REDIRECT_LOGIN;
                }
            }*/
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(
            final HttpServletRequest request,
            final SessionStatus sessionStatus,
            final Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        if (sessionStatus.isComplete()) {
            currentUser.logout();
        } else if (currentUser.isAuthenticated()) {
            return "redirect:/secured/terms";
        } else {
            sessionStatus.setComplete();
        }

        String nonce = nonceGenerator.generateNonce();
        NonceStorage nonceStorage = new RequestNonceStorage(request);
        nonceStorage.setState(nonce);

        model.addAttribute("callbackUrl", UrlAid.buildURI(request, ccdProperties, "callback"));
        model.addAttribute("state", nonce);
        //model.addAttribute("userRegistration", new UserRegistration());

        return LOGIN_VIEW;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(
            final HttpServletRequest request,
            final UsernamePasswordToken credentials,
            final RedirectAttributes redirectAttributes,
            final Model model) {
        Subject currentUser = SecurityUtils.getSubject();
        String username = credentials.getUsername();
        try {
            currentUser.login(credentials);
        } catch (AuthenticationException exception) {
            LOGGER.warn(String.format("Failed login attempt from user %s.", username));
            redirectAttributes.addFlashAttribute("errorMsg", Collections.singletonList("Invalid username and/or password."));
            return "redirect:/login";
        }

        /*UserAccount userAccount = userAccountService.findByUsername(username);
        if (userAccount.isActive()) {
            UserLogin userLogin = userAccount.getUserLogin();
            userLogin.setLastLoginDate(userLogin.getLoginDate());
            userLogin.setLastLoginLocation(userLogin.getLoginLocation());
            userLogin.setLoginDate(new Date(System.currentTimeMillis()));
            try {
                userLogin.setLoginLocation(UrlUtility.InetNTOA(request.getRemoteAddr()));
            } catch (UnknownHostException exception) {
                LOGGER.info(exception.getLocalizedMessage());
            }
            userAccountService.saveUserAccount(userAccount);

            model.addAttribute("appUser", appUserService.createAppUser(userAccount, true));*/
            return "redirect:/secured/terms";
        /*} else {
            currentUser.logout();
            redirectAttributes.addFlashAttribute("errorMsg", Collections.singletonList("Your account has not been activated."));
            return REDIRECT_LOGIN;
        }*/
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RedirectView logOut(
            @Value("${auth0.domain}") final String auth0Domain,
            //@ModelAttribute("appUser") final AppUser appUser,
            final HttpServletRequest request,
            final SessionStatus sessionStatus,
            final RedirectAttributes redirectAttributes) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            currentUser.logout();
            sessionStatus.setComplete();
            redirectAttributes.addFlashAttribute("successMsg", Collections.singletonList("You have successfully logged out."));
        }

        /*if (appUser.getLocalAccount()) {
            return new RedirectView(LOGIN, true);
        } else {*/
            String redirectUrl = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host(auth0Domain)
                    .pathSegment("v2", "logout")
                    .queryParam("returnTo", UrlAid.buildURI(request, ccdProperties, "login"))
                    .build().toString();
            return new RedirectView(redirectUrl, false);
        //}
    }

}
