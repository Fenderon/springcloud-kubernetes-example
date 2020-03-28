#!/usr/bin/env bash
#mvn install -f pom.xml
./service-parent/deploy.sh
./consumer-service/deploy.sh
./provide-service/deploy.sh
./gateway-service/deploy.sh