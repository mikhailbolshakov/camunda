package org.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(Application.class);
        addInitHooks(application);
        application.run(args);

    }

    static void addInitHooks(SpringApplication application) {
        application.addListeners((ApplicationListener<ApplicationReadyEvent>) event -> { });
    }

}

