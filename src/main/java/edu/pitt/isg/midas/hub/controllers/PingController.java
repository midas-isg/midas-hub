package edu.pitt.isg.midas.hub.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Component
public class PingController {

	public static final String PUBLIC_PING_URL = "/public/ping";
	public static final String AUTHENTICATED_PING_URL = "/authenticated/ping";
	public static final String SECURED_PING_URL = "/secured/ping";
	public static final String ADMIN_PING_URL = "/admin/ping";

	@RequestMapping(value = PUBLIC_PING_URL)
	@ResponseBody
	public String ping() {
		return "All good. You don't need to be authenticated to call this";
	}

	@RequestMapping(value = AUTHENTICATED_PING_URL)
	@ResponseBody
	public String authenticatedPing() {
		return "All good! You only get this message if you're authenticated!";
	}

	@RequestMapping(value = SECURED_PING_URL)
	@ResponseBody
	public String securedPing() {
		return "All good! You only get this message if you're User!";
	}

	@RequestMapping(value = ADMIN_PING_URL)
	@ResponseBody
	public String adminPing() {
		return "All good! You only get this message if you're Admin!";
	}
}
