#!/bin/bash

CLANK_MEM_INIT="512m"
CLANK_MEM_MAX="1024m"

# Set the directory to this script's current directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR

if [[ $# -eq 0 ]]; then
	echo -e "Usage: $0 config/configuration.json"
	exit 1
fi

docker run \
	-it \
	--rm \
	-v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro \
	-v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro \
	-p 8484:8484/tcp \
	-p 10071:10071/tcp \
	-p 10075:10075/tcp \
	-p 10078:10078/tcp \
	-p 10079:10079/tcp \
	-p 50000:50000/udp \
	-p 50001:50001/udp \
	-e CLANK_DEBUG="true" \
	-e CLANK_MEM_INIT="${CLANK_MEM_INIT}" \
	-e CLANK_MEM_MAX="${CLANK_MEM_MAX}" \
	-e CLANK_CONFIG="$@" \
	clank

