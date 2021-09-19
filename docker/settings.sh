#!/bin/bash

# GraalVM (JVM) source code
GRAALVM_SOURCE="https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.0.0.2/graalvm-ce-java11-linux-amd64-21.0.0.2.tar.gz"

# This is for aarch64 (ARM) hardware
if [[ "$(uname -i)"  =~ .*aarch64* ]]; then
	GRAALVM_SOURCE="https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-21.0.0.2/graalvm-ce-java11-linux-aarch64-21.0.0.2.tar.gz"
fi

# Maven source code
MAVEN_SOURCE="https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz"

