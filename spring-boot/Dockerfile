FROM eclipse-temurin
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ADD data /data
ENTRYPOINT ["java", "-jar", "/app.jar"]