import com.google.protobuf.gradle.*

plugins {
    java
    application
    id("com.google.protobuf") version "0.8.17"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "net.hashsploit"
version = "0.1.8"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("net.hashsploit.clank.Main")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.34.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "build/generated/source/proto/main/grpc", "build/generated/source/proto/main/java")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.google.code.gson", "gson", "2.8.8")
    implementation("com.google.guava", "guava", "30.1.1-jre")
    implementation("io.netty", "netty-all", "4.1.67.Final")
    implementation("jline", "jline", "2.14.6")
    implementation("com.google.code.gson", "gson", "2.8.8")
    implementation("org.luaj", "luaj-jse", "3.0.1")
    implementation("com.google.code.gson", "gson", "2.8.8")
    implementation("org.apache.logging.log4j", "log4j-api", "2.14.1")
    implementation("org.apache.logging.log4j", "log4j-core", "2.14.1")
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", "2.14.1")
    implementation("org.slf4j", "slf4j-api", "1.7.32")
    implementation("io.grpc", "grpc-netty-shaded", "1.34.0")
    implementation("io.grpc", "grpc-protobuf", "1.34.0")
    implementation("io.grpc", "grpc-stub", "1.34.0")
    implementation("javax.annotation", "javax.annotation-api", "1.2")
    implementation("com.github.hashsploit", "medius-crypto", "master-SNAPSHOT")
    implementation("org.mariadb.jdbc", "mariadb-java-client", "2.1.2")
}
