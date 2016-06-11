package com.example.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Component
public class SecuredPingController implements ViewPath {

	@RequestMapping(value = "/secured/ping")
	@ResponseBody
	public String securedPing() {
		return "All good! You only get this message if you're authenticated!";
	}
}
