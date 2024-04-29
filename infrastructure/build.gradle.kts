plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

application {
    mainClass.set("com.sokima.reactive.grpc.bookstore.infrastructure.Starter")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(project(":proto-domain"))
    implementation(project(":java-domain"))
    implementation(project(":usecase"))

    implementation("org.springframework.boot:spring-boot-starter")

    implementation(libs.reactor)

    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}

dependencies {
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.projectreactor:reactor-test")
}
