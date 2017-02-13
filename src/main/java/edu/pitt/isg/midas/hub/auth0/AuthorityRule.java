package edu.pitt.isg.midas.hub.auth0;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class AuthorityRule {
    void addGenericAuthoritiesFromCurrentAuthorities(List<String> roles) {
        final Set<String> set = roles.stream()
                .map(role -> role.split("\\."))
                .filter(tokens -> tokens.length == 2)
                .map(tokens -> tokens[0])
                .collect(toSet());
        set.removeAll(roles);
        roles.addAll(set);
    }
}