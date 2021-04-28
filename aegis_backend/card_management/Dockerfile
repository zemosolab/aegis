FROM java:8-jdk

EXPOSE 8002

RUN mkdir /app

WORKDIR /app

ARG JAR_FILE=target/*jar

COPY ${JAR_FILE} /app/cardmanagement-0.0.1-SNAPSHOT.jar

COPY .env .

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/cardmanagement-0.0.1-SNAPSHOT.jar"]
