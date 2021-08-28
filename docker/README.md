# Clank container micro-service stack

You can develop, build, test, and generate production containers for Clank using the following guide.

It is recommended to use the Clank Dev Script Tool (clank.sh for Mac/Linux, or clank.bat for Windows) to build the docker images.

Image types:
- `clank-dev` - Image used as a disposable dev environment container for testing/developing Clank.
- `clank` - Image that is designed to be clean, small, and used in production.

## How to build clank-dev for development

The `clank-dev` image is generated automatically when you run any of the `./clank.sh d-` Clank Dev Script Tool commands, such as `./clank.sh dbuild`.

All commands are relative to the root project path.

Examples:
```sh
# Example MAS
docker run --rm -it -p 10075:10075 \
	-u $(id -u ${USER}):$(id -g ${USER}) \
	-v "$(pwd):/mnt" clank-dev clank.sh run config/mas.json
```

## How to build clank for production

Pre-build Docker images are available on [Docker Hub](https://hub.docker.com/r/hashsploit/clank)!

Otherwise you can build the `clank` production image by running: `./clank.sh dpbuild`.

### Required volume mounts
- Configuration mount: `-v "$(pwd)/config/mas.json:/home/clank/config.json:ro"`
- Plugin mount: `-v "$(pwd)/plugins:/home/clank/plugins:ro"`

### Optional environment variables (-e)
 - `CLANK_MEM_INIT` = Initial JVM memory heap size (default: 128m).
 - `CLANK_MEM_MAX` = Maximum JVM memory heap size (default: 1024m).
 - `CLANK_CONFIG` = JSON configuration file name (default: config.json).

### Production Examples

All commands are relative to the root project path. The following production containers are persistant containers,
meaning they can be started and stopped via `docker start <name>` and `docker stop <name>`.

```sh
# Production MUIS
docker run -d --name muis -p 10071:10071 \
	-v "$(pwd)/config/muis.json:/home/clank/config.json:ro" \
	-v "$(pwd)/plugins:/home/clank/plugins:ro" clank

# Production MAS
docker run -d --name mas -p 10075:10075 \
	-v "$(pwd)/config/mas.json:/home/clank/config.json:ro" \
	-v "$(pwd)/plugins:/home/clank/plugins:ro" clank

# Production MLS
docker run -d --name mls -p 8484:8484 -p 10078:10078 \
	-v "$(pwd)/config/mls.json:/home/clank/config.json:ro" \
	-v "$(pwd)/plugins:/home/clank/plugins:ro" clank

# Production DME
docker run -d --name dme -p 10079:10079 -p 50000:50000/udp \
	-e CLANK_MEM_MAX="4096m" \
	-v "$(pwd)/config/dme.json:/home/clank/config.json:ro" \
	-v "$(pwd)/plugins:/home/clank/plugins:ro" clank

# Production NAT
docker run -d --name nat -p 10070:10070/udp \
	-v "$(pwd)/config/nat.json:/home/clank/config.json:ro" \
	-v "$(pwd)/plugins:/home/clank/plugins:ro" clank
```

