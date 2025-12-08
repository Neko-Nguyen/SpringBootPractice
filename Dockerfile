FROM eclipse-temurin:17-jdk-alpine
MAINTAINER carameow.com
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]