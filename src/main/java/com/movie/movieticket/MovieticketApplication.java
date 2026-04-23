package com.movie.movieticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieticketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieticketApplication.class, args);
	}

}
