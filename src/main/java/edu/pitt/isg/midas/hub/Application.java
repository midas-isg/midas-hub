package edu.pitt.isg.midas.hub;

import edu.pitt.isg.midas.hub.affiliation.AffiliationRunner;
import edu.pitt.isg.midas.hub.service.ServiceRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
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

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("user")
				.apiInfo(apiInfo())
				.select()
				.paths(regex("(/admin)*/api/user.*"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("MIDAS Hub")
				.description("The Hub to MIDAS Network applications")
				.build();
	}
}
