package com.capmoney.api.manager.bootstrap.service;

import com.capmoney.api.ApiApplication;
import com.capmoney.api.http.ApiProblem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

/** Separate JVM entry point for the multi-process integration test. */
public final class ManagerLinkRenewalWorker {
    private ManagerLinkRenewalWorker() {}

    public static void main(String[] args) throws Exception {
        Path properties = Path.of(args[0]);
        Path ready = Path.of(args[1]);
        Path start = Path.of(args[2]);
        SpringApplication application = new SpringApplication(ApiApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        try (var context = application.run("--spring.config.on-not-found=ignore",
                "--spring.config.additional-location=file:" + properties)) {
            Files.writeString(ready, "ready");
            long deadline = System.nanoTime() + Duration.ofSeconds(30).toNanos();
            while (!Files.exists(start) && System.nanoTime() < deadline) Thread.sleep(20);
            if (!Files.exists(start)) throw new IllegalStateException("start_barrier_timeout");
            String proof = context.getEnvironment().getRequiredProperty("capmoney.test.proof-token");
            String ip = context.getEnvironment().getRequiredProperty("capmoney.test.client-ip");
            try {
                context.getBean(ManagerLinkService.class).renew(proof, ip);
                System.out.println("RENEW_RESULT=accepted");
            } catch (ApiProblem expected) {
                System.out.println("RENEW_RESULT=" + expected.code());
            }
        }
    }
}
