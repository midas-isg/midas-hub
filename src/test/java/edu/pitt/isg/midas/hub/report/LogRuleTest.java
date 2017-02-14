package edu.pitt.isg.midas.hub.report;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ACCOUNTS_APP_ADMIN;
import static edu.pitt.isg.midas.hub.auth0.PredefinedStrings.ACCOUNTS_DIRECTOR;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class LogRuleTest {
    private static final ReportingLog log1A = newDefaultLog("app1", "affA");
    private static final ReportingLog log1B = newDefaultLog("app1", "affB");
    private static final ReportingLog log2A = newDefaultLog("app2", "affA");
    private static final ReportingLog log2B = newDefaultLog("app2", "affB");
    private static final ReportingLog log3A = newDefaultLog("app3", "affA");
    private static final ReportingLog log3B = newDefaultLog("app3", "affB");
    private static final List<ReportingLog> sources = asList(log1A, log1B, log2A, log2B, log3A, log3B);
    private LogRule rule = new LogRule();
    private static final List<String> admin1Roles = toAppAdminRoles("app1");
    private static final List<ReportingLog> log1s = asList(log1A, log1B);

    private static ReportingLog newDefaultLog(String app, String aff) {
        final ReportingLog log = new ReportingLog();
        log.setApplicationName(app);
        log.setApplicationId(app);
        log.setUserAffiliation(aff);
        return log;
    }

    @Test
    public void testDirector() throws Exception {
        test(asList(ACCOUNTS_DIRECTOR), sources);
    }

    @Test
    public void testDirectorWithAffiliationPrintedAsNull() throws Exception {
        final List<ReportingLog> logs = new ArrayList<>(sources);
        logs.add(newDefaultLog("don't care (null affiliation case)", null));
        logs.add(newDefaultLog("don't care (affiliation named null case)", "null"));
        test(logs, asList(ACCOUNTS_DIRECTOR), sources);
    }

    @Test
    public void testDirectorWithNullApplication() throws Exception {
        final List<ReportingLog> logs = new ArrayList<>(sources);
        logs.add(newDefaultLog(null, "don't care (null application case)"));
        test(logs, asList(ACCOUNTS_DIRECTOR), sources);
    }

    @Test
    public void testAdmin1() throws Exception {
        test(admin1Roles, log1s);
    }

    @Test
    public void testAdmin2() throws Exception {
        test(toAppAdminRoles("app2"), asList(log2A, log2B));
    }

    @Test
    public void testAdmin1and3() throws Exception {
        final ArrayList<String> roles = new ArrayList<>(admin1Roles);
        roles.add(toAppAdminRole("app3"));
        final ArrayList<ReportingLog> expected = new ArrayList<>(log1s);
        expected.addAll(asList(log3A, log3B));
        test(roles, expected);
    }

    @Test
    public void testAdmin1AndDirector() throws Exception {
        final ArrayList<String> roles = new ArrayList<>(admin1Roles);
        roles.add(ACCOUNTS_DIRECTOR);
        test(roles, log1s);
    }

    @Test
    public void testAdmin1AndDisabled2() throws Exception {
        final ArrayList<String> roles = new ArrayList<>(admin1Roles);
        roles.add("disable " + toAppAdminRole("app2"));
        test(roles, log1s);
    }

    private void test(List<String> roles, List<ReportingLog> expected) {
        test(sources, roles, expected);
    }

    private void test(List<ReportingLog> logs, List<String> roles, List<ReportingLog> expected) {
        final List<ReportingLog> actualLogs = rule.toReportingLogs(logs, roles);
        assertThat(actualLogs).containsExactlyElementsOf(expected);
    }

    private static List<String> toAppAdminRoles(String app) {
        return asList(ACCOUNTS_APP_ADMIN, toAppAdminRole(app));
    }

    private static String toAppAdminRole(String app) {
        return ACCOUNTS_APP_ADMIN + "." + app;
    }
}