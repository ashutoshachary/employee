package com.ashutosh.employeetesting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeetestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeetestingApplication.class, args);
	}

}
