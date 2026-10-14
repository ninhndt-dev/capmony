package com.capmoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) {
        if (java.util.Arrays.asList(args).contains("--capmoney.bootstrap-manager")) {
            SpringApplication application = new SpringApplication(ApiApplication.class);
            application.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
            try (var context = application.run(args)) {
                var result = context.getBean(com.capmoney.api.manager.bootstrap.service.BootstrapIssuer.class).issue();
                System.out.println("bootstrap=" + result.code() + " generation=" + result.generation());
                int exitCode = result.exitCode();
                context.close();
                System.exit(exitCode);
            }
        } else {
            SpringApplication.run(ApiApplication.class, args);
        }
    }
}
