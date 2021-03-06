package edu.pitt.isg.midas.hub.log;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ACCOUNTS_APP_ADMIN;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class LogRule {
    @Autowired
    private LogRepository logRepo;
    @Value("${app.log.clientIdsToFilterOut}")
    private String[] arrayOfClientIdsToFilterOut;

    public List<ReportingLog> toReportingLogsByRoles(List<String> roles) {
        final List<ReportingLog> logs = toReportingLogs(listLogsWithoutSensitiveData(), roles);
        return logs;
    }

    private List<ReportingLog> listLogsWithoutSensitiveData() {
        final List<ReportingLog> logs = logRepo.findAll();
        logs.forEach(log -> log.setRaw(null));
        return logs;
    }

    @VisibleForTesting
    List<ReportingLog> toReportingLogs(List<ReportingLog> logs, List<String> roles) {
        final Set<String> filterOutClientIds = toClientIdsToFilterOut();
        final Set<String> filterOutEventCodes = toEventCodesToFilterOut();
        return logs.stream()
                .filter(l -> l.getApplicationName() != null)
//                .filter(l -> isNotPrintedAsNull(l.getUserAffiliation()))
                .filter(l -> !filterOutClientIds.contains(l.getApplicationId()))
                .filter(l -> !filterOutEventCodes.contains(l.getEventCode()))
                .filter(l -> contains(l.getApplicationId(), toClientIdToFilterIn(roles)))
                .collect(toList());
    }

    private Set<String> toClientIdToFilterIn(List<String> roles) {
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

    private Set<String> toClientIdsToFilterOut() {
        if (arrayOfClientIdsToFilterOut == null)
            return emptySet();
        return stream(arrayOfClientIdsToFilterOut).collect(toSet());
    }
}
