# Используем образ для сборки средствами Gradle
FROM gradle:8.3.0-jdk20-jammy AS build
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src/
RUN gradle clean build --no-daemon

# Второй этап: запускаем приложение
FROM amazoncorretto:20-alpine3.14
WORKDIR /app
COPY --from=build /app/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]