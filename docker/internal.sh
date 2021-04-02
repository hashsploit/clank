#!/bin/bash

#
# NOTE: This script is copied and ran inside the container.
# It is not supposed to be run manually or by the host machine.
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

# Start server
java -server -XX:+UseG1GC -Xms${CLANK_MEM_INIT} -Xmx${CLANK_MEM_MAX} -jar ${DIR}/clank.jar ${CLANK_CONFIG}

