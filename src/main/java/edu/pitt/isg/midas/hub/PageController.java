package edu.pitt.isg.midas.hub;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("appUser")
public class PageController {
    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=text/html")
    public String showLandingPage() {
        return "redirect:/report";
    }
}
