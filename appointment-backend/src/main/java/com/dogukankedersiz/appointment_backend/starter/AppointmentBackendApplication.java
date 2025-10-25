package com.dogukankedersiz.appointment_backend.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.dogukankedersiz.pageable"})
@EnableJpaRepositories(basePackages = {"com.dogukankedersiz"})
public class AppointmentBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentBackendApplication.class, args);
	}

}
