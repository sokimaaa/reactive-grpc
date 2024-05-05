package com.sokima.reactive.grpc.bookstore.e2e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

public abstract class E2eContainer {

    private static final Logger log = LoggerFactory.getLogger(E2eContainer.class);

    static DockerComposeContainer<?> environment = createEnvironment();

    static {
        environment.start();
    }

    static DockerComposeContainer<?> createEnvironment() {
        return new DockerComposeContainer<>(new File("../docker-compose.yaml"))
                .withBuild(true)
                .withLocalCompose(true)
                .withExposedService(
                        "bookstore",
                        9090,
                        Wait.forLogMessage(".*Started Starter.*", 1)
                )
                .withExposedService("postgres", 5432)
                .withLogConsumer("bookstore", new Slf4jLogConsumer(log));
    }
}
