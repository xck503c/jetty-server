package com.xck.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class JettyServerController {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(JettyServerController.class);
        app.getBean(JettyServer.class).doInit();
        app.getBean(JettyServer.class).doStart();
    }
}
