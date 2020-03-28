#!/bin/bash
workdir=$(cd "$(dirname "$0")"; pwd)
echo ${workdir}
#这里会同时构建client、common模块
mvn clean install -f ${workdir}/pom.xml -pl client -am
mvn clean package -f ${workdir}/pom.xml -pl server -Dmaven.test.skip=true -Dtag.version=dev.latest
mvn dockerfile:push -f ${workdir}/pom.xml -pl server -Dtag.version=dev.latest
kubectl delete --ignore-not-found=true -f ${workdir}/server/src/main/resources/provide-dev.yaml
kubectl apply -f ${workdir}/server/src/main/resources/provide-dev.yaml

