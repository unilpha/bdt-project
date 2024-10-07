package com.bdt.assignment.bdt_final_project;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class BdtFinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdtFinalProjectApplication.class, args);
	}
}
