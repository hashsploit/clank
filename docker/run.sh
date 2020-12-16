#!/bin/bash
# This script is copied and ran inside the container,
# modify it accordingly.

INIT_MEM="512m"
MAX_MEM="1024m"
JAR="clank.jar"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

# Start server
java -server -XX:+UseG1GC -Xms${INIT_MEM} -Xmx${MAX_MEM} -jar ${DIR}/${JAR} config/${CLANK_CONFIG}

