#!/bin/bash

# GraalVM (JVM) source code
GRAALVM_SOURCE="https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-20.3.0/graalvm-ce-java11-linux-amd64-20.3.0.tar.gz"

# Maven source code
MAVEN_SOURCE="https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz"

# Adds additional docker build parameters to each of the other scripts automatically.
# For example: "--cpuset-cpus 0-3"
ADDITIONAL_BUILD_PARAMS=""

