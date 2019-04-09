package com.lknmproduction.messengerrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MessengerRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerRestApplication.class, args);
    }

}
