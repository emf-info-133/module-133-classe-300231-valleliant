FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ENV DATABASE_URL=jdbc:mysql://host.docker.internal:3306/133-db-alparslanc
COPY target/module133classe300231valleliant1-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
#ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar 