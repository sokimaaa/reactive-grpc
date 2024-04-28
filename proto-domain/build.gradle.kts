import com.google.protobuf.gradle.*

plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

val protobufJavaVersion = "3.22.2"
val annotationApi = "1.3.2"
val reactorGrpcStub = "1.2.4"
val protocGenGrpcJava = "1.55.1"

dependencies {
    implementation(libs.reactor)
    implementation(libs.grpcApi)
    implementation(libs.grpcStub)
    implementation(libs.grpcProtobuf)

    implementation("javax.annotation:javax.annotation-api:${annotationApi}")

    implementation("com.google.protobuf:protobuf-java:${protobufJavaVersion}")
    implementation("com.salesforce.servicelibs:reactor-grpc-stub:${reactorGrpcStub}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufJavaVersion}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${protocGenGrpcJava}"
        }
        id("reactorGrpc") {
            artifact = "com.salesforce.servicelibs:reactor-grpc:${reactorGrpcStub}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("reactorGrpc")
            }
        }
    }
}
