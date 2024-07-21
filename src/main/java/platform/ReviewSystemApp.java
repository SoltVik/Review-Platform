package platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReviewSystemApp {

	public static void main(String[] args) {
		SpringApplication.run(ReviewSystemApp.class, args);
	}
}
