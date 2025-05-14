# Use an official Java runtime as a parent image
FROM amazoncorretto:21.0.4-alpine3.18

# Установка зависимостей для Docker CLI
RUN apk add --no-cache curl bash

# Установка Docker CLI (последняя версия)
RUN DOCKER_VERSION=24.0.5 \
    && curl -L https://download.docker.com/linux/static/stable/x86_64/docker-$DOCKER_VERSION.tgz | tar -xz -C /tmp \
    && mv /tmp/docker/docker /usr/local/bin/docker \
    && chmod +x /usr/local/bin/docker \
    && rm -rf /tmp/docker

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/agu-back.jar agu-back.jar

# Expose the port that the application will run on
EXPOSE 8082

# Run the JAR file
ENTRYPOINT ["java", "-jar", "agu-back.jar"]