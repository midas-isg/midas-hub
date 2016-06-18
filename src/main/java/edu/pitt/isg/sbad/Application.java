package edu.pitt.isg.sbad;

import edu.pitt.isg.sbad.auth0.Auth0Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.pitt.isg.sbad", "com.auth0"})
@EnableAutoConfiguration
@PropertySource("classpath:auth0.properties")
public class Application {
	public static void main(String[] args) {
		final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Auth0Config.class);
		ctx.refresh();
		SpringApplication.run(Application.class, args);
	}
}