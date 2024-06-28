FROM amazoncorretto:17
ARG JAR_FILE=target/GymFinalProject-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY ${JAR_FILE} gym-service.jar
ENV SPRING_PROFILES_ACTIVE=default,dev,integration
ENTRYPOINT ["java","-jar","gym-service.jar"]
