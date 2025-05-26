package unipay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for launching the UniPay Spring Boot application.
 * Scans the 'unipay' base package for components and configurations.
 */
@SpringBootApplication(scanBasePackages = "unipay")
public class UnipayApplication {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UnipayApplication.class, args);
    }
}
