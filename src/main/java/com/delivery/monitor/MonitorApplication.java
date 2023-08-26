package com.delivery.monitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.delivery.monitor.admins.AdminsGenerator;
import com.delivery.monitor.products.ProductsGenerator;
import com.delivery.monitor.tables.DataReset;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class MonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(DataReset tableGenerator, ProductsGenerator productsGenerator,
			AdminsGenerator adminsGenerator) {
		return args -> {
			log.info("Initializing database");
			// tableGenerator.resetDatabase();
			log.info("Database initialized successfully");
			log.info("Generating products");
			productsGenerator.loadProductsFromFile();
			log.info("Products generated successfully");
			log.info("Generating Admins");
			// adminsGenerator.insertAdmins();
			log.info("Admins generated successfully");
		};
	}
}
