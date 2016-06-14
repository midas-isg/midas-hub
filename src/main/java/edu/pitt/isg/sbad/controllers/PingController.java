package edu.pitt.isg.sbad.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Component
public class PingController {
	@RequestMapping(value = "/ping")
	@ResponseBody
	public String ping() {
		return "All good. You don't need to be authenticated to call this";
	}

	@RequestMapping(value = "/secured/ping")
	@ResponseBody
	public String securedPing() {
		return "All good! You only get this message if you're authenticated!";
	}
}
