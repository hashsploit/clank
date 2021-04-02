# Clank container micro-service stack

Clank can be built and deployed as a docker micro-service stack using the
following steps.

## How to build

1. Configure the JSON files in the project root `config/` along with any plugins
   you may want `plugins/`.
2. Configure `docker-settings.sh` (this specifies the build parameters).
2. Build the Docker image using `./docker-build.sh`.
3. You can now deploy a container from the `clank` Docker image that was created.

Note: The `config/` and `plugins/` directories are mounted into the container.

## How to run a temporary container

Temporary containers are instantly destroyed the moment the server stops and leave no trace.

It's recommended to play around with this using the `./docker-run.sh` script with a configuration file as the parameter.

For example:

```bash
./docker-run.sh config/mas.json
```

### Temporary Clank MAS server
```bash
docker run -it --rm -v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro -v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro -p 10075:10075/tcp -e CLANK_MEM_INIT=512m -e CLANK_MEM_MAX=1024m -e CLANK_CONFIG=config/mas.json clank
```

### Temporary Clank MLS server
TCP port 8484 is used for the RPC server, while TCP port 10078 is for the actual MLS server.
```bash
docker run -it --rm -v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro -v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro -p 8484:8484/tcp -p 10078:10078/tcp -e CLANK_MEM_INIT=512m -e CLANK_MEM_MAX=1024m -e CLANK_CONFIG=config/mls.json clank
```

### Temporary Clank DME Server
```bash
docker run -it --rm -v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro -v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro -p 10079:10079/tcp -p 51000:51000/udp -e CLANK_MEM_INIT=512m -e CLANK_MEM_MAX=1024m -e CLANK_CONFIG=config/dme.json clank
```

## Run a persistant container

A persistant container can be started using `docker start <container_name>` and stopped using `docker stop <container_name>` at anytime.


### Clank MAS Server
```bash
docker run -h clank-mas -v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro -v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro -p 10075:10075/tcp -e CLANK_MEM_INIT=512m -e CLANK_MEM_MAX=1024m -e CLANK_CONFIG=config/mas.json --name clank-mas clank
```

### Clank MLS Server
```bash
docker run -h clank-mls -v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro -v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro -p 8484:8484/tcp -p 10078:10078/tcp -e CLANK_MEM_INIT=512m -e CLANK_MEM_MAX=1024m -e CLANK_CONFIG=config/mls.json --name clank-mls clank
```

### Clank DME Server
```bash
docker run -h clank-dme -v "$(dirname "$(pwd)")/config":/home/ratchet/config:ro -v "$(dirname "$(pwd)")/plugins":/home/ratchet/plugins:ro -p 10079:10079/tcp -p 51000:51000/udp -e CLANK_MEM_INIT=1024m -e CLANK_MEM_MAX=2048m -e CLANK_CONFIG=config/dme.json --name clank-dme clank
```

