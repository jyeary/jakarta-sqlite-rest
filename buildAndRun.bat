@echo off
call mvn clean package
call docker build -t jyeary/jakarta-sqlite-rest:1.0.0 .
call docker rm -f jakarta-sqlite-rest
call docker run -d -p 8080:8080 -p 8443:8443 --name jakarta-sqlite-rest jyeary/jakarta-sqlite-rest:1.0.0