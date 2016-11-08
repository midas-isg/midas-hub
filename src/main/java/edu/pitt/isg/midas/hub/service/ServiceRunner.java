package edu.pitt.isg.midas.hub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static edu.pitt.isg.midas.hub.Application.RUNNER_PREFIX;

@ConditionalOnProperty(
        prefix = RUNNER_PREFIX,
        name = {"service.enabled"}
)
@Component
public class ServiceRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ServiceRunner.class);

    @Autowired
    private ServiceRepository repository;

    @Override
    public void run(String... args) throws Exception {
        loadServicesIfNotExist();
        logServices();
    }

    private void loadServicesIfNotExist() {
        if (repository.count() <= 0)
            loadServices();
    }

    private void loadServices() {
        repository.save(new Service("Apollo Library",
                "http://www.epimodels.org/apolloLibraryViewer/",
                "Apollo Library of Standardized Machine Interpretable Information", null, null, null));
        repository.save(new Service("SPEW Service",
                "http://spew.olympus.psc.edu/syneco/spe",
                "SPEW Web Service", null, null, null));
    }

    private void logServices() {
        log.info("All Services:");
        log.info("-------------------------------");
        final Iterable<Service> iterable = repository.findAll();
        for (Service s : iterable) {
            log.info(s.toString());
        }
        log.info("");
    }
}