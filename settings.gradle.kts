rootProject.name = "reactive-grpc-bookstore"

include("java-domain")
include("proto-domain")
include("usecase")
include("infrastructure")
include("e2e")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            library("reactor", "io.projectreactor:reactor-core:3.6.4")
            library("grpcApi", "io.grpc:grpc-api:1.55.1")
            library("grpcStub", "io.grpc:grpc-stub:1.55.1")
            library("grpcProtobuf", "io.grpc:grpc-protobuf:1.55.1")
            library("codec", "commons-codec:commons-codec:1.16.1")
            library("apache", "org.apache.commons:commons-lang3:3.0")
        }
    }
}
