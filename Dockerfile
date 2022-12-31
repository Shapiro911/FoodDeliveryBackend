FROM gradle:latest AS builder
WORKDIR /code
COPY build.gradle settings.gradle /code
COPY . .
RUN gradle build

FROM openjdk:17
WORKDIR /code
ARG JAR_FILE=/code/build/libs/*.jar
COPY --from=builder ${JAR_FILE} application.jar

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]