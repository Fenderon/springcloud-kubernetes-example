#!/bin/bash
workdir=$(cd "$(dirname "$0")"; pwd)
echo ${workdir}
mvn install -f ${workdir}/pom.xml