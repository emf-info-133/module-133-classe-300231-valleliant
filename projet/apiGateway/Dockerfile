FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=${JAVA_OPTS}
WORKDIR /app
COPY target/module133classe300231valleliant1-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT exec java ${JAVA_OPTS} -jar module133classe300231valleliant1-0.0.1-SNAPSHOT.jar
# OPTIONNEL : Pour améliorer les performances de démarrage
# ENTRYPOINT exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar gateway-0.0.1-SNAPSHOT.jar
