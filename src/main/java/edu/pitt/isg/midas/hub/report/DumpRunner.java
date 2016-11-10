package edu.pitt.isg.midas.hub.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static edu.pitt.isg.midas.hub.Application.RUNNER_PREFIX;

@ConditionalOnProperty(
        prefix = RUNNER_PREFIX,
        name = {"dump.enabled"}
)
@Component
class DumpRunner implements CommandLineRunner {
    private static final String RATE_MS = RUNNER_PREFIX + ".dump.rate.ms";
    private static final Logger log = LoggerFactory.getLogger(DumpRunner.class);

    @Autowired
    private LogDumper logDumper;
    @Value("${" + RATE_MS + "}")
    private String fixedRateString;

    @Override
    public void run(String... args) throws Exception {
        run();
    }

    private void run() {
        final String msg = "is dumping logs and will run again in " + fixedRateString + " milliseconds.";
        log.info(msg);
        logDumper.dump();
    }

    @Scheduled(
            initialDelayString="${" + RATE_MS + "}",
            fixedRateString="${" + RATE_MS + "}"
    )
    public void runAsScheduled() {
        run();
    }
}