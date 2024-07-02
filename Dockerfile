FROM amazoncorretto:17
ARG JAR_FILE=target/GymFinalProject-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} gym-service.jar
ENTRYPOINT ["java","-jar","gym-service.jar"]
