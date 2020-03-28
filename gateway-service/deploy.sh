#!/usr/bin/env bash
#这里会同时构建client、common模块
mvn clean install -f pom.xml -pl client -am
mvn clean package -pl server -Dmaven.test.skip=true -Dtag.version=dev.latest
mvn dockerfile:push -pl server -Dtag.version=dev.latest
kubectl delete --ignore-not-found=true -f ./server/src/main/resources/gateway-dev.yaml
kubectl apply -f ./server/src/main/resources/gateway-dev.yaml

