package edu.pitt.isg.midas.hub;

import edu.pitt.isg.midas.hub.affiliation.Affiliation;
import edu.pitt.isg.midas.hub.affiliation.AffiliationRepository;
import edu.pitt.isg.midas.hub.affiliation.AffiliationRunner;
import edu.pitt.isg.midas.hub.service.Service;
import edu.pitt.isg.midas.hub.service.ServiceRepository;
import edu.pitt.isg.midas.hub.service.ServiceRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.pitt.isg", "com.auth0"})
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner runService(ServiceRunner runner) {
		return runner;
	}

	@Bean
	public CommandLineRunner runAffiliation(AffiliationRunner runner) {
		return runner;
	}
}
