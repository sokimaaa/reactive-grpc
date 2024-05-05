plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

val testContainersVersion = "1.19.7"

tasks {
    named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        isEnabled = false
    }
}

dependencies {
    testImplementation(project(":proto-domain"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.devh:grpc-client-spring-boot-starter:2.15.0.RELEASE")

    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
