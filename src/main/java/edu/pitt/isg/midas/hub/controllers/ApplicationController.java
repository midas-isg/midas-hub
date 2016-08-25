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
package edu.pitt.isg.midas.hub.controllers;

import edu.pitt.isg.midas.hub.auth0.AppUser;
import edu.pitt.isg.midas.hub.service.Service;
import edu.pitt.isg.midas.hub.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * Feb 18, 2016 3:18:46 PM
 *
 * @author Kevin V. Bui (kvb2@pitt.edu)
 */
@Controller
@SessionAttributes("appUser")
public class ApplicationController {
    @Autowired
    private ServiceRepository repository;

    @RequestMapping(value = "federated", method = RequestMethod.POST) // TODO make it more secured.
    public String processTermsAndConditions(
            @ModelAttribute("appUser") final AppUser appUser,
            final Model model) {
        model.addAttribute("appUser", appUser);

        return "redirect:/secured/home";
    }

    @RequestMapping(value = "secured/home", method = RequestMethod.GET)
    public String showHomePage(@ModelAttribute("appUser") final AppUser appUser, final Model model) {
        final Iterable<Service> iterable = repository.findAll();
        model.addAttribute("services", iterable);
        return "secured/home";
    }
}
