package edu.pitt.isg.midas.hub.auth0;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityRuleTest {
    private final AuthorityRule rule = new AuthorityRule();

    private static final List<String> sources0 = asList("no gerica authority (without tht dot character)");
    private static final List<String> sources1 = asList("GA.1", "non-ga");
    private static final List<String> expected1 = new ArrayList<>();
    private static final List<String> sources2 = asList("non_ga.", "GA.1", "GA.2");
    private static final List<String> expected2 = new ArrayList<>();
    private static final List<String> sources11 = asList("GA1.any", "non_GA", "GA2.any");
    private static final List<String> expected11 = new ArrayList<>();

    static {
        fillExpected(expected1, sources1);
        fillExpected(expected2, sources2);
        expected11.addAll(sources11);
        expected11.addAll(asList("GA1", "GA2"));
    }

    private static void fillExpected(List<String> expected, List<String> sources) {
        expected.addAll(sources);
        expected.add("GA");
    }

    @Test
    public void testSource0() throws Exception {
        test(sources0, sources0);
    }

    @Test
    public void testSource1() throws Exception {
        test(sources1, expected1);
    }

    @Test
    public void testExpected1() throws Exception {
        test(expected1, expected1);
    }

    @Test
    public void testSource2() throws Exception {
        test(sources2, expected2);
    }

    @Test
    public void testExpected2() throws Exception {
        test(expected2, expected2);
    }

    @Test
    public void testSource11() throws Exception {
        test(sources11, expected11);
    }

    @Test
    public void testExpected11() throws Exception {
        test(expected11, expected11);
    }

    private void test(List<String> sources, List<String> expected) {
        final List<String> roles = new ArrayList<>(sources);
        rule.addGenericAuthoritiesFromCurrentAuthorities(roles);
        assertThat(roles).containsExactlyElementsOf(expected);
    }
}