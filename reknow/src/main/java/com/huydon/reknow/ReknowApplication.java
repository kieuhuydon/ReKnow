package com.huydon.reknow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReknowApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReknowApplication.class, args);
	}

}
