plugins {
    java
    idea
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
}

val grpcVersion = "1.56.1"
val protobufVersion = "4.28.2"
val protocVersion = protobufVersion

dependencies {
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("com.google.errorprone:error_prone_annotations:2.18.0")
    implementation("org.apache.tomcat:annotations-api:6.0.53")
    implementation ("ch.qos.logback:logback-classic")
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protocVersion"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}

idea {
    module {
        sourceDirs.add(file("build/generated/source/proto/main/grpc"))
        sourceDirs.add(file("build/generated/source/proto/main/java"))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

idea {
    module {
        sourceDirs.add(file("build/generated/source/proto/main/grpc"))
        sourceDirs.add(file("build/generated/source/proto/main/java"))
    }
}