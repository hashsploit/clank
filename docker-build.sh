#!/bin/bash

# Build the base Docker image for Clank to be deployed as a micro-service.

DOCKER_IMAGE="clank"

# Set the directory to this script's current directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

# Reference config
source ./docker-settings.sh

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "$(tput bold)$(tput setaf 1)Docker is not installed, please install Docker!$(tput sgr0)"
    exit 1
fi

# Check if docker image are already build, if not, build it.
if [[ "$(docker images -q ${DOCKER_IMAGE} 2>/dev/null)" -ne "" ]]; then
	docker rmi --force ${DOCKER_IMAGE}
fi

docker build \
	--force-rm \
	--build-arg GRAALVM_SOURCE="${GRAALVM_SOURCE}" \
	--build-arg MAVEN_SOURCE="${MAVEN_SOURCE}" \
	--tag ${DOCKER_IMAGE} \
	${ADDITIONAL_BUILD_PARAMS} .

