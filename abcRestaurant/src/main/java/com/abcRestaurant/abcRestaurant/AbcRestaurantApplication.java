package com.abcRestaurant.abcRestaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.abcRestaurant.abcRestaurant")
@EnableScheduling
@Import(com.abcRestaurant.abcRestaurant.Configuration.MailConfiguration.class)

public class AbcRestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbcRestaurantApplication.class, args);
	}

}
