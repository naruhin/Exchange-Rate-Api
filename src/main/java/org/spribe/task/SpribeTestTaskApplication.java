package org.spribe.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpribeTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpribeTestTaskApplication.class, args);
	}

}
