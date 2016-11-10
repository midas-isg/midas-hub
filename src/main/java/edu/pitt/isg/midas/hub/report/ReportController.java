package edu.pitt.isg.midas.hub.report;

import edu.pitt.isg.midas.hub.auth0.Auth0Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.List;

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
        model.addAttribute("logs", listLogsWithoutSensitiveData());
        return "report";
    }

    private List<ReportingLog> listLogsWithoutSensitiveData() {
        final List<ReportingLog> logs = logRepo.findAll();
        logs.forEach(log -> log.setRaw(null));
        return logs;
    }

    private List<HashMap<String, ?>> listUserWithoutSensitiveData() {
        final List<HashMap<String, ?>> users = dao.listUsers();
        users.forEach(user -> user.remove("identities"));
        return users;
    }
}