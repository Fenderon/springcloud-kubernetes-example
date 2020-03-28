#!/usr/bin/env bash
#mvn install -f pom.xml
sh ./service-parent/deploy.sh
sh ./service-rpc/deploy.sh
sh ./service-common/deploy.sh
sh ./module-cache/deploy.sh
sh ./module-field-comparator//deploy.sh
sh ./consumer-service/deploy.sh
sh ./provide-service/deploy.sh
sh ./gateway-service/deploy.sh