FROM debian:bullseye-slim
LABEL name="clank"
LABEL description="Clank - A high-performance SCE-RT Medius server"
LABEL maintainer="hashsploit <hashsploit@protonmail.com>"

# Install dependencies
RUN echo "Updating packages ..." \
	&& apt-get update -y >/dev/null 2>&1 \
	&& echo "Installing dependencies ..." \
	&& apt-get install -y \
	curl \
	build-essential \
	libz-dev \
	zlib1g-dev >/dev/null 2>&1

# Remove bloat
RUN rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
	&& apt-get autoremove --purge -y \
	&& apt-get clean -y \
	&& rm -rf /usr/share/man \
	&& rm -rf /usr/share/locale \
	&& rm -rf /usr/share/doc \
	&& mkdir -p /etc/initramfs-tools/conf.d/ \
	&& echo "COMPRESS=xz" | tee /etc/initramfs-tools/conf.d/compress >/dev/null 2>&1

# Download GraalVM
ARG GRAALVM_SOURCE
RUN echo "Downloading GraalVM from ${GRAALVM_SOURCE} ..." \
	&& curl -sLo /tmp/graal.tar.gz ${GRAALVM_SOURCE}

# Install GraalVM
RUN echo "Installing GraalVM ..." \
	&& cd /tmp \
	&& tar -xzf graal.tar.gz \
	&& rm graal.tar.gz \
	&& mkdir -p /usr/lib/jvm \
	&& mv graalvm*/ /usr/lib/jvm/ \
	&& cd /usr/lib/jvm \
	&& ln -s /usr/lib/jvm/graalvm*/bin/java /usr/bin/java \
	&& ln -s /usr/lib/jvm/graalvm*/bin/javac /usr/bin/javac \
	&& ln -s /usr/lib/jvm/graalvm*/bin/javap /usr/bin/javap \
	&& ln -s /usr/lib/jvm/graalvm*/bin/jar /usr/bin/jar \
	&& ln -s /usr/lib/jvm/graalvm*/bin/gu /usr/bin/gu

# Add standard java utils to path
ENV PATH="/usr/bin/java:/usr/bin/javap:/usr/bin/javac:/usr/bin/jar:/usr/bin/gu:${PATH}"

# Install "native-image" using "gu"
RUN echo "Installing native-image ..." \
	&& gu install native-image \
	&& ln -s /usr/lib/jvm/graalvm*/bin/native-image /usr/bin/native-image \
	&& chmod +x /usr/bin/*

# Add native-image to path
ENV PATH="/usr/bin/native-image:${PATH}"

ARG MAVEN_SOURCE
RUN echo "Downloading Maven from ${MAVEN_SOURCE} ..." \
	&& curl -sLo /tmp/maven.tar.gz ${MAVEN_SOURCE}

# Install maven
RUN echo "Installing maven ..." \
	&& cd /tmp \
	&& tar -xzf maven.tar.gz \
	&& rm maven.tar.gz \
	&& mv apache-maven-* /opt/maven/ \
	&& chmod +x /opt/maven/bin/*

# Add maven to path
ENV PATH="/opt/maven/bin:$PATH"

# Set up the non-root user
RUN adduser --shell /bin/bash --gecos "" --disabled-password --quiet ratchet

# Copy everything into the container for virtualization and set permissions
ADD ./ /home/ratchet
RUN echo "Setting permissions ..." \
	&& chown ratchet:ratchet -R /home/ratchet

# Switch user
USER ratchet
WORKDIR /home/ratchet

# Download the latest medius-crypto library
ARG MEDIUS_CRYPTO_SOURCE
RUN echo "Downloading medius-crypto from ${MEDIUS_CRYPTO_SOURCE} ..." \
	&& curl -sLo /tmp/medius-crypto.tar.gz ${MEDIUS_CRYPTO_SOURCE}

# Unzip and build the dependency
RUN echo "Building medius-crypto ..." \
	&& cd /tmp \
	&& tar -xzf medius-crypto.tar.gz \
	&& cd medius-crypto-*/ \
	&& mvn clean install

# Build project and clean up
RUN echo "Building Clank ..." \
	&& cd /home/ratchet/ \
	&& mv docker/docker-run.sh . \
	&& chmod +x build.sh docker-run.sh \
	&& ./build.sh \
	&& rm -rf bin/ target/ src/ .settings/ .git/ docker/ \
	&& rm -f *.bat \
	&& rm -f *.md \
	&& rm -f Dockerfile \
	&& rm -f launch.sh \
	&& rm -f build.sh \
	&& rm -f compile-protobufs.sh \
	&& rm -f pom.xml \
	&& rm -f .classpath \
	&& rm -f .project

ENTRYPOINT ["/bin/bash", "./docker-run.sh"]

