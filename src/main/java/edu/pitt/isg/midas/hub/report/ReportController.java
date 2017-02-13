package edu.pitt.isg.midas.hub.report;

import com.auth0.spring.security.mvc.Auth0JWTToken;
import com.auth0.spring.security.mvc.Auth0UserDetails;
import com.auth0.web.Auth0User;
import edu.pitt.isg.midas.hub.auth0.Auth0Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ACCOUNTS_APP_ADMIN;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.CAN_VIEW_LOG_REPORTS;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;

@Controller
@SessionAttributes("appUser")
class ReportController {
    @Autowired
    private LogRepository logRepo;
    @Autowired
    private Auth0Dao dao;

    @PreAuthorize(CAN_VIEW_LOG_REPORTS)
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReportPage(final Model model, @AuthenticationPrincipal Auth0JWTToken authenticationToken) {
        model.addAttribute("users", listUserWithoutSensitiveData());
        model.addAttribute("logs", filter(listLogsWithoutSensitiveData(), toUser(authenticationToken)));
        return "report";
    }

    private List<HashMap<String, ?>> listUserWithoutSensitiveData() {
        final List<HashMap<String, ?>> users = dao.listUsers();
        users.forEach(user -> user.remove("identities"));
        return users;
    }

    private List<ReportingLog> listLogsWithoutSensitiveData() {
        final List<ReportingLog> logs = logRepo.findAll();
        logs.forEach(log -> log.setRaw(null));
        return logs;
    }

    private List<ReportingLog> filter(List<ReportingLog> logs, final Auth0UserDetails user) {
        final Set<String> filterOutEventCodes = toEventCodesToFilterOut();
        final Set<String> filterInClientId = toClientIdToFilterIn(user);
        return logs.stream()
                .filter(l -> l.getApplicationName() != null)
                .filter(l -> isNotPrintedAsNull(l.getUserAffiliation()))
                .filter(l -> !filterOutEventCodes.contains(l.getEventCode()))
                .filter(l -> contains(l.getApplicationId(), filterInClientId))
                .collect(toList());
    }

    private Auth0UserDetails toUser(Auth0JWTToken authenticationToken) {
        return (Auth0UserDetails)authenticationToken.getPrincipal();
    }

    private Set<String> toClientIdToFilterIn(Auth0UserDetails auth0User) {
        final Collection<? extends GrantedAuthority> authorities = auth0User.getAuthorities();
        final List<String> roles =authorities.stream()
                .map(a -> a.getAuthority())
                .collect(toList());
        if (roles.contains(ACCOUNTS_APP_ADMIN))
            return roles.stream()
                    .filter(role -> role.startsWith(ACCOUNTS_APP_ADMIN))
                    .map(role -> role.split(ACCOUNTS_APP_ADMIN + "\\."))
                    .filter(tokens -> tokens.length == 2)
                    .map(tokens -> tokens[1])
                    .collect(toSet());
        return null;
    }

    private boolean contains(String appId, Set<String> set) {
        if (set == null)
            return true;
        else if (appId == null)
            return false;
        return set.contains(appId);
    }

    private boolean isNotPrintedAsNull(String s) {
        return s != null && ! s.equalsIgnoreCase("null");
    }

    private Set<String> toEventCodesToFilterOut() {
        final HashSet<String> set = new HashSet<>();
        set.add("seacft"); // seacft = Success Exchange (Authorization Code for Access Token)
        set.add("feacft"); // feacft = Failed Exchange (Authorization Code for Access Token)
        return set;
    }
}