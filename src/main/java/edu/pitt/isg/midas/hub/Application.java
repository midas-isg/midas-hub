package edu.pitt.isg.midas.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.TimeZone;

import static java.util.TimeZone.getTimeZone;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class Application {
	public static final String RUNNER_PREFIX = "app.runner";

	public static void main(String[] args) {
		TimeZone.setDefault(getTimeZone("UTC"));
		SpringApplication.run(Application.class, args);
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
				.title("MIDAS Software Portal")
				.description("The Portal to MIDAS Network applications")
				.build();
	}
}
