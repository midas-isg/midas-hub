package edu.pitt.isg.midas.hub.report;

import edu.pitt.isg.midas.hub.auth0.Auth0Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("appUser")
class ReportController {
    @Autowired
    private LogRepository logRepo;
    @Autowired
    private Auth0Dao dao;

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String showReportPage(final Model model) {
        model.addAttribute("users", listUserWithoutSensitiveData());
        model.addAttribute("logs", filter(listLogsWithoutSensitiveData()));
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

    private List<ReportingLog> filter(List<ReportingLog> logs) {
        final Set<String> filterOutEventCodes = toEventCodesToFilterOut();
        return logs.stream()
                .filter(l -> !filterOutEventCodes.contains(l.getEventCode()))
                .collect(Collectors.toList());
    }

    private HashSet<String> toEventCodesToFilterOut() {
        final HashSet<String> set = new HashSet<>();
        set.add("seacft"); // seacft = Success Exchange (Authorization Code for Access Token)
        set.add("feacft"); // feacft = Failed Exchange (Authorization Code for Access Token)
        return set;
    }
}