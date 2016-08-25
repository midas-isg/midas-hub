package edu.pitt.isg.midas.hub.service;

import edu.pitt.isg.midas.hub.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@org.springframework.stereotype.Service
public class ServiceRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ServiceRunner.class);

    @Autowired
    private ServiceRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // reloadServices();
        loadServicesIfNotExist();
        logServices();
    }

    private void loadServicesIfNotExist() {
        if (repository.count() <= 0)
            loadServices();
    }

    private void reloadServices() {
        repository.deleteAll();
        loadServices();
    }

    private void loadServices() {
        repository.save(new Service("Apollo Library",
                "http://www.epimodels.org/apolloLibraryViewer/",
                "Apollo Library of Standardized Machine Interpretable Information"));
        repository.save(new Service("SPEW Service",
                "http://spew.olympus.psc.edu/syneco/spe",
                "SPEW Web Service"));
    }

    private void logServices() {
        log.debug("All Services:");
        log.debug("-------------------------------");
        final Iterable<Service> iterable = repository.findAll();
        for (Service s : iterable) {
            log.debug(s.toString());
        }
        log.debug("");
    }
}
