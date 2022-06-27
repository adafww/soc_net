FROM adoptopenjdk/openjdk11:jre-11.0.11_9
ARG JAR_FILE=/build/libs/*.jar
WORKDIR /app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]