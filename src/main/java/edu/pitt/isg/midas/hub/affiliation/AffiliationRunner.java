package edu.pitt.isg.midas.hub.affiliation;

import edu.pitt.isg.midas.hub.service.ServiceRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class AffiliationRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ServiceRunner.class);

    @Autowired
    private AffiliationRepository repository;

    @Override
    public void run(String... args) throws Exception {
        loadAffiliationsIfNotExist();
        logAffiliations();
    }

    private void loadAffiliationsIfNotExist() {
        if (repository.count() <= 0)
            loadAffiliations();
    }

    private void logAffiliations() {
        log.debug("All Affiliations:");
        log.debug("-------------------------------");
        for (Affiliation aff : repository.findAll()) {
            log.debug(aff.toString());
        }
        log.debug("");
    }

    private void loadAffiliations() {
        repository.save(new Affiliation("HPC", "High Performance Computing"));
        repository.save(new Affiliation("ISG", "MIDAS Informatics Services Group"));
        repository.save(new Affiliation("MISSION", "Networking and education arm of the ISG"));
        repository.save(new Affiliation("SYNECO", "Synthetic Populations and Ecosystems"));
    }
}
