package unipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "unipay")
public class UnipayApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnipayApplication.class, args);
    }
}
