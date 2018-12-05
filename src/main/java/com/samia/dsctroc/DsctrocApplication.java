package com.samia.dsctroc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DsctrocApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsctrocApplication.class, args);
    }
}
