# Clank container micro-service stack

Clank can be built and deployed as a docker micro-service stack using the
following steps.

## How to build

1. Configure the JSON files in the project root `config/` along with any plugins
   you may want `plugins/`.
2. Configure `docker-settings.sh` (this specifies the build parameters) and `docker-run.sh`
   (this will be the script that starts the servers inside the container).
2. Build the Docker image using `./docker-build.sh`.
3. You can now run the docker image and setting the `CLANK_CONFIG` to the config
   file (without the `config/` directory prefix). For example `mas.json`.

Note: once you build the image, the configuration files are already copied over,
therefore if you want to modify the configuration you will need to rebuild the image.
This also includes any changes to the `docker-run.sh` file.

Examples:

```bash
# Example usage
docker run -it --rm -h ${HOSTNAME} -p ${HOST_TCP_PORT}:8080/tcp -e CLANK_CONFIG=config.json --name ${CONTAINER_NAME} clank

# Run a Clank MAS
docker run -it --rm -h clank-mas -p 10075:10075/tcp -e CLANK_CONFIG=mas.json --name clank-mas clank

# Run a Clank DME Server
docker run -it --rm -h clank-dme -p 10079:10079/tcp -p 51000:51000/udp -e CLANK_CONFIG=dme.json --name clank-dme clank
```

## Run example scripts

If you just want to quickly see an example you can run `./example-mas.sh` after building
to deploy a Clank Medius Authentication Server that runs on TCP port 10075 and uses the
configuration file `clank/mas.json`.

