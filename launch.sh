#!/bin/bash
INIT_MEM="512m"
MAX_MEM="1024m"
JAR="clank.jar"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Check if the jar file exists
if [ ! -f "${DIR}/${JAR}" ]; then
	echo -e "$(tput bold)$(tput setaf 1)The jar file ${JAR} does not exist! Run ./build.sh first.$(tput sgr0)"
	exit 1
fi

# Check if java is installed
if ! command -v java &> /dev/null; then
    echo -e "$(tput bold)$(tput setaf 1)Java is not installed, please install java!$(tput sgr0)"
    exit 1
fi

# Execute server
java -server -XX:+UseG1GC -Xms${INIT_MEM} -Xmx${MAX_MEM} -jar ${DIR}/${JAR} $@

# Fix coloring
echo -e "$(tput sgr0)"

