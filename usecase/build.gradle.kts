plugins {
    id("java")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(project(":proto-domain"))
    implementation(project(":java-domain"))

    implementation(libs.apache)
    implementation(libs.grpcApi)
    implementation(libs.grpcStub)
    implementation(libs.grpcProtobuf)
    implementation(libs.reactor)

    compileOnly("org.immutables:value:2.10.0-rc0")
    annotationProcessor("org.immutables:value:2.10.0-rc0")

    implementation("org.slf4j:slf4j-api:1.7.25")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.10.0")

    testImplementation("io.projectreactor:reactor-test:3.6.4")
}
