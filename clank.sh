#!/usr/bin/env bash

# Change directory to the current script directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

SCRIPT="$0"

log() {
	echo -e "$(tput sgr0)${1}$(tput sgr0)"
}

check_installed() {
	# Check if command is installed
	if ! command -v ${1} &> /dev/null; then
		return 0
	fi
	return 1
}

require_installed() {
	# Check if command is installed
	if ! check_installed ${1} == 0; then
    	log "$(tput bold)$(tput setaf 1)${1} (${2}) is not installed, please install it!$(tput sgr0)"
    	exit 1
	fi
}

clean() {
	log "Cleaning ..."
	mvn clean
	rm -rf target/
}

compile_protobufs() {
	log "Building protocol-buffers ..."
	mvn clean protobuf:compile
}

build() {
	log "Building ..."
	mvn clean compile assembly:single
	
	# Copy final jar to root path
	cp target/clank-*.jar clank.jar
}

run() {
	# Execute server
	INIT_MEM="256m"
	MAX_MEM="1024m"
	JAR="clank.jar"
	
	# Check if the jar file exists
	if [ ! -f "${DIR}/${JAR}" ]; then
		log "$(tput bold)$(tput setaf 1)The jar file ${JAR} does not exist! Run '${SCRIPT} build' first.$(tput sgr0)"
		exit 1
	fi
	
	# Run server
	java -server -XX:+UseG1GC -Xms${INIT_MEM:=CLANK_INIT_MEM} -Xmx${MAX_MEM:=CLANK_MAX_MEM} -jar ${DIR}/${JAR} ${@:2}

	# Fix coloring
	echo -e "$(tput sgr0)"
}

docker_init() {
	# Check if docker image are already build, if not, build it.
	if [[ "$(docker images -q "clank-dev:latest" 2>/dev/null)" == "" ]]; then
		log "The docker image 'clank-dev' was not found, building ..."
		source docker/settings.sh
		
		docker build \
			--force-rm \
			--build-arg GRAALVM_SOURCE="${GRAALVM_SOURCE}" \
			--build-arg MAVEN_SOURCE="${MAVEN_SOURCE}" \
			--file docker/clank-dev.dockerfile \
			--tag clank-dev \
			docker/
	fi
}

docker_shell() {
	docker_init
	log "==== NOTE ===="
	log "> Project is mounted at /mnt"
	log "> You will be running as the root user in the container"
	docker run --rm --interactive --tty \
		-v "$(pwd):/mnt" \
		clank-dev /bin/bash
}

docker_clean() {
	docker_init
	docker run --rm \
		-u $(id -u ${USER}):$(id -g ${USER}) \
		-v "$(pwd):/mnt" \
		clank-dev ${SCRIPT} clean
}

docker_proto() {
	docker_init
	docker run --rm \
		-u $(id -u ${USER}):$(id -g ${USER}) \
		-v "$(pwd):/mnt" \
		clank-dev ${SCRIPT} proto
}

docker_build() {
	docker_init
	docker run --rm \
		-u $(id -u ${USER}):$(id -g ${USER}) \
		-v "$(pwd):/mnt" \
		clank-dev ${SCRIPT} build
}

docker_run() {
	docker_init
	docker run --rm --interactive --tty \
		-u $(id -u ${USER}):$(id -g ${USER}) \
		-v "$(pwd):/mnt" \
		clank-dev ${SCRIPT} run ${@:2}
}

docker_prod_build() {
	source docker/settings.sh
		
	docker build \
		--force-rm \
		--build-arg GRAALVM_SOURCE="${GRAALVM_SOURCE}" \
		--build-arg CI_SERVER="${CI_SERVER}" \
		--file docker/clank.dockerfile \
		--tag clank \
		docker/

	log ""
	log "Required volume mounts:"
	log " -v \"$(pwd)/config/mas.json:/home/clank/config.json:ro\""
	log " -v \"$(pwd)/plugins:/home/clank/plugins:ro\""
	log ""
	log "Optional environment variables (-e):"
	log " - CLANK_MEM_INIT = Initial JVM memory heap size"
	log " - CLANK_MEM_MAX = Maximum JVM memory heap size"
	log " - CLANK_CONFIG = JSON configuration file name"
	log ""
	log "Example:"
	log "$(tput setaf 4)docker run -d --name mas -p 10075:10075 -v \"\$(pwd)/config/mas.json:/home/clank/config.json:ro\" -v \"\$(pwd)/plugins:/home/clank/plugins:ro\" clank$(tput sgr0)"
}









# Menu
case "$1" in
	"clean")
		require_installed "java", "Java"
		require_installed "mvn", "Maven"
	    clean
	;;
	"proto" | "pt")
		require_installed "java", "Java"
		require_installed "mvn", "Maven"
	    compile_protobufs
	;;
	"build" | "jar")
		require_installed "java", "Java"
		require_installed "mvn", "Maven"
	    build
	;;
	"run")
		require_installed "java", "Java"
	    run $@
	;;
	"dshell")
		require_installed "docker", "Docker"
	    docker_shell
	;;
	"dclean")
		require_installed "docker", "Docker"
	    docker_clean
	;;
	"dproto" | "dpt")
		require_installed "docker", "Docker"
	    docker_proto
	;;
	"dbuild")
		require_installed "docker", "Docker"
	    docker_build
	;;
	"drun")
		require_installed "docker", "Docker"
	    docker_run $@
	;;
	"dpbuild")
		require_installed "docker", "Docker"
	    docker_prod_build
	;;
	*)
		echo -e "====================="
		echo -e "Clank Dev Script Tool"
		echo -e "====================="
		echo -e ""
		echo -e " Commands:"
		echo -e "  * clean ................ Clean-up the development environment"
		echo -e "  * proto, pt ............ Compile the protocol-buffer sources"
		echo -e "  * build, jar ........... Compile/build clank into an executable jar file"
		echo -e "  * run <JSON_CONFIG> .... Run clank with the JSON_CONFIG provided"
		echo -e ""
		echo -e " Docker Dev Commands: (see docker/README.md)"
		echo -e "  * dshell ............... Start a shell inside the clank-dev container"
		echo -e "  * dclean ............... Run 'clean' inside the clank-dev container"
		echo -e "  * dproto, dpt .......... Run 'proto' inside the clank-dev container"
		echo -e "  * dbuild, djar ......... Run 'build' inside the clank-dev container"
		echo -e "  * drun <JSON_CONFIG> ... Run 'run' inside the clank-dev container"
		echo -e "  * dpbuild .............. Generate a production ready Clank image"
	;;
esac

