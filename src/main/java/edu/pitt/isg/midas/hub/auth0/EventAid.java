package edu.pitt.isg.midas.hub.auth0;

import java.util.HashMap;
import java.util.Map;

public class EventAid {
    /**
     * EVENT ACRONYM MAPPING according to
     * https://auth0.com/docs/api/management/v2#!/Logs/get_logs
     */
    public static Map<String, String> makeEventCode2NameLookupMap(){
        Map<String, String> eventCode2Name = new HashMap<>();
        eventCode2Name.put("s", "Success Login");
        eventCode2Name.put("ssa", "Success Silent Auth");
        eventCode2Name.put("fsa", "Failed Silent Auth");
        eventCode2Name.put("seacft", "Success Exchange (Authorization Code for Access Token)");
        eventCode2Name.put("feacft", "Failed Exchange (Authorization Code for Access Token)");
        eventCode2Name.put("seccft", "Success Exchange (Client Credentials for Access Token)");
        eventCode2Name.put("feccft", "Failed Exchange (Client Credentials for Access Token)");
        eventCode2Name.put("sepft", "Success Exchange (Password for Access Token)");
        eventCode2Name.put("fepft", "Failed Exchange (Password for Access Token)");
        eventCode2Name.put("f", "Failed Login");
        eventCode2Name.put("w", "Warnings During Login");
        eventCode2Name.put("du", "Deleted User");
        eventCode2Name.put("fu", "Failed Login (invalid email/username)");
        eventCode2Name.put("fp", "Failed Login (wrong password)");
        eventCode2Name.put("fc", "Failed by Connector");
        eventCode2Name.put("fco", "Failed by CORS");
        eventCode2Name.put("con", "Connector Online");
        eventCode2Name.put("coff", "Connector Offline");
        eventCode2Name.put("fcpro", "Failed Connector Provisioning");
        eventCode2Name.put("ss", "Success Signup");
        eventCode2Name.put("fs", "Failed Signup");
        eventCode2Name.put("cs", "Code Sent");
        eventCode2Name.put("cls", "Code/Link Sent");
        eventCode2Name.put("sv", "Success Verification Email");
        eventCode2Name.put("fv", "Failed Verification Email");
        eventCode2Name.put("scp", "Success Change Password");
        eventCode2Name.put("fcp", "Failed Change Password");
        eventCode2Name.put("sce", "Success Change Email");
        eventCode2Name.put("fce", "Failed Change Email");
        eventCode2Name.put("scu", "Success Change Username");
        eventCode2Name.put("fcu", "Failed Change Username");
        eventCode2Name.put("scpn", "Success Change Phone Number");
        eventCode2Name.put("fcpn", "Failed Change Phone Number");
        eventCode2Name.put("svr", "Success Verification Email Request");
        eventCode2Name.put("fvr", "Failed Verification Email Request");
        eventCode2Name.put("scpr", "Success Change Password Request");
        eventCode2Name.put("fcpr", "Failed Change Password Request");
        eventCode2Name.put("fn", "Failed Sending Notification");
        eventCode2Name.put("sapi", "API Operation");
        eventCode2Name.put("fapi", "Failed API Operation");
        eventCode2Name.put("limit_wc", "Blocked Account");
        eventCode2Name.put("limit_mu", "Blocked IP Address");
        eventCode2Name.put("limit_ui", "Too Many Calls to /userinfo");
        eventCode2Name.put("api_limit", "Rate Limit On API");
        eventCode2Name.put("sdu", "Successful User Deletion");
        eventCode2Name.put("fdu", "Failed User Deletion");
        eventCode2Name.put("slo", "Success Logout");
        eventCode2Name.put("flo", "Failed Logout");
        eventCode2Name.put("sd", "Success Delegation");
        eventCode2Name.put("fd", "Failed Delegation");
        eventCode2Name.put("fcoa", "Failed Cross Origin Authentication");
        eventCode2Name.put("scoa", "Success Cross Origin Authentication");
        return  eventCode2Name;
    }
}