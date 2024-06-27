FROM amazoncorretto:17
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ./target/GymFinalProject-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
