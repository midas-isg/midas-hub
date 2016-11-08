package edu.pitt.isg.midas.hub.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static edu.pitt.isg.midas.hub.Application.RUNNER_PREFIX;

@ConditionalOnProperty(
        prefix = RUNNER_PREFIX,
        name = {"dump.enabled"}
)
@Component
public class DumpRunner implements CommandLineRunner {
    @Autowired
    private LogDumper log;

    @Override
    public void run(String... args) throws Exception {
        log.dump();
    }
}