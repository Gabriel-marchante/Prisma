package org.padalustro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class Index {

	public static void main(String[] args) {
		SpringApplication.run(Index.class, args);
	}

}
