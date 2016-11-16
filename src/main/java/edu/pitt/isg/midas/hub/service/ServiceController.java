package edu.pitt.isg.midas.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.IS_ISG_ADMIN;

@Controller
@SessionAttributes("appUser")
class ServiceController {
    @Autowired
    private ServiceRepository repository;

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String showServices(Model model) {
        model.addAttribute("services", repository.findAll());
        return "secured/home";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=text/html")
    public String showLandingPage() {
        return "redirect:/services";
    }

    @PostAuthorize(IS_ISG_ADMIN)
    @RequestMapping(value = "/api/services", method = RequestMethod.POST)
    public String postService(@ModelAttribute Service form){
        repository.save(form);
        return "redirect:/services";
    }
}