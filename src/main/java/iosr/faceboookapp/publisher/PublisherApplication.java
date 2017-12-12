package iosr.faceboookapp.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublisherApplication.class, args);
	}
}
