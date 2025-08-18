package io.spring.restbucksgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(UriConfiguration.class)
@SpringBootApplication
public class RestbucksgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestbucksgatewayApplication.class, args);
	}


	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		return builder.routes()

				.route(p -> p
						.path("/statistics")
						.uri(uriConfiguration.getUrl())).
				build();
	}
}
