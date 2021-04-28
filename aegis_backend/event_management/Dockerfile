FROM java:8-jdk

EXPOSE 8805

RUN mkdir /app

WORKDIR /app

ARG JAR_FILE=target/*jar

COPY ${JAR_FILE} /app/eventmanagement-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/eventmanagement-0.0.1-SNAPSHOT.jar"]
