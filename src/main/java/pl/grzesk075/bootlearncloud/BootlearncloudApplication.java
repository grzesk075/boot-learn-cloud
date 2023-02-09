package pl.grzesk075.bootlearncloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class BootlearncloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootlearncloudApplication.class, args);
	}

}
