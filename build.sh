#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Check if maven is installed
if ! command -v mvn &> /dev/null; then
    echo -e "$(tput bold)$(tput setaf 1)Maven is not installed, please install maven!$(tput sgr0)"
    exit 1
fi

# Compile the jar file
echo -e "Building ..."
mvn clean compile assembly:single

# Copy final jar to root path.
cp target/clank-*.jar clank.jar

