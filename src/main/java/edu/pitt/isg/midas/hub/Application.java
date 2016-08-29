package edu.pitt.isg.midas.hub;

import edu.pitt.isg.midas.hub.affiliation.AffiliationRunner;
import edu.pitt.isg.midas.hub.service.ServiceRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
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
