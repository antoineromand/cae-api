#!/bin/sh

docker build -f CustomFlyway.Dockerfile -t custom-flyway:latest .

docker run --rm -e FLYWAY_URL='jdbc:postgresql://host.docker.internal:5450/pae-db' \
  -e FLYWAY_USER='root' \
  -e FLYWAY_PASSWORD='LePoissonSteve?' \
  custom-flyway:latest