FROM eclipse-temurin:25-jdk-jammy

WORKDIR /app

ARG JAR_FILE=bootstrap/api/build/libs/api.jar
COPY ${JAR_FILE} app.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
