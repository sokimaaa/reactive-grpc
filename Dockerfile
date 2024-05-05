FROM gradle:8.4-jdk17
COPY . .

RUN gradle build -xtest

EXPOSE 9090
ENTRYPOINT ["gradle", "run"]