FROM openjdk:17 as builder
WORKDIR /code
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17
WORKDIR /code
COPY --from=builder /code/dependencies/ .
COPY --from=builder /code/spring-boot-loader/ .
COPY --from=builder /code/snapshot-dependencies/ .
RUN true
COPY --from=builder /code/application/ .

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]