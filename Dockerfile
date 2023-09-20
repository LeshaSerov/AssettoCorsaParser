FROM maven:3.9.1-amazoncorretto-20-debian-bullseye AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package

FROM amazoncorretto:20-alpine3.14
WORKDIR /app
COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


#docker run --name name -d --rm jpa
