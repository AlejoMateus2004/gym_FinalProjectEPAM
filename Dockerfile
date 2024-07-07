FROM amazoncorretto:17
ARG JAR_FILE=target/GymFinalProject-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} gym-service.jar
COPY src/main/resources/static/InitialGymData.xlsx /app/src/main/resources/static/InitialGymData.xlsx
ENTRYPOINT ["java","-jar","gym-service.jar"]
