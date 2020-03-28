#!/usr/bin/env bash
#mvn install -f pom.xml
mvn clean install -f service-parent/pom.xml
mvn clean clean pacakge -f pom.xml -pl comsumer-service -am -Dmaven.test.skip=true -Dtag.version=dev.latest
mvn clean install -f pom.xml -pl comsumer-service -Dmaven.test.skip=true -Dtag.version=dev.latest
./service-parent/deploy.sh
./consumer-service/deploy.sh
./provide-service/deploy.sh
./gateway-service/deploy.sh

mvn clean install -f pom.xml -pl comsumer-service -am