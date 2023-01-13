package com.WebServer;

import com.Server.ninja.Server.Server;
import com.Server.ninja.Server.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Web implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Web.class, args);
    }
    @Override
    public void run(String... args) {

    }
}
