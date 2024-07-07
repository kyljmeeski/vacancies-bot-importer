FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /
COPY /src /src
COPY pom.xml /
RUN mvn -DskipTests=true -f /pom.xml clean package

FROM eclipse-temurin:17-jre
WORKDIR /
COPY --from=build /target/*.jar importer.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "importer.jar"]