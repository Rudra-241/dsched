package io.github.rudra241.dsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsServerApplication.class, args);
    }
}