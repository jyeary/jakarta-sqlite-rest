#!/bin/sh
mvn clean package \
&& docker build -t jyeary/jakarta-sqlite-rest:1.0.0 .
docker container rm -f jakarta-sqlite-rest || true \
&& docker container run -d -p 8080:8080 -p 8443:8443 --name jakarta-sqlite-rest jyeary/jakarta-sqlite-rest:1.0.0