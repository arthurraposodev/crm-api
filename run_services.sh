#!/bin/bash

mvn clean install -DskipTests

mvn -f ./gateway spring-boot:build-image -Dspring-boot.build-image.imageName=crm_gateway:latest -DskipTests
mvn -f ./user spring-boot:build-image -Dspring-boot.build-image.imageName=crm_user:latest -DskipTests
mvn -f ./customer spring-boot:build-image -Dspring-boot.build-image.imageName=crm_customer:latest -DskipTests

docker-compose -f compose_all_services.yaml up