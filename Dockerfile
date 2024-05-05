FROM openjdk:17

COPY ./infrastructure/build/libs/*.jar ./app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]