FROM maven:3.8.3-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ENV DATABASE_URL=jdbc:mysql://mysql-db:3306/133-db-alparslanc
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar 