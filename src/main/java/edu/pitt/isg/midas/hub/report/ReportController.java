package edu.pitt.isg.midas.hub.report;

import com.auth0.spring.security.mvc.Auth0JWTToken;
import com.auth0.spring.security.mvc.Auth0UserDetails;
import edu.pitt.isg.midas.hub.log.LogRule;
import edu.pitt.isg.midas.hub.log.ReportingLog;
import edu.pitt.isg.midas.hub.user.UserRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.CAN_VIEW_LOG_REPORTS;
import static java.util.stream.Collectors.toList;

@Controller
@SessionAttributes("appUser")
class ReportController {
    @Autowired
    private UserRule userRule;
    @Autowired
    private LogRule logRule;

    @PreAuthorize(CAN_VIEW_LOG_REPORTS)
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReportPage(final Model model, @AuthenticationPrincipal Auth0JWTToken authenticationToken) {
        model.addAttribute("users", userRule.listAllWithoutSensitiveData());
        model.addAttribute("logs", toLogs(authenticationToken));
        return "report";
    }

    private List<ReportingLog> toLogs(Auth0JWTToken authenticationToken) {
        final Auth0UserDetails user = toUser(authenticationToken);
        final List<String> roles = toAuthorities(user);
        return logRule.toReportingLogsByRoles(roles);
    }

    private Auth0UserDetails toUser(Auth0JWTToken authenticationToken) {
        return (Auth0UserDetails)authenticationToken.getPrincipal();
    }

    private List<String> toAuthorities(Auth0UserDetails auth0User) {
        return auth0User.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(toList());
    }
}