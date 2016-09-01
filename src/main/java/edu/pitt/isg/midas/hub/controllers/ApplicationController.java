package edu.pitt.isg.midas.hub.controllers;

import edu.pitt.isg.midas.hub.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("appUser")
class ApplicationController {
    @Autowired
    private ServiceRepository repository;

    @RequestMapping(value = "/secured/home", method = RequestMethod.GET)
    public String showHomePage(final Model model) {
        model.addAttribute("services", repository.findAll());
        return "secured/home";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=text/html")
    public String showLandingPage() {
        return "redirect:/secured/home";
    }
}
