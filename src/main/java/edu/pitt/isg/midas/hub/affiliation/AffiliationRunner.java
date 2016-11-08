package edu.pitt.isg.midas.hub.affiliation;

import edu.pitt.isg.midas.hub.service.ServiceRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static edu.pitt.isg.midas.hub.Application.RUNNER_PREFIX;

@ConditionalOnProperty(
        prefix = RUNNER_PREFIX,
        name = {"affiliation.enabled"}
)
@Component
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
        log.info("All Affiliations:");
        log.info("-------------------------------");
        for (Affiliation aff : repository.findAll()) {
            log.info(aff.toString());
        }
        log.info("");
    }

    private void loadAffiliations() {
        repository.save(new Affiliation("ISG:HPC", "High Performance Computing - ISG"));
        repository.save(new Affiliation("ISG:SDS", "Software Development Services - ISG"));
        repository.save(new Affiliation("ISG:MISSION", "Networking and education arm of the ISG"));
        repository.save(new Affiliation("ISG", "Informatics Services Group"));
        repository.save(new Affiliation("ISG:SYNECO", "Synthetic Populations and Ecosystems - ISG"));
        repository.save(new Affiliation("CCDD", "Center for Communicable Disease Dynamics"));
        repository.save(new Affiliation("Fred Hutch", "Fred Hutchinson Cancer Research Center"));
        repository.save(new Affiliation("Pitt", "University of Pittsburgh"));
        repository.save(new Affiliation("VT BI", "Biocomplexity Institute of Virginia Tech"));
        repository.save(new Affiliation("LANL", "Los Alamos National Laboratory"));
        repository.save(new Affiliation("UCSF", "University of California, San Francisco"));
        repository.save(new Affiliation("UCSD", "University of California, San Diego"));
        repository.save(new Affiliation("LSU Vet Med", "Louisiana State University School of Veterinary Medicine"));
        repository.save(new Affiliation("Yale", "Yale University"));
        repository.save(new Affiliation("UGA", "University of Georgia"));
        repository.save(new Affiliation("Imperial", "Imperial College London"));
        repository.save(new Affiliation("U-M", "University of Michigan"));
        repository.save(new Affiliation("Columbia", "Columbia University"));
        repository.save(new Affiliation("UT", "University of Texas"));
    }
}
