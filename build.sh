#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if ! command -v mvn &> /dev/null; then
    echo -e "$(tput bold)$(tput setaf 1)Maven not installed, please install maven!$(tput sgr0)"
    exit 1
fi

echo -e "Building ..."

mvn clean compile assembly:single

cp target/clank-*.jar clank.jar

