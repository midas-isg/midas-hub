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

import edu.pitt.isg.sbad.main.AppUser;
import org.apache.shiro.web.subject.WebSubject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("appUser")
@RequestMapping(value = "user/registration")
public class UserRegistrationController implements ViewPath {

    private static final String[] REGISTRATION_SUCCESS = {
        "Thank you for registering!",
        "You should receive an email from us shortly."
    };


    @RequestMapping(value = "federated", method = RequestMethod.POST)
    public String processTermsAndConditions(
            @RequestParam("agree") boolean agree,
            @ModelAttribute("appUser") final AppUser appUser,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final SessionStatus sessionStatus,
            final RedirectAttributes redirectAttributes,
            final Model model) {
        model.addAttribute("appUser", appUser);

        new WebSubject.Builder(request, response)
                        .authenticated(true)
                        .sessionCreationEnabled(true)
                        .buildSubject();
        return REDIRECT_HOME;
    }
}
