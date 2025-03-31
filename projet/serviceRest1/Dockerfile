FROM maven:3.8.3-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENV DATABASE_URL=jdbc:mysql://host.docker.internal:3306/bd_monprojet
ENTRYPOINT ["java", "-jar", "app.jar"] 