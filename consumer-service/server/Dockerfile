FROM openjdk:8-jre-alpine
ENV TIME_ZONE=Asia/Shanghai
RUN echo "${TIME_ZONE}" > /etc/timezone
RUN apk add --no-cache tzdata && ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar