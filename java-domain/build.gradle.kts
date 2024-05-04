plugins {
    id("java")
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

dependencies {
    implementation(libs.reactor)
    implementation(libs.codec)
    implementation(libs.apache)

    compileOnly("org.immutables:value:2.10.0-rc0")
    annotationProcessor("org.immutables:value:2.10.0-rc0")

    implementation("org.slf4j:slf4j-api:1.7.25")
}
