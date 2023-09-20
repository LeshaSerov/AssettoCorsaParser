FROM amazoncorretto:20-alpine3.14
COPY gradle-wrapper.jar gradle-wrapper.jar
CMD \["java","-jar","gradle-wrapper.jar"\]


FROM gradle:4.7.0-jdk20-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:8-jre-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]

#docker run --name name -d --rm jpa
# Используем образ Amazon Corretto на базе Alpine Linux 3.14
FROM amazoncorretto:20-alpine3.14
# Устанавливаем переменную окружения для указания домашней директории приложения
ENV APP_HOME /app
# Создаем директорию для приложения
RUN mkdir -p $APP_HOME
# Устанавливаем рабочую директорию внутри контейнера
WORKDIR $APP_HOME
# Копируем файлы сборки Gradle внутрь контейнера
COPY build/libs/*.jar $APP_HOME/app.jar
# Копируем JAR-файлы из поддиректории build/libs внутрь контейнера
COPY build/libs/*.jar $APP_HOME/app.jar

# Опционально: копируем дополнительные ресурсы (например, конфигурационные файлы) в директорию приложения

# Указываем команду для запуска приложения
CMD ["java", "-jar", "/app.jar"]
